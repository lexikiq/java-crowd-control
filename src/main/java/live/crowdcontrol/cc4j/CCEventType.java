package live.crowdcontrol.cc4j;

import io.leangen.geantyref.TypeToken;
import live.crowdcontrol.cc4j.websocket.payload.PublicEffectPayload;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CCEventType<T> {
	private final @NotNull String listenerId;
	private final @NotNull TypeToken<T> typeToken;

	public CCEventType(@NotNull String listenerId, @NotNull TypeToken<T> typeToken) {
		this.listenerId = listenerId;
		this.typeToken = typeToken;
	}

	public CCEventType(@NotNull String listenerId, @NotNull Class<T> clazz) {
		this(listenerId, TypeToken.get(clazz));
	}

	public static CCEventType<Void> ofVoid(@NotNull String listenerId) {
		return new CCEventType<>(listenerId, Void.class);
	}

	public @NotNull String getListenerId() {
		return listenerId;
	}

	public @NotNull TypeToken<T> getTypeToken() {
		return typeToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CCEventType<?> that = (CCEventType<?>) o;
		return Objects.equals(listenerId, that.listenerId) && Objects.equals(typeToken, that.typeToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(listenerId, typeToken);
	}

	/**
	 * Called when a player's WebSocket initially establishes its connection.
	 * The connectionID will be unavailable at this point.
	 */
	public static final CCEventType<Void> CONNECTION = ofVoid("connection");

	/**
	 * Called when a player's WebSocket connectionID becomes known.
	 */
	public static final CCEventType<Void> IDENTIFIED = ofVoid("identified");

	/**
	 * Called when a player's WebSocket becomes authenticated.
	 * The connectionID may be unavailable at this point.
	 */
	public static final CCEventType<Void> AUTHENTICATED = ofVoid("authenticated");

	/**
	 * Called when a player's authentication token expires.
	 * This may happen if they have not re-authenticated in about 6 months.
	 */
	public static final CCEventType<Void> AUTH_EXPIRED = ofVoid("auth_expired");

	/**
	 * Called when a player's auth token is removed for any reason.
	 * Shares overlap with {@link #AUTH_EXPIRED}.
	 */
	public static final CCEventType<Void> UNAUTHENTICATED = ofVoid("unauthenticated");

	/**
	 * Called when an {@code effect-request} on the {@code pub} domain comes in from the player.
	 * Note that the traditional way to receive this information is via {@link CCEffect#onTrigger(PublicEffectPayload, CCPlayer)}.
	 */
	public static final CCEventType<PublicEffectPayload> PUB_EFFECT_REQUEST = new CCEventType<>("pub_effect_request", PublicEffectPayload.class);
}
