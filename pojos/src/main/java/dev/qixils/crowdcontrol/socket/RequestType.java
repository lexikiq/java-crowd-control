package dev.qixils.crowdcontrol.socket;

import dev.qixils.crowdcontrol.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.util.HashMap;
import java.util.Map;

public enum RequestType implements ByteObject {
	/**
	 * Indicates that you should simulate the starting of an effect (i.e. test if it's available)
	 * but should not actually start the effect.
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	TEST(TriState.TRUE), // 0
	/**
	 * Indicates that you should start an effect, if available.
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	START(TriState.TRUE), // 1
	/**
	 * Indicates that you should stop an effect.
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	STOP(TriState.TRUE), // 2
	/**
	 * Indicates the result of a {@link Response.PacketType#REMOTE_FUNCTION}.
	 * @since 3.6.0
	 */
	@ApiStatus.AvailableSince("3.6.0")
	REMOTE_FUNCTION_RESULT(TriState.UNKNOWN, (byte) 0xD0), // 208
	/**
	 * Identifies the connected player.
	 * @since 3.5.3
	 */
	@ApiStatus.AvailableSince("3.5.3")
	PLAYER_INFO(TriState.UNKNOWN, (byte) 0xE0), // 224
	/**
	 * Indicates that a streamer is attempting to log in to the Crowd Control server.
	 * <p>
	 * This value is only used internally by the library. You will not encounter this value
	 * and should assume it does not exist.
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	LOGIN(TriState.UNKNOWN, (byte) 0xF0), // 240
	/**
	 * This packet's sole purpose is to establish that the connection with the
	 * Crowd Control server has not been dropped.
	 * <p>
	 * This value is only used internally by the library. You will not encounter this value
	 * and should assume it does not exist.
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	KEEP_ALIVE(TriState.FALSE, (byte) 0xFF); // 255

	private static final @NotNull Map<Byte, RequestType> BY_BYTE;

	static {
		Map<Byte, RequestType> map = new HashMap<>(values().length);
		for (RequestType type : values())
			map.put(type.encodedByte, type);
		BY_BYTE = map;
	}

	private final @NotNull TriState isStandard;
	private final byte encodedByte;

	RequestType(@NotNull TriState isStandard, byte encodedByte) {
		this.isStandard = isStandard;
		this.encodedByte = encodedByte;
	}

	RequestType(@NotNull TriState isStandard) {
		this.isStandard = isStandard;
		this.encodedByte = (byte) ordinal();
	}

	/**
	 * Gets a packet type from its corresponding JSON encoding.
	 *
	 * @param encodedByte byte used in JSON encoding
	 * @return corresponding Type if applicable
	 * @since 3.0.0
	 */
	@ApiStatus.Internal
	@ApiStatus.AvailableSince("3.0.0")
	@CheckReturnValue
	public static @Nullable RequestType from(byte encodedByte) {
		return BY_BYTE.get(encodedByte);
	}

	@ApiStatus.Internal
	@ApiStatus.AvailableSince("3.0.0")
	@CheckReturnValue
	public byte getEncodedByte() {
		return encodedByte;
	}

	/**
	 * Determines if this packet represents a standard effect request.
	 *
	 * @return if this packet represents a standard effect request
	 * @since 3.3.0
	 */
	@ApiStatus.AvailableSince("3.3.0")
	public boolean isEffectType() {
		return isStandard == TriState.TRUE;
	}

	/**
	 * Determines if this packet is expected to use incremental IDs.
	 *
	 * @return if this packet is expected to use incremental IDs
	 * @since 3.5.3
	 */
	@ApiStatus.AvailableSince("3.5.3")
	@NotNull
	public TriState usesIncrementalIds() {
		return isStandard;
	}
}
