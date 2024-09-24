package live.crowdcontrol.cc4j;

import live.crowdcontrol.cc4j.websocket.payload.PublicEffectPayload;
import org.jetbrains.annotations.NotNull;

public interface CCTimedEffect extends CCEffect {

	/**
	 * Pauses this effect.
	 */
	void onPause(@NotNull PublicEffectPayload request, @NotNull CCPlayer source);

	/**
	 * Resumes this effect.
	 */
}
