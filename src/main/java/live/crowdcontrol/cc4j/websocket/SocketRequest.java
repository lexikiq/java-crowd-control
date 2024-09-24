package live.crowdcontrol.cc4j.websocket;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class SocketRequest {
	public @NotNull String action;
	public @Nullable Object data;

	SocketRequest() {
	}

	public SocketRequest(@NotNull String action) {
		this.action = action;
	}

	public SocketRequest(@NotNull String action, @Nullable Object data) {
		this.action = action;
		this.data = data;
	}
}
