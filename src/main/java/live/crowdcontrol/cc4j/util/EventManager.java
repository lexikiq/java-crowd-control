package live.crowdcontrol.cc4j.util;

import live.crowdcontrol.cc4j.CCEventType;
import live.crowdcontrol.cc4j.CrowdControl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EventManager {
	private static final Logger log = LoggerFactory.getLogger(EventManager.class);
	public static int CATCH_UP_DEFAULT = -1;
	public static int RECORD_LIMIT = 100;
	private final List<EventRecord<?>> records = new ArrayList<>(RECORD_LIMIT);
	private final Map<CCEventType<?>, List<Consumer<?>>> listeners = new HashMap<>();
	private final CrowdControl parent;

	public EventManager(CrowdControl parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	protected <T> Stream<EventRecord<T>> getRecords(CCEventType<T> event, int catchUpPeriod) {
		if (catchUpPeriod == 0) return Stream.empty();
		return records.stream()
			.flatMap(record -> event.equals(record.getEventType()) ? Stream.of((EventRecord<T>) record) : Stream.empty())
			.filter(record -> catchUpPeriod == -1 || !Instant.now().minusSeconds(catchUpPeriod).isBefore(record.getTriggeredAt()));
	}

	protected <T> void invoke(EventRecord<T> record, Consumer<T> listener) {
		parent.getEventPool().submit(() -> {
			try {
				listener.accept(record.getEventBody());
			} catch (Exception e) {
				log.error("Failed to dispatch event {} to listener {}", record.getEventType(), listener.getClass().getSimpleName());
			}
		});
	}

	/**
	 * Dispatches an event to its listeners and stores it in a temporary log.
	 *
	 * @param event event type
	 * @param body body to pass onto listeners
	 */
	@SuppressWarnings("unchecked")
	public <T> void dispatch(@NotNull CCEventType<T> event, T body) {
		EventRecord<T> record = new EventRecord<>(event, body);

		List<Consumer<T>> eventListeners = (List<Consumer<T>>) (Object) listeners.get(event);
		if (eventListeners != null) {
			for (Consumer<T> listener : eventListeners) {
				invoke(record, listener);
			}
		}

		while (records.size() >= RECORD_LIMIT)
			records.remove(0);

		records.add(record);
	}

	/**
	 * Dispatches a Void event to its listeners and stores it in a temporary log.
	 *
	 * @param event event type
	 */
	public void dispatch(@NotNull CCEventType<Void> event) {
		dispatch(event, null);
	}

	/**
	 * Registers a listener to be called as appropriate.
	 * <p>
	 * {@code catchUpPeriod} determines the time in seconds in which to look for recently sent events
	 * to catch the listener up on events it just missed.
	 * Set to 0 to disable, or -1 to fetch all records (within the {@value RECORD_LIMIT} most recent).
	 *
	 * @param event the type of event to register for
	 * @param listener the function to call as necessary
	 * @param catchUpPeriod duration in seconds or -1
	 */
	public <T> void registerEventSupplier(CCEventType<T> event, Consumer<T> listener, int catchUpPeriod) {
		listeners.computeIfAbsent(event, $ -> new ArrayList<>()).add(listener);
		getRecords(event, catchUpPeriod).forEachOrdered(record -> invoke(record, listener));
	}

	/**
	 * Registers a listener to be called as appropriate.
	 * <p>
	 * If this event has recently been dispatched, then your listener will immediately be invoked.
	 * See {@link #registerEventSupplier(CCEventType, Consumer, int)} for more information on the catch-up period.
	 *
	 * @param event the type of event to register for
	 * @param listener the function to call as necessary
	 */
	public <T> void registerEventSupplier(CCEventType<T> event, Consumer<T> listener) {
		registerEventSupplier(event, listener, CATCH_UP_DEFAULT);
	}

	/**
	 * Registers a listener to be called as appropriate.
	 * <p>
	 * {@code catchUpPeriod} determines the time in seconds in which to look for recently sent events
	 * to catch the listener up on events it just missed.
	 * Set to 0 to disable, or -1 to fetch all records (within the {@value RECORD_LIMIT} most recent).
	 *
	 * @param event the type of event to register for
	 * @param listener the function to call as necessary
	 * @param catchUpPeriod duration in seconds or -1
	 */
	public void registerEventRunnable(CCEventType<?> event, Runnable listener, int catchUpPeriod) {
		registerEventSupplier(event, $ -> listener.run(), catchUpPeriod);
	}

	/**
	 * Registers a listener to be called as appropriate.
	 * <p>
	 * If this event has recently been dispatched, then your listener will immediately be invoked.
	 * See {@link #registerEventRunnable(CCEventType, Runnable, int)} for more information on the catch-up period.
	 *
	 * @param event the type of event to register for
	 * @param listener the function to call as necessary
	 */
	public void registerEventRunnable(CCEventType<?> event, Runnable listener) {
		registerEventRunnable(event, listener, CATCH_UP_DEFAULT);
	}
}
