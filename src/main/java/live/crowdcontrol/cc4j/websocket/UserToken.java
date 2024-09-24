package live.crowdcontrol.cc4j.websocket;

import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;

import java.util.List;

public class UserToken {
	@RegExp
	public static final String CCUID_PATTERN = "^ccuid-[0-7][0-9a-hjkmnp-tv-z]{25}$";

	public String type;
	public String jti;
	@Pattern(CCUID_PATTERN)
	public String ccUID;
	public String originID;
	public String profileType;
	public String name;
	public List<String> roles;
	public int exp;
	public String ver;
}
