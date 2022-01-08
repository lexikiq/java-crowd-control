package dev.qixils.crowdcontrol.socket;

import dev.qixils.crowdcontrol.RequestManager;
import dev.qixils.crowdcontrol.exceptions.ExceptionUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Manages the connection to Crowd Control clients.
 *
 * @since 3.0.0
 */
@ApiStatus.AvailableSince("3.0.0")
public final class ServerSocketManager implements SocketManager {
	private static final Logger logger = LoggerFactory.getLogger("CC-ServerSocket");
	final RequestManager crowdControl;
	final Executor effectPool = Executors.newCachedThreadPool();
	private final List<SocketThread> socketThreads = new ArrayList<>();
	volatile boolean running = true;
	private ServerSocket serverSocket;

	/**
	 * Creates a new server-side socket manager. This is intended only for use by the library.
	 *
	 * @param crowdControl Crowd Control instance
	 * @since 3.0.0
	 */
	@CheckReturnValue
	@ApiStatus.Internal
	@ApiStatus.AvailableSince("3.0.0")
	public ServerSocketManager(@NotNull RequestManager crowdControl) {
		this.crowdControl = ExceptionUtil.validateNotNull(crowdControl, "crowdControl");
		new Thread(this::loop, "crowd-control-socket-loop").start();
	}

	private void loop() {
		if (!running)
			return;

		try {
			serverSocket = new ServerSocket(crowdControl.getPort());
		} catch (IOException exc) {
			logger.error("Could not register port " + crowdControl.getPort() + ". This is a fatal exception; attempts to reconnect will not be made.", exc);
			return;
		}

		while (running) {
			socketThreads.removeIf(SocketThread::isSocketClosed);
			try {
				Socket clientSocket = serverSocket.accept();
				SocketThread socketThread = new SocketThread(this, clientSocket);
				if (!running) {
					socketThread.shutdown(null, "Server is shutting down");
					break;
				}
				socketThreads.add(socketThread);
				socketThread.start();
			} catch (IOException exc) {
				if (running)
					logger.warn("Failed to accept new socket connection", exc);
			}
		}
	}

	@Override
	@ApiStatus.AvailableSince("3.1.0")
	public void shutdown(@Nullable Request cause, @Nullable String reason) throws IOException {
		if (!running) return;
		running = false;
		for (SocketThread socketThread : socketThreads) {
			if (socketThread.isSocketActive())
				socketThread.shutdown(cause, reason);
		}
		if (serverSocket != null && !serverSocket.isClosed())
			serverSocket.close();
	}
}