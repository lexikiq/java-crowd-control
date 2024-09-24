package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CCTimedEffectResult extends CCEffectResult {
	protected int timeRemaining;

	@JsonCreator
	CCTimedEffectResult(@JsonProperty("request") @NotNull UUID request,
						@JsonProperty("stamp") int stamp,
						@JsonProperty("message") @NotNull String message,
						@JsonProperty("status") @NotNull ResultStatus status,
						int timeRemaining) {
		super(request, stamp, message, status);
		this.timeRemaining = timeRemaining;
	}

	public CCTimedEffectResult(@NotNull UUID requestID,
							   @NotNull ResultStatus status,
							   @NotNull String message,
							   int timeRemaining) {
		super(requestID, status, message);
		this.timeRemaining = timeRemaining;

		if (!status.isTimed()) {
			throw new IllegalArgumentException("Expected a timed status, received instant status " + status);
		}
	}
}
