package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionData {

	@RegExp
	public static final @NotNull String TOPIC_PATTERN = "^(?<domain>overlay|session|prv|pub|app|ext|whisper)(?:/(?<scope>\\*|ccuid-[0-7][0-9a-hjkmnp-tv-z]{25}))?/(?<target>\\*|ccuid-[0-7][0-9a-hjkmnp-tv-z]{25})$";
	private static final @NotNull Logger log = LoggerFactory.getLogger(SubscriptionData.class);

	private final @NotNull Set<@NotNull String> topics;
	private final @Nullable String token;
	private final @Nullable String key;

	@JsonCreator
	public SubscriptionData(@JsonProperty("topics") @NotNull Set<@NotNull String> topics,
							@JsonProperty("token") @Nullable String token,
							@JsonProperty("key") @Nullable String key) {
		this.topics = Collections.unmodifiableSet(new HashSet<>(topics));
		for (String topic : this.topics) {
			if (topic.matches(TOPIC_PATTERN)) continue;
			log.warn("Topic {} may be invalid; fails to match expected pattern", topic);
		}

		this.token = token;
		this.key = key;
	}

	public SubscriptionData(@NotNull Set<@NotNull String> topics,
							@Nullable String token) {
		this(topics, token, null);
	}

	public SubscriptionData(@NotNull Set<@NotNull String> topics) {
		this(topics, null, null);
	}

	/**
	 * Gets the set of topics to be subscribed to.
	 * It is immutable.
	 *
	 * @return topic list
	 */
	public @NotNull Set<@NotNull String> getTopics() {
		return topics;
	}

	/**
	 * Gets the token to authenticate with.
	 *
	 * @return JWT token
	 */
	public @Nullable String getToken() {
		return token;
	}

	/**
	 * Gets the key to authenticate with.
	 * This is given out to lesser third-party applications such as the official Overlay which lacks write permissions.
	 *
	 * @return overlay key
	 */
	public @Nullable String getKey() {
		return key;
	}
}
