package dev.qixils.crowdcontrol.exceptions;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * An exception indicating that a software utilizing the Crowd Control receiver library was
 * unable to map the provided
 * {@link dev.qixils.crowdcontrol.socket.Request.Target target(s)} while attempting to execute an
 * effect and that the library should
 * {@link dev.qixils.crowdcontrol.socket.Response.ResultType#RETRY retry} the effect after a short
 * time.
 *
 * @since 3.0.0
 */
@ApiStatus.AvailableSince("3.0.0")
@Deprecated
public final class NoApplicableTarget extends CrowdControlException {

	/**
	 * Constructs a new exception with {@code null} as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	public NoApplicableTarget() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.  The
	 * cause is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	public NoApplicableTarget(@Nullable String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail
	 * message of {@code (cause==null ? null : cause.toString())} (which
	 * typically contains the class and detail message of {@code cause}).
	 * This constructor is useful for exceptions that are little more than
	 * wrappers for other throwables (for example, {@link
	 * java.security.PrivilegedActionException}).
	 *
	 * @param cause the cause (which is saved for later retrieval by the
	 *              {@link #getCause()} method).  (A {@code null} value is
	 *              permitted, and indicates that the cause is nonexistent or
	 *              unknown.)
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	public NoApplicableTarget(@Nullable Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and
	 * cause.  <p>Note that the detail message associated with
	 * {@code cause} is <i>not</i> automatically incorporated in
	 * this exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method).  (A {@code null} value is
	 *                permitted, and indicates that the cause is nonexistent or
	 *                unknown.)
	 * @since 3.0.0
	 */
	@ApiStatus.AvailableSince("3.0.0")
	public NoApplicableTarget(@Nullable String message, @Nullable Throwable cause) {
		super(message, cause);
	}
}
