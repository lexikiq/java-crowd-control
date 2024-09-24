package live.crowdcontrol.cc4j.websocket.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CallData<A> {
	protected final @NotNull String type;
	protected final @NotNull UUID id;
	protected final @NotNull String method;
	protected final @NotNull List<@NotNull A> args;

	@JsonCreator
	CallData(@JsonProperty("type") @NotNull String type,
			 @JsonProperty("id") @NotNull UUID id,
			 @JsonProperty("method") @NotNull String method,
			 @JsonProperty("args") @NotNull List<A> args) {
		this.type = type;
		this.id = id;
		this.method = method;
		this.args = args;
	}

	public CallData(@NotNull CallDataMethod<A> method,
					@NotNull List<A> args) {
		this.type = "call";
		this.id = UUID.randomUUID();
		this.method = method.getValue();
		this.args = args;
	}
}
