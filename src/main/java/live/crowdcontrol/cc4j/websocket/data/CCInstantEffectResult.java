package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CCInstantEffectResult extends CCEffectResult {

	@JsonCreator
	CCInstantEffectResult(@JsonProperty("request") @NotNull UUID request,
						  @JsonProperty("stamp") int stamp,
						  @JsonProperty("message") @NotNull String message,
						  @JsonProperty("status") @NotNull ResultStatus status) {
		super(request, stamp, message, status);
	}

	public CCInstantEffectResult(@NotNull UUID requestID,
								 @NotNull ResultStatus status,
								 @NotNull String message) {
		super(requestID, status, message);

		if (status.isTimed()) {
			throw new IllegalArgumentException("Expected an instant status, received timed status " + status);
		}
	}
}
