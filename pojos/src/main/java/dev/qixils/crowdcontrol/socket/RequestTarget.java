package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.qixils.crowdcontrol.util.PostProcessable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RequestTarget extends JsonHolder<RequestTarget> implements PostProcessable {

	@SuppressWarnings("unused") // used by gson
	RequestTarget(@NotNull JsonObject data) {
		super(ObjectTypes.TARGET, data);
	}

	RequestTarget(@NotNull JsonHolderBuilder<?> builder) {
		super(builder);
	}

	public static JsonHolderBuilder<RequestTarget> builder() {
		return new JsonHolderBuilder<>(ObjectTypes.TARGET);
	}

	@Override
	public void postProcess() {
		Optional<String> id = get(ID);
		Optional<String> service = get(SERVICE);
		if (!id.isPresent() || !service.isPresent()) return;

		String[] split = id.get().split("_", 2);
		if (split.length != 2 || !split[0].equalsIgnoreCase(service.get())) return;

		set(ID, split[1]);
	}

	public static <T> Field.Builder<T> buildField(@NotNull TypeToken<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.TARGET.id());
	}

	public static <T> Field.Builder<T> buildField(@NotNull Class<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.TARGET.id());
	}

	public static final Field<String> ID = buildField(String.class).addIds("id", "originID").build();

	public static final Field<String> NAME = buildField(String.class).addId("name").build();

	public static final Field<String> LOGIN = buildField(String.class).addId("login").build();

	public static final Field<String> AVATAR = buildField(String.class).addIds("avatar", "image").build();

	public static final Field<String> SERVICE = buildField(String.class).addIds("service", "profile").build();

	public static final Field<String> CC_UID = buildField(String.class).addId("ccUID").build();
}
