package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class CCEffectResult {
	protected final @NotNull UUID request;
	protected final int stamp;
	protected final @NotNull String message;
	protected final @NotNull ResultStatus status;

	@JsonCreator
	CCEffectResult(@JsonProperty("request") @NotNull UUID request,
				   @JsonProperty("stamp") int stamp,
				   @JsonProperty("message") @NotNull String message,
				   @JsonProperty("status") @NotNull ResultStatus status) {
		this.request = request;
		this.stamp = stamp;
		this.message = message;
		this.status = status;
	}

	public CCEffectResult(@NotNull UUID requestID,
						  @NotNull ResultStatus status,
						  @NotNull String message) {
		this.stamp = (int) (System.currentTimeMillis() / 1000L);
		this.request = requestID;
		this.status = status;
		this.message = message;
	}

	/**
	 * Gets the ID of the request that this result is in response to.
	 *
	 * @return request ID
	 */
	public @NotNull UUID getRequestID() {
		return request;
	}

	/**
	 * Gets the timestamp in seconds since Unix epoch that this result was generated.
	 *
	 * @return unix epoch seconds timestamp
	 */
	public int getTimestamp() {
		return stamp;
	}

	/**
	 * Gets the message to be displayed to the viewer about the status.
	 * May not be displayed if the status is successful.
	 * String may be empty if nothing is to be displayed.
	 *
	 * @return message
	 */
	public @NotNull String getMessage() {
		return message;
	}

	/**
	 * The status value of the result.
	 *
	 * @return result status
	 */
	public @NotNull ResultStatus getStatus() {
		return status;
	}
}
