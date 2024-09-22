package live.crowdcontrol.cc4j;

import live.crowdcontrol.cc4j.websocket.ConnectedPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

@ApiStatus.Internal
public interface EventListener<T> {

	/**
	 * The domain to listen for events on.
	 * Can be {@code *} for all (subscribed) domains.
	 *
	 * @return domain type
	 */
	@NotNull
	String domain();

	/**
	 * The type of event to listen for.
	 *
	 * @return event type
	 */
	@NotNull
	String type();

	/**
	 * The class to deserialize the data into.
	 *
	 * @return data class
	 */
	@NotNull
	Class<T> dataClass();

	/**
	 * Handles the specified event.
	 *
	 * @param event the event data
	 */
	void handle(@NotNull ConnectedPlayer player, @UnknownNullability T event);
}
