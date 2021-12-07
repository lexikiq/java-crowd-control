package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonParseException;
import dev.qixils.crowdcontrol.RequestManager;
import dev.qixils.crowdcontrol.exceptions.ExceptionUtil;
import dev.qixils.crowdcontrol.exceptions.NoApplicableTarget;
import dev.qixils.crowdcontrol.socket.Request.Type;
import dev.qixils.crowdcontrol.socket.Response.PacketType;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Processes incoming requests from a Crowd Control socket and executes them.
 */
final class EffectExecutor {
	private static final Logger logger = Logger.getLogger("CC-EffectExecutor");
	private final @Nullable SocketThread socketThread;
	private final Socket socket;
	private final Executor effectPool;
	private final InputStreamReader input;
	private final RequestManager crowdControl;
	private final @Nullable String password;
	private boolean loggedIn = false;

	EffectExecutor(SocketThread socketThread) throws IOException {
		this.socketThread = socketThread;
		this.socket = socketThread.socket;
		this.effectPool = socketThread.socketManager.effectPool;
		this.input = new InputStreamReader(socket.getInputStream());
		this.crowdControl = socketThread.socketManager.crowdControl;
		this.password = crowdControl.getPassword();
	}

	EffectExecutor(Socket socket, Executor effectPool, RequestManager crowdControl) throws IOException {
		this.socketThread = null;
		this.socket = socket;
		this.effectPool = effectPool;
		this.input = new InputStreamReader(socket.getInputStream());
		this.crowdControl = crowdControl;
		this.password = crowdControl.getPassword();
	}

	void run() throws IOException {
		// get incoming data
		Request request;
		try {
			request = JsonObject.fromInputStream(input, Request::fromJSON);
		} catch (JsonParseException e) {
			logger.log(Level.WARNING, "Failed to parse JSON from socket", e);
			return;
		}

		if (request == null) {
			if (socketThread != null)
				socketThread.shutdown("Received a blank packet; assuming client has disconnected");
			else
				socket.close();
			return;
		}

		request.originatingSocket = socket;

		if (request.getType() == Type.KEEP_ALIVE) {
			request.buildResponse().packetType(PacketType.KEEP_ALIVE).send();
			return;
		}

		// login handling
		if (!loggedIn && password != null && socketThread != null) {
			if (request.getType() != Type.LOGIN) {
				request.buildResponse().type(ResultType.NOT_READY).message("Client has not logged in").send();
			} else if (password.equalsIgnoreCase(request.getMessage())) {
				logger.info("New client successfully logged in (" + socketThread.displayName + ")");
				DummyResponse resp = new DummyResponse();
				resp.id = request.getId();
				resp.message = "Successfully logged in";
				resp.type = PacketType.LOGIN_SUCCESS;
				resp.write(socket);
				loggedIn = true;
			} else {
				logger.info("Aborting connection due to incorrect password (" + socketThread.displayName + ")");
				socketThread.shutdown(request, "Incorrect password");
			}
			return;
		}

		// process request
		effectPool.execute(() -> {
			try {
				crowdControl.handle(request);
			} catch (Throwable exc) {
				if (ExceptionUtil.isCause(NoApplicableTarget.class, exc)) {
					request.buildResponse().type(ResultType.FAILURE).message("Streamer(s) unavailable").send();
				} else {
					logger.log(Level.WARNING, "Request handler threw an exception", exc);
					request.buildResponse().type(ResultType.FAILURE).message("Request handler threw an exception").send();
				}
			}
		});
	}
}