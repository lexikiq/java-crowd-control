package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.qixils.crowdcontrol.exceptions.ExceptionUtil;
import dev.qixils.crowdcontrol.fields.CommonFields;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.time.Duration;

public class ResponseNew extends JsonHolder<ResponseNew> {

	@SuppressWarnings("unused") // used by gson
	ResponseNew(@NotNull JsonObject data) {
		super(ObjectTypes.RESPONSE, data);
	}

	ResponseNew(@NotNull JsonHolderBuilder<?> builder) {
		super(builder);
	}

	public static <T> Field.Builder<T> buildField(@NotNull TypeToken<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.REQUEST.id());
	}

	public static <T> Field.Builder<T> buildField(@NotNull Class<T> type) {
		return new Field.Builder<>(type).addSupport(ObjectTypes.REQUEST.id());
	}

	public static JsonHolderBuilder<ResponseNew> builder() {
		return new JsonHolderBuilder<>(ObjectTypes.RESPONSE);
	}

	/**
	 * @see CommonFields#ID
	 * @since 4.0.0
	 */
	public static final Field<Integer> ID = CommonFields.ID;

	public static final Field<ResponseType> TYPE = buildField(ResponseType.class).addId("type").build();

	public static final Field<ResponseStatus> STATUS = buildField(ResponseStatus.class).addId("status").build();

	/**
	 * @see CommonFields#MESSAGE
	 * @since 4.0.0
	 */
	public static final Field<String> MESSAGE = CommonFields.MESSAGE;

	public static final Field<Duration> TIME_REMAINING = buildField(Duration.class).addId("timeRemaining").build();

	/**
	 * @see CommonFields#SOCKET
	 * @since 4.0.0
	 */
	public static final Field<SocketManager> SOCKET = CommonFields.SOCKET;

	public static final Field<IdType> ID_TYPE = buildField(IdType.class).addId("idType").build();

	// misc

	/**
	 * Creates a {@link ResponseNew} indicating that the socket connection is being terminated.
	 *
	 * @param originatingSocket socket being terminated
	 * @param message           message describing the reason for termination
	 * @return a new Response object
	 * @throws IllegalArgumentException if the socket is null
	 * @since 3.3.2
	 */
	@ApiStatus.AvailableSince("3.3.2")
	@CheckReturnValue
	@NotNull
	static ResponseNew ofDisconnectMessage(@NotNull SocketManager originatingSocket, @Nullable String message) {
		return ResponseNew.builder()
			.set(ResponseNew.SOCKET, originatingSocket)
			.set(ResponseNew.TYPE, ResponseType.DISCONNECT)
			.set(ResponseNew.MESSAGE, ExceptionUtil.validateNotNullElse(message, "Disconnected"))
			.build();
	}

	/**
	 * Creates a {@link ResponseNew} indicating that the socket connection is being terminated.
	 *
	 * @param request request which caused this response
	 * @param message message describing the reason for termination
	 * @return a new Response object
	 * @throws IllegalArgumentException if the request is null
	 * @since 3.3.2
	 */
	@ApiStatus.AvailableSince("3.3.2")
	@CheckReturnValue
	@NotNull
	static ResponseNew ofDisconnectMessage(@NotNull RequestNew request, @Nullable String message) {
		return ResponseNew.builder()
			.set(ResponseNew.SOCKET, request.getOrNull(RequestNew.SOCKET))
			.set(ResponseNew.TYPE, ResponseType.DISCONNECT)
			.set(ResponseNew.MESSAGE, ExceptionUtil.validateNotNullElse(message, "Disconnected"))
			.build();
	}

	/**
	 * Sends this {@link ResponseNew} to the client or server that delivered the related {@link RequestNew}.
	 *
	 * @throws IllegalStateException if {@link #SOCKET} is unset
	 *                               (i.e. the response was created without a {@link RequestNew})
	 * @since 4.0.0
	 * @return whether the response was successfully sent
	 *         (false if an IOException occurred, true otherwise)
	 */
	@ApiStatus.AvailableSince("3.0.0")
	public boolean send() throws IllegalStateException {
		try {
			rawSend();
			return true;
		} catch (IOException exc) {
			logger.warn("Failed to write response to socket", exc);
			return false;
		}
	}

	void rawSend() throws IllegalStateException, IOException {
		SocketManager socket = getOrNull(SOCKET);

		if (socket == null) {
			throw new IllegalStateException("Response was constructed without a Request and thus cannot find where to be sent");
		}

		if (socket.isClosed()) {
			return;
		}

		socket.write(this);
	}
}
