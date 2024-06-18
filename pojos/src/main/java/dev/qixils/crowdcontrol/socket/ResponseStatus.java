package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.util.HashMap;
import java.util.Map;

public enum ResponseStatus implements ByteObject {
	/**
	 * The effect was applied successfully.
	 *
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	SUCCESS(false, false), // 0
	/**
	 * The effect failed to be applied. Will refund the purchaser.
	 *
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	FAILURE(true, false), // 1
	/**
	 * The requested effect is unusable and should not be requested again.
	 *
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	UNAVAILABLE(true, false), // 2
	/**
	 * The effect is momentarily unavailable but may be retried in a few seconds.
	 *
	 * @since 1.0.0
	 */
	@ApiStatus.AvailableSince("1.0.0")
	RETRY(false, false), // 3
	/**
	 * The timed effect has been paused and is now waiting.
	 *
	 * @see dev.qixils.crowdcontrol.TimedEffect
	 * @since 2.0.0
	 */
	@ApiStatus.AvailableSince("2.0.0")
	PAUSED(false, false, (byte) 0x06), // 6
	/**
	 * The timed effect has been resumed and is counting down again.
	 *
	 * @see dev.qixils.crowdcontrol.TimedEffect
	 * @since 2.0.0
	 */
	@ApiStatus.AvailableSince("2.0.0")
	RESUMED(false, false, (byte) 0x07), // 7
	/**
	 * The timed effect has finished.
	 *
	 * @see dev.qixils.crowdcontrol.TimedEffect
	 * @since 2.0.0
	 */
	@ApiStatus.AvailableSince("2.0.0")
	FINISHED(true, false, (byte) 0x08), // 8
	/**
	 * Instructs the client to display this effect in its menu.
	 * <p>
	 * This type is not intended to be used as a response for an actual effect but rather sent to the client as
	 * necessary to update the status of an effect in the menu.
	 * This must be used in combination with {@link ResponseType#EFFECT_STATUS}.
	 *
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	VISIBLE(true, true, (byte) 0x80), // 128
	/**
	 * Instructs the client to hide this effect in its menu.
	 * <p>
	 * This type is not intended to be used as a response for an actual effect but rather sent to the client as
	 * necessary to update the status of an effect in the menu.
	 * This must be used in combination with {@link ResponseType#EFFECT_STATUS}.
	 *
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	NOT_VISIBLE(true, true, (byte) 0x81), // 129
	/**
	 * Instructs the client to make this effect in its menu selectable.
	 * <p>
	 * This type is not intended to be used as a response for an actual effect but rather sent to the client as
	 * necessary to update the status of an effect in the menu.
	 * This must be used in combination with {@link ResponseType#EFFECT_STATUS}.
	 *
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	SELECTABLE(true, true, (byte) 0x82), // 130
	/**
	 * Instructs the client to make this effect in its menu unselectable.
	 * <p>
	 * This type is not intended to be used as a response for an actual effect but rather sent to the client as
	 * necessary to update the status of an effect in the menu.
	 * This must be used in combination with {@link ResponseType#EFFECT_STATUS}.
	 *
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	NOT_SELECTABLE(true, true, (byte) 0x83), // 131
	/**
	 * Indicates that this Crowd Control server is not yet accepting requests.
	 * <p>
	 * This is an internal field used to indicate that the login process with a client has
	 * not yet completed. You should instead use {@link #FAILURE} to indicate a
	 * temporary failure or {@link #UNAVAILABLE} to indicate a permanent failure.
	 *
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	NOT_READY(true, false, (byte) 0xFF); // 255

	private static final Map<Byte, ResponseStatus> BY_BYTE;

	static {
		Map<Byte, ResponseStatus> map = new HashMap<>(values().length);
		for (ResponseStatus type : values())
			map.put(type.encodedByte, type);
		BY_BYTE = map;
	}

	private final boolean terminating;
	private final byte encodedByte;
	private final boolean isStatus;

	ResponseStatus(boolean terminating, boolean isStatus, byte encodedByte) {
		this.terminating = terminating;
		this.isStatus = isStatus;
		this.encodedByte = encodedByte;
	}

	ResponseStatus(boolean terminating, boolean isStatus) {
		this.terminating = terminating;
		this.isStatus = isStatus;
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
	@javax.annotation.CheckReturnValue
	public static @Nullable ResponseStatus from(byte encodedByte) {
		return BY_BYTE.get(encodedByte);
	}

	@ApiStatus.AvailableSince("3.0.0")
	@ApiStatus.Internal
	@javax.annotation.CheckReturnValue
	public byte getEncodedByte() {
		return encodedByte;
	}

	/**
	 * Determines if this result type always marks the end to a series of {@link Response}s to a
	 * {@link Request}.
	 *
	 * @return true if this result type always marks the end of a series of {@link Response}s
	 * @since 3.3.0
	 */
	@ApiStatus.AvailableSince("3.3.0")
	@javax.annotation.CheckReturnValue
	public boolean isTerminating() {
		return terminating;
	}

	/**
	 * Determines if this result type must be used in combination with {@link ResponseType#EFFECT_STATUS}
	 * and an effect ID of 0.
	 *
	 * @return true if this result type is for an effect status packet
	 * @since 3.5.2
	 */
	@ApiStatus.AvailableSince("3.5.2")
	@CheckReturnValue
	public boolean isStatus() {
		return isStatus;
	}
}
