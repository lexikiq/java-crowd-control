package dev.qixils.crowdcontrol.fields;

import com.google.gson.reflect.TypeToken;
import dev.qixils.crowdcontrol.socket.Field;
import dev.qixils.crowdcontrol.socket.ObjectTypes;
import dev.qixils.crowdcontrol.socket.SocketManager;
import org.jetbrains.annotations.NotNull;

public class CommonFields {
	private CommonFields() {
	}

	public static <T> Field.Builder<T> packet(@NotNull TypeToken<T> type) {
		return new Field.Builder<>(type).addSupports(ObjectTypes.REQUEST.id(), ObjectTypes.RESPONSE.id());
	}

	public static <T> Field.Builder<T> packet(@NotNull Class<T> type) {
		return new Field.Builder<>(type).addSupports(ObjectTypes.REQUEST.id(), ObjectTypes.RESPONSE.id());
	}

	/**
	 * The ID of the packet. Corresponds to a unique transaction.
	 *
	 * @since 4.0.0
	 */
	public static final Field<Integer> ID = packet(Integer.class).addId("id").build();

	/**
	 * The message describing or explaining the packet.
	 *
	 * @since 4.0.0
	 */
	public static final Field<String> MESSAGE = packet(String.class).addId("message").build();

	/**
	 * The socket from which a packet was received from or will be sent to.
	 *
	 * @since 4.0.0
	 */
	public static final Field<SocketManager> SOCKET = packet(SocketManager.class).addId("socket").isTransient(true).build();

	// TODO: `LOGIN` (request, request target, request source)
}
