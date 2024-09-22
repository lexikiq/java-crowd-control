package live.crowdcontrol.cc4j.websocket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Request {
	public @NotNull String action;
	public @Nullable Object data;


	Request() {
	}

	public Request(@NotNull String action) {
		this.action = action;
	}

	public Request(@NotNull String action, @Nullable Object data) {
		this.action = action;
		this.data = data;
	}
}
