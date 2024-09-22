package live.crowdcontrol.cc4j.websocket.listeners;

import live.crowdcontrol.cc4j.EventListener;
import live.crowdcontrol.cc4j.websocket.ConnectedPlayer;
import live.crowdcontrol.cc4j.websocket.payload.WhoAmIPayload;
import org.jetbrains.annotations.NotNull;

public class WhoAmIListener implements EventListener<WhoAmIPayload> {

	@Override
	public @NotNull String domain() {
		return "direct";
	}

	@Override
	public @NotNull String type() {
		return "whoami";
	}

	@Override
	public @NotNull Class<WhoAmIPayload> dataClass() {
		return WhoAmIPayload.class;
	}

	@Override
	public void handle(@NotNull ConnectedPlayer player, @NotNull WhoAmIPayload event) {

		// TODO: emit event of some sort?
	}
}
