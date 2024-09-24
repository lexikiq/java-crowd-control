package live.crowdcontrol.cc4j.util;

import live.crowdcontrol.cc4j.CCEventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class EventRecord<T> {
	private final @NotNull Instant triggeredAt;
	private final @NotNull CCEventType<T> eventType;
	private final @Nullable T eventBody;

	public EventRecord(@NotNull CCEventType<T> eventType, @Nullable T eventBody) {
		this.triggeredAt = Instant.now();
		this.eventType = eventType;
		this.eventBody = eventBody;
	}

	public static EventRecord<Void> ofVoid(@NotNull CCEventType<Void> eventType) {
		return new EventRecord<>(eventType, null);
	}

	public @NotNull Instant getTriggeredAt() {
		return triggeredAt;
	}

	public @NotNull CCEventType<T> getEventType() {
		return eventType;
	}

	/**
	 * Gets the event body.
	 * May be null for Void events.
	 *
	 * @return event body
	 */
	@Nullable
	public T getEventBody() {
		return eventBody;
	}
}
