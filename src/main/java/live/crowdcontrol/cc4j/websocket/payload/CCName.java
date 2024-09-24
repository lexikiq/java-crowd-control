package live.crowdcontrol.cc4j.websocket.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class CCName implements Comparable<CCName> {
	@JsonProperty("public")
	private @NotNull final String displayName;
	private @Nullable final String sort;

	@JsonCreator
	public CCName(@JsonProperty("public") @NotNull String displayName,
				  @JsonProperty("sort") @Nullable String sort) {
		this.displayName = displayName;
		this.sort = sort;
	}

	public CCName(@NotNull String displayName) {
		this(displayName, null);
	}

	/**
	 * Gets the name displayed to users.
	 *
	 * @return display name
	 */
	@JsonProperty("public")
	public @NotNull String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the name used for alphabetical sorting.
	 * If null, fall back to {@link #getDisplayName()}.
	 *
	 * @return sort name
	 */
	@JsonProperty("sort")
	public @Nullable String getSortValue() {
		return sort;
	}

	/**
	 * Computes the name used for alphabetical sorting.
	 * Returns {@link #getSortValue()} if present, else {@link #getDisplayName()}.
	 *
	 * @return computed sort name
	 */
	public @NotNull String computeSortValue() {
		if (sort != null) return sort;
		return displayName;
	}

	@Override
	public int compareTo(@NotNull CCName o) {
		return computeSortValue().compareTo(o.computeSortValue());
	}

	public static class CCNameAdapter extends StdDeserializer<CCName> {
		protected CCNameAdapter() {
			super(CCName.class);
		}

		@Override
		public CCName deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
			String displayName;
			String sortName = null;
			if (parser.isExpectedStartObjectToken()) {
				ObjectCodec codec = parser.getCodec();
				JsonNode node = codec.readTree(parser);

				displayName = node.get("public").asText();
				sortName = node.get("sort").asText(null);
			} else {
				displayName = parser.getText();
			}
			return new CCName(displayName, sortName);
		}
	}
}
