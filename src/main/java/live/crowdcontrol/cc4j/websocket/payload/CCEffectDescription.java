package live.crowdcontrol.cc4j.websocket.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CCEffectDescription {
	private @NotNull String effectID;
	private @NotNull String type;
	private @NotNull CCName name;
	private @NotNull String image;
	private @Nullable String note;
	private @Nullable String description;
	private @Nullable List<String> tags;
	private boolean disabled;
	@JsonProperty("new")
	private boolean isNew;
	private boolean inactive;
	private boolean admin;
	private boolean hidden;
	private boolean unavailable;
	private @Nullable List<String> category;
	private @Nullable List<String> group;
	private int duration;
	// TODO tiktok
	// TODO sessionCooldown
	// TODO userCooldown
	// TODO scale

	// boring getters

	public @NotNull String getEffectID() {
		return effectID;
	}

	public @NotNull String getType() {
		return type;
	}

	public @NotNull CCName getName() {
		return name;
	}

	public @NotNull String getImage() {
		return image;
	}

	public @Nullable String getNote() {
		return note;
	}

	public @Nullable String getDescription() {
		return description;
	}

	public @Nullable List<String> getTags() {
		return tags;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public boolean isNew() {
		return isNew;
	}

	public boolean isInactive() {
		return inactive;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isUnavailable() {
		return unavailable;
	}

	public @Nullable List<String> getCategory() {
		return category;
	}

	public @Nullable List<String> getGroup() {
		return group;
	}

	public int getDuration() {
		return duration;
	}
}
