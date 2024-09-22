package live.crowdcontrol.cc4j.websocket;

import com.auth0.jwt.JWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import live.crowdcontrol.cc4j.EventListener;
import live.crowdcontrol.cc4j.websocket.listeners.WhoAmIListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConnectedPlayer extends WebSocketClient {
	private static final Gson GSON = new GsonBuilder().create();
	private static final List<EventListener<?>> LISTENERS;
	private static final Logger log = LoggerFactory.getLogger(ConnectedPlayer.class);
	private String token;
	private UserToken userToken;

	static {
		List<EventListener<?>> listeners = new ArrayList<>();
		listeners.add(new WhoAmIListener());
		LISTENERS = Collections.unmodifiableList(listeners);
	}

	public ConnectedPlayer() {
		super(URI.create("wss://pubsub.crowdcontrol.live/"));
	}

	public void setToken(String token) {
		this.userToken = GSON.fromJson(JWT.decode(token).getPayload(), UserToken.class);
		this.token = token;
	}

	private void send(Request request) {
		send(GSON.toJson(request));
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		send(new Request("whoami"));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void onMessage(String message) {
		Event event = GSON.fromJson(message, Event.class);
		for (EventListener listener : LISTENERS) {
			String domain = listener.domain();
			if (!Objects.equals(event.type, listener.type()) || (!Objects.equals("*", domain) && !Objects.equals(event.domain, listener.domain())))
				continue;
			try {
				listener.handle(this, GSON.fromJson(event.payload, listener.dataClass()));
			} catch (Exception e) {
				log.error("{} failed to handle event", listener.getClass().getName(), e);
			}
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onError(Exception ex) {
		log.error("An unknown error has occurred", ex);
	}
}
