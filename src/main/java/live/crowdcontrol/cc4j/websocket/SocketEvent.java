package live.crowdcontrol.cc4j.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SocketEvent {
	public String domain;
	public String type;
	public JsonNode payload;
}
