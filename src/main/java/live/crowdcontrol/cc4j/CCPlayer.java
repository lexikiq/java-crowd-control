package live.crowdcontrol.cc4j;

import live.crowdcontrol.cc4j.util.EventManager;
import live.crowdcontrol.cc4j.websocket.UserToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CCPlayer {

	/**
	 * Gets the unique ID that represents an in-game player.
	 *
	 * @return unique ID
	 */
	@NotNull
	UUID getUuid();

	/**
	 * Gets the ID of this connection.
	 * Used primarily for authentication.
	 *
	 * @return connection ID
	 */
	@Nullable
	String getConnectionID();

	/**
	 * Gets the user's token used for making authenticated requests.
	 *
	 * @return auth token
	 */
	@Nullable
	String getToken();

	/**
	 * Gets the user's profile as decoded from their auth token.
	 *
	 * @return user profile
	 */
	@Nullable
	UserToken getUserToken();

	/**
	 * Gets the manager which handles distributing events.
	 */
	@NotNull
	EventManager getEventManager();
}
