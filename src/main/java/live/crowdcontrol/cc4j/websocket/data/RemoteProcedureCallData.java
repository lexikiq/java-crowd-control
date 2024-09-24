package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class RemoteProcedureCallData {
	private final @NotNull String token;
	private final @NotNull CallData<?> call;

	@JsonCreator
	public RemoteProcedureCallData(@JsonProperty("token") @NotNull String token,
								   @JsonProperty("call") @NotNull CallData<?> call) {
		this.token = token;
		this.call = call;
	}

	/**
	 * Gets the token used to authenticate this call.
	 *
	 * @return JWT token
	 */
	public @NotNull String getToken() {
		return token;
	}

	/**
	 * Gets the data of this call.
	 *
	 * @return call data
	 */
	public @NotNull CallData<?> getCall() {
		return call;
	}
}
