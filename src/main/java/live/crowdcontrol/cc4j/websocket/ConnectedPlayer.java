package live.crowdcontrol.cc4j.websocket;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import live.crowdcontrol.cc4j.CCEventType;
import live.crowdcontrol.cc4j.CCPlayer;
import live.crowdcontrol.cc4j.CrowdControl;
import live.crowdcontrol.cc4j.util.EventManager;
import live.crowdcontrol.cc4j.websocket.data.SubscriptionData;
import live.crowdcontrol.cc4j.websocket.payload.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ApiStatus.Internal
public class ConnectedPlayer extends WebSocketClient implements CCPlayer {
	private static final ObjectMapper JACKSON;
	private static final Logger log = LoggerFactory.getLogger(ConnectedPlayer.class);
	private final @NotNull Set<String> subscriptions = new HashSet<>();
	private final @NotNull EventManager eventManager;
	private final @NotNull UUID uuid;
	private final @NotNull Path tokenPath;
	private @Nullable String connectionID;
	private @Nullable String token;
	private @Nullable UserToken userToken;
	private boolean privateAvailable;

	static {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule("CrowdControlSerializers");
		module.addDeserializer(CCName.class, new CCName.CCNameAdapter());
		mapper.registerModule(module);
		JACKSON = mapper;
	}

	// WebSocket Impl

	public ConnectedPlayer(@NotNull UUID uuid, @NotNull CrowdControl parent) {
		super(URI.create("wss://pubsub.crowdcontrol.live/"));

		this.uuid = uuid;
		this.tokenPath = parent.getDataFolder().resolve(uuid + ".token");
		this.eventManager = new EventManager(parent);

		loadToken();
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		eventManager.dispatch(CCEventType.CONNECTION);
		send(new SocketRequest("whoami"));
	}

	@Override
	public void onMessage(String message) {
		try {
			SocketEvent event = JACKSON.readValue(message, SocketEvent.class);
			switch (event.type) {
				case "whoami":
					connectionID = JACKSON.treeToValue(event.payload, WhoAmIPayload.class).connectionID;
					// TODO: emit connected event
					break;
				case "login-success":
					setToken(JACKSON.treeToValue(event.payload, LoginSuccessPayload.class).token);
					saveToken();
					break;
				case "subscription-result":
					assert this.userToken != null : "Subscribed before authenticating";
					SubscriptionResultPayload subscriptionPayload = JACKSON.treeToValue(event.payload, SubscriptionResultPayload.class);
					subscriptions.addAll(subscriptionPayload.getSuccess());
					privateAvailable = subscriptions.contains("prv/" + this.userToken.ccUID);
					break;
				case "effect-request":
					if (!event.domain.equals("pub") && !event.domain.equals("prv")) return;
					if (privateAvailable && event.domain.equals("pub")) return;
					PublicEffectPayload effectRequestPayload = JACKSON.treeToValue(event.payload, PublicEffectPayload.class);
					break;
				default:
					log.debug("Ignoring unknown event {} on domain {}", event.type, event.domain);
			}
		} catch (Exception e) {
			log.warn("Failed to handle incoming message {}", message, e);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO: remove from CrowdControl
	}

	@Override
	public void onError(Exception ex) {
		log.error("An unknown error has occurred", ex);
	}

	// Semi Boilerplate

	public void send(SocketRequest request) {
		try {
			send(JACKSON.writeValueAsString(request));
		} catch (JsonProcessingException e) {
			log.warn("Failed to send message {}", request, e);
		}
	}

	public boolean setToken(String token) {
		try {
			this.userToken = JACKSON.readValue(JWT.decode(token).getPayload(), UserToken.class);

			if (Instant.ofEpochSecond(this.userToken.exp).plus(1, ChronoUnit.DAYS).isBefore(Instant.now())) {
				log.warn("User {}'s auth token has expired", uuid);
				this.userToken = null;
				return false;
			}
		} catch (JsonProcessingException e) {
			log.warn("Failed to set token {}", token, e);
			return false;
		}
		this.token = token;
		subscribe();

		// TODO: emit login event
		return true;
	}


	private boolean loadToken() {
		if (!Files.exists(tokenPath))
			return false;

		try {
			try (BufferedReader reader = Files.newBufferedReader(tokenPath)) {
				return setToken(reader.readLine());
			}
		} catch (Exception e) {
			log.warn("Failed to read user {} token", uuid, e);
			return false;
		}
	}

	private void saveToken() {
		if (this.token == null)
			return;

		try {
			Files.write(tokenPath, token.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.warn("Failed to write user {} token", uuid, e);
		}
	}

	private void subscribe() {
		if (this.token == null || this.userToken == null)
			return;

		Set<String> subscribeTo = new HashSet<>(Arrays.asList(
			"pub/" + this.userToken.ccUID,
			"prv/" + this.userToken.ccUID
		));

		subscribeTo.removeAll(subscriptions);

		if (subscribeTo.isEmpty()) return;

		send(new SocketRequest(
			"subscribe",
			new SubscriptionData(
				subscribeTo,
				this.token
			)
		));
	}

	// True Boilerplate

	@NotNull
	public UUID getUuid() {
		return uuid;
	}

	@Nullable
	public String getConnectionID() {
		return connectionID;
	}

	@Nullable
	public String getToken() {
		return token;
	}

	@Nullable
	public UserToken getUserToken() {
		return userToken;
	}

	@Override
	public @NotNull EventManager getEventManager() {
		return eventManager;
	}
}
