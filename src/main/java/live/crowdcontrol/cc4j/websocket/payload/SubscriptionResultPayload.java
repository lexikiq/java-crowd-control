package live.crowdcontrol.cc4j.websocket.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionResultPayload {
	private final @NotNull Set<@NotNull String> success;
	private final @NotNull Set<@NotNull String> failure;

	@JsonCreator
	public SubscriptionResultPayload(@JsonProperty("success") @NotNull Set<@NotNull String> success,
									 @JsonProperty("failure") @NotNull Set<@NotNull String> failure) {
		this.success = Collections.unmodifiableSet(new HashSet<>(success));
		this.failure = Collections.unmodifiableSet(new HashSet<>(failure));
	}

	/**
	 * Gets the set of successfully subscribed topics.
	 * It is immutable.
	 *
	 * @return subscribed topics
	 */
	public @NotNull Set<@NotNull String> getSuccess() {
		return success;
	}

	/**
	 * Gets the set of topics that could not be subscribed to.
	 * This usually indicates a permission failure.
	 * It is immutable.
	 *
	 * @return not subscribed topics
	 */
	public @NotNull Set<@NotNull String> getFailure() {
		return failure;
	}
}
