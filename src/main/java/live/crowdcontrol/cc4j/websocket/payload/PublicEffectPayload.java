package live.crowdcontrol.cc4j.websocket.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PublicEffectPayload {

	private @NotNull UUID requestID;
	private int timestamp;
	private @NotNull CCEffectDescription effect;
	private @NotNull CCSanitizedUserRecord target;
	private @Nullable CCSanitizedUserRecord origin;
	private @Nullable CCSanitizedUserRecord requester;
	private boolean anonymous;
	private int quantity;
	private int localTimestamp; // nullable
	// sourceDetails
	// game
	// gamePack
	// parameters

	// json fixers

	@JsonProperty("quantity")
	void setQuantity(int quantity) {
		this.quantity = Math.max(1, quantity);
	}

	// boring getters

	public @NotNull UUID getRequestID() {
		return requestID;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public @NotNull CCEffectDescription getEffect() {
		return effect;
	}

	public @NotNull CCSanitizedUserRecord getTarget() {
		return target;
	}

	public @Nullable CCSanitizedUserRecord getOrigin() {
		return origin;
	}

	public @Nullable CCSanitizedUserRecord getRequester() {
		return requester;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getLocalTimestamp() {
		return localTimestamp;
	}
}
