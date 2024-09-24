package live.crowdcontrol.cc4j;

import live.crowdcontrol.cc4j.websocket.data.CCEffectResult;
import live.crowdcontrol.cc4j.websocket.payload.PublicEffectPayload;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CCEffect {

	@RegExp
	String EFFECT_ID_PATTERN = "^[a-zA-Z_][a-zA-Z0-9_]*$";

	/**
	 * Gets the ID of the effect.
	 * Used to determine which effect object to execute.
	 *
	 * @return effect ID
	 */
	@Pattern(EFFECT_ID_PATTERN)
	String effectID();

	// TODO: reference const in javadoc
	/**
	 * Triggers this effect.
	 * <p>
	 * May return null if you'd prefer to send a response {@link CrowdControl#sendResult(UUID, CCEffectResult) manually},
	 * but please note that if you fail to emit a terminating response within 60 seconds
	 * then a failure response will be produced for you.
	 *
	 * @param request the request responsible for invoking this effect
	 * @param source the player whose connection originated this request
	 * @return the result, if available
	 */
	@Nullable
	CCEffectResult onTriggerEffect(@NotNull PublicEffectPayload request, @NotNull CCPlayer source);
}
