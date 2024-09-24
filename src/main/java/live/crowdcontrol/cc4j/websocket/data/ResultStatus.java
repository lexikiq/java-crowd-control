package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ResultStatus {
	SUCCESS("success", false),
	FAIL_TEMPORARY("failTemporary", false),
	FAIL_PERMANENT("failPermanent", false),
	DELAY_ESTIMATED("delayEstimated", true),
	TIMED_BEGIN("timedBegin", true),
	TIMED_PAUSE("timedPause", true),
	TIMED_RESUME("timedResume", true),
	TIMED_END("timedEnd", true),
	@JsonEnumDefaultValue
	UNKNOWN("", false),
	;

	// Static

	private static final Map<String, ResultStatus> BY_VALUE;

	static {
		Map<String, ResultStatus> byValue = new HashMap<>();
		for (ResultStatus status : values()) {
			if (status == UNKNOWN) continue;
			byValue.put(status.value, status);
		}
		BY_VALUE = Collections.unmodifiableMap(byValue);
	}

	/**
	 * Gets a status from its JSON string value.
	 * If a status by the provided name could not be found, returns {@link #UNKNOWN}.
	 *
	 * @param value JSON string value
	 * @return result status value
	 */
	@JsonCreator
	public static @NotNull ResultStatus fromValue(@NotNull String value) {
		return BY_VALUE.getOrDefault(value, UNKNOWN);
	}

	// Instance

	private final @NotNull String value;
	private final boolean timed;

	ResultStatus(@NotNull String value, boolean timed) {
		this.value = value;
		this.timed = timed;
	}

	/**
	 * Gets the encoded string value of this status.
	 *
	 * @return json value
	 */
	@JsonValue
	public @NotNull String getValue() {
		return value;
	}

	/**
	 * Gets whether this status represents a timed status.
	 * A timed status is required to use {@link live.crowdcontrol.cc4j.websocket.data.CCTimedEffectResult CCTimedEffectResult},
	 * while non-timed statuses are required to use {@link live.crowdcontrol.cc4j.websocket.data.CCInstantEffectResult CCInstantEffectResult}.
	 *
	 * @return is timed status
	 */
	public boolean isTimed() {
		return timed;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
