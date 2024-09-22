package live.crowdcontrol.cc4j.websocket;

import com.google.gson.JsonElement;

public class Event {
	public String domain;
	public String type;
	public JsonElement payload;
}
