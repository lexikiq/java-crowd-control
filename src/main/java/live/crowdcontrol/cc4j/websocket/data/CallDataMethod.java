package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public class CallDataMethod<A> {
	private final @NotNull String value;

	@JsonCreator
	public CallDataMethod(@NotNull String value) {
		this.value = value;
	}

	/**
	 * Returns the string value of this method.
	 *
	 * @return method id
	 */
	@JsonValue
	public @NotNull String getValue() {
		return value;
	}

	public static final CallDataMethod<CCEffectResult> EFFECT_RESPONSE = new CallDataMethod<>("effectResponse");
}
