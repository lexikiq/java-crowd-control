package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.util.HashMap;
import java.util.Map;

public enum ResponseType implements ByteObject {
	/**
	 * The packet is the result of executing an effect.
	 *
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	EFFECT_RESULT(false, true), // 0
	/**
	 * The packet is updating the status of effects.
	 * This should be used with an {@link Response.Builder#id(int) id} of 0.
	 *
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	EFFECT_STATUS(false, true), // 1
	/**
	 * The packet is a generic event.
	 *
	 * @since 3.6.1
	 */
	@ApiStatus.AvailableSince("3.6.1")
	GENERIC_EVENT(false, false, (byte) 0x10), // 16
	/**
	 * The packet is triggering a remote function to be run in the CS.
	 * This should be used with an {@link Response.Builder#id(int) id} of 0 and should specify the function name to execute
	 * in the {@link Response.Builder#method(String) method} field.
	 * This may optionally be used with the {@link Response.Builder#addArguments(Object...) args} field.
	 *
	 * @since 3.6.0
	 */
	@ApiStatus.AvailableSince("3.6.0")
	REMOTE_FUNCTION(false, false, (byte) 0xD0), // 208
	/**
	 * <b>Internal value</b> used to prompt a connecting client for a password.
	 *
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	LOGIN(false, false, (byte) 0xF0), // 240
	/**
	 * <b>Internal value</b> used to indicate a successful login.
	 *
	 * @since 3.1.0
	 */
	@ApiStatus.AvailableSince("3.1.0")
	@ApiStatus.Internal
	LOGIN_SUCCESS(false, false, (byte) 0xF1), // 241
	/**
	 * <b>Internal value</b> used to indicate that the socket is being disconnected.
	 *
	 * @since 3.1.0
	 */
	@ApiStatus.AvailableSince("3.1.0")
	@ApiStatus.Internal
	DISCONNECT(true, false, (byte) 0xFE), // 254
	/**
	 * <b>Internal value</b> used to reply to a keep alive packet.
	 *
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	KEEP_ALIVE(false, false, (byte) 0xFF); // 255

	private static final Map<Byte, ResponseType> BY_BYTE;

	static {
		Map<Byte, ResponseType> map = new HashMap<>(values().length);
		for (ResponseType type : values())
			map.put(type.encodedByte, type);
		BY_BYTE = map;
	}

	private final byte encodedByte;
	private final boolean isMessageRequired;
	private final boolean hasResultType;

	ResponseType(boolean isMessageRequired, boolean hasResultType, byte encodedByte) {
		this.isMessageRequired = isMessageRequired;
		this.hasResultType = hasResultType;
		this.encodedByte = encodedByte;
	}

	ResponseType(boolean isMessageRequired, boolean hasResultType) {
		this.isMessageRequired = isMessageRequired;
		this.hasResultType = hasResultType;
		this.encodedByte = (byte) ordinal();
	}

	/**
	 * Gets a packet type from its corresponding JSON encoding.
	 *
	 * @param encodedByte byte used in JSON encoding
	 * @return corresponding Type if applicable
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	@CheckReturnValue
	public static @Nullable ResponseType from(byte encodedByte) {
		return BY_BYTE.get(encodedByte);
	}

	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	@CheckReturnValue
	public byte getEncodedByte() {
		return encodedByte;
	}

	/**
	 * Determines if this packet type requires an accompanying message to be sent.
	 *
	 * @return true if a message is required
	 * @since 3.3.2
	 */
	@ApiStatus.AvailableSince("3.3.2")
	@CheckReturnValue
	public boolean isMessageRequired() {
		return isMessageRequired;
	}

	/**
	 * Determines if this packet type requires an accompanying {@link Response.ResultType} to be sent.
	 *
	 * @return true if a result type is required
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	@CheckReturnValue
	public boolean hasResultType() {
		return hasResultType;
	}
}
