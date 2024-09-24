package live.crowdcontrol.cc4j;

import live.crowdcontrol.cc4j.websocket.ConnectedPlayer;
import live.crowdcontrol.cc4j.websocket.SocketRequest;
import live.crowdcontrol.cc4j.websocket.data.*;
import live.crowdcontrol.cc4j.websocket.payload.PublicEffectPayload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static live.crowdcontrol.cc4j.CCEffect.EFFECT_ID_PATTERN;

public class CrowdControl {
	private static final Logger log = LoggerFactory.getLogger(CrowdControl.class);
	protected final Map<String, CCEffect> effects = new HashMap<>();
	protected final Map<UUID, ConnectedPlayer> players = new HashMap<>();
	protected final Path dataFolder;

	public CrowdControl(@NotNull Path dataFolder) {
		this.dataFolder = dataFolder;
	}

	@Nullable
	public CCPlayer getPlayer(@NotNull UUID playerID) {
		ConnectedPlayer existing = players.get(playerID);
		if (existing == null) return null;
		if (!existing.isOpen()) {
			players.remove(playerID);
			return null;
		}
		return existing;
	}

	@NotNull
	public CCPlayer addPlayer(@NotNull UUID playerID) {
		CCPlayer existing = getPlayer(playerID);
		if (existing != null) {
			log.warn("Asked to add player {} with existing connection", playerID);
			return existing;
		}
		ConnectedPlayer player = new ConnectedPlayer(playerID, dataFolder);
		player.connect();
		players.put(playerID, player);
		return player;
	}

	public boolean addEffect(@NotNull CCEffect effect) {
		String effectID = effect.effectID();
		if (!effectID.matches(EFFECT_ID_PATTERN)) {
			log.error("Effect ID {} should match pattern {}", effectID, EFFECT_ID_PATTERN);
			return false;
		}
		if (effects.containsKey(effectID)) {
			log.error("Effect ID {} is already registered", effectID);
			return false;
		}
		effects.put(effectID, effect);
		return true;
	}

	@Nullable
	public CCEffectResult executeEffect(@NotNull PublicEffectPayload payload, @NotNull ConnectedPlayer source) {
		String effectID = payload.getEffect().getEffectID();
		CCEffect effect = effects.get(effectID);
		if (effect == null) {
			log.error("Cannot execute unknown effect {}", effectID);
			return new CCInstantEffectResult(
				payload.getRequestID(),
				ResultStatus.FAIL_PERMANENT,
				"Unknown Effect"
			);
		}
		try {
			return effect.onTriggerEffect(payload, source);
		} catch (Exception e) {
			log.error("Failed to invoke effect {}", effectID, e);
			return new CCInstantEffectResult(
				payload.getRequestID(),
				ResultStatus.FAIL_TEMPORARY,
				"Effect experienced an unknown error"
			);
		}
	}

	public boolean sendResult(@NotNull UUID playerId, @NotNull CCEffectResult result) {
		ConnectedPlayer player = players.get(playerId);
		if (player == null) return false;
		if (!player.isOpen()) return false;
		String token = player.getToken();
		if (token == null) return false;

		player.send(new SocketRequest(
			"rpc",
			new RemoteProcedureCallData(
				token,
				new CallData<>(
					CallDataMethod.EFFECT_RESPONSE,
					Collections.singletonList(result)
				)
			)
		));
		return true;
	}
}
