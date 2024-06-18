package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class RequestSource extends JsonHolder<RequestSource> {

	@SuppressWarnings("unused") // used by gson
	RequestSource(@NotNull JsonObject data) {
		super(ObjectTypes.SOURCE, data);
	}

	RequestSource(@NotNull JsonHolderBuilder<?> builder) {
		super(builder);
	}

	public static JsonHolderBuilder<RequestSource> builder() {
		return new JsonHolderBuilder<>(ObjectTypes.SOURCE);
	}

	// TODO: kinda misusing JsonHolder by having these all transient lol, miiight want to draft up another layer of abstraction
	public static <T> Field.Builder<T> buildField(@NotNull TypeToken<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.SOURCE.id()).isTransient(true);
	}

	public static <T> Field.Builder<T> buildField(@NotNull Class<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.SOURCE.id()).isTransient(true);
	}

	public static final Field<RequestTarget> TARGET = buildField(RequestTarget.class).addId("target").build();

	public static final Field<InetAddress> IP = buildField(InetAddress.class).addId("ip").build();

	public static final Field<String> LOGIN = buildField(String.class).addId("login").build();
}
