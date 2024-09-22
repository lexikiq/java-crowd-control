package live.crowdcontrol.cc4j.websocket;

import java.util.List;

public class UserToken {
	public String type;
	public String jti;
	public String ccUID;
	public String originID;
	public String profileType;
	public String name;
	public List<String> roles;
	public int exp;
	public String ver;
}
