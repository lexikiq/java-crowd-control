package live.crowdcontrol.cc4j.websocket.payload;

import org.jetbrains.annotations.NotNull;

public class CCSanitizedUserRecord {
	private @NotNull String ccUID;
	private @NotNull String image;
	private @NotNull String name;
	private @NotNull String profile; // ProfileType: 'twitch' | 'tiktok' | 'youtube' | 'discord' | 'tiktok-gifter' | 'pulsoid'
	private @NotNull String originID;
}
