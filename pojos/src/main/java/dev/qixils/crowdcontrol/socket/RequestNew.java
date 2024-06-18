package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.qixils.crowdcontrol.fields.CommonFields;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class RequestNew extends JsonHolder<RequestNew> {

	@SuppressWarnings("unused") // used by gson
	RequestNew(@NotNull JsonObject data) {
		super(ObjectTypes.REQUEST, data);
	}

	RequestNew(@NotNull JsonHolderBuilder<?> builder) {
		super(builder);
	}

	public static <T> Field.Builder<T> buildField(@NotNull TypeToken<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.REQUEST.id());
	}

	public static <T> Field.Builder<T> buildField(@NotNull Class<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.REQUEST.id());
	}

	public static JsonHolderBuilder<RequestNew> builder() {
		return new JsonHolderBuilder<>(ObjectTypes.REQUEST);
	}

	/**
	 * @see CommonFields#ID
	 * @since 4.0.0
	 */
	public static final Field<Integer> ID = CommonFields.ID;

	public static final Field<RequestType> TYPE = buildField(RequestType.class).addId("type").build();

	public static final Field<String> EFFECT = buildField(String.class).addId("code").build();

	/**
	 * @see CommonFields#MESSAGE
	 * @since 4.0.0
	 */
	public static final Field<String> MESSAGE = CommonFields.MESSAGE;

	/**
	 * The viewer(s) who triggered the effect.
	 *
	 * @since 4.0.0
	 */
	public static final Field<RequestTarget[]> VIEWERS = buildField(RequestTarget[].class).addId("viewers").build();

	public static final Field<Integer> COST = buildField(Integer.class).addId("cost").build();

	public static final Field<RequestTarget[]> TARGETS = buildField(RequestTarget[].class).addId("targets").build();

	public static final Field<Duration> DURATION = buildField(Duration.class).addId("duration").build();

	public static final Field<JsonElement> VALUE = buildField(JsonElement.class).addId("value").build();

	public static final Field<Integer> QUANTITY = buildField(Integer.class).addId("quantity").build();

	public static final Field<String> LOGIN = buildField(String.class).addId("login").build();

	public static final Field<String> PASSWORD = buildField(String.class).addId("password").build();

	public static final Field<RequestTarget> PLAYER = buildField(RequestTarget.class).addId("player").build();

	/**
	 * @see CommonFields#SOCKET
	 * @since 4.0.0
	 */
	public static final Field<SocketManager> SOCKET = CommonFields.SOCKET;

	public static final Field<RequestSource> SOURCE = buildField(RequestSource.class).addId("source").isTransient(true).build();

	// misc

	public JsonHolderBuilder<ResponseNew> buildResponse() {
		return ResponseNew.builder()
			.set(ID, getOrNull(ID))
			.set(SOCKET, getOrNull(SOCKET));
		// TODO
		//  if (request.getEffect() != null)
		//	    this.ids.add(request.getEffect());
	}
}
