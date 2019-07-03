package fr.ankeraout.mcank;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the main part of the server code which is the server loop
 * that is in charge of accepting incoming client connections. It is a
 * singleton, which means that it is accessible from anywhere in the code, but
 * also that there can only be one instance of this class at a given time.
 * 
 * @author Ankeraout
 */
public final class ClassicubeServer {
	/**
	 * The name of the logger. This value is passed as a parameter when calling
	 * {@link Logger#getLogger(String)}.
	 */
	public static final String LOGGER_NAME = "ClassicubeServer";

	/**
	 * The only instance of this class.
	 */
	private static ClassicubeServer instance;

	/**
	 * The current state of the server.
	 */
	private ClassicubeServerState state;

	/***
	 * The server socket used for listening to incoming client connections.
	 */
	private ServerSocket socket;

	/**
	 * The lock for the server state.
	 */
	private Object stateLock;

	/**
	 * The thread that is listening for incoming connections.
	 */
	private Thread listenThread;

	/**
	 * The server properties. This is basically a dictionary that contains all of
	 * the values of the server properties file. All the values in
	 * assets/server-default.properties are always guaranteed to be defined.
	 */
	private ClassicubeServerProperties properties;

	/**
	 * The private constructor of the singleton.
	 */
	private ClassicubeServer() {
		// Initialize locks
		this.stateLock = new Object();

		// Initialize the server state
		this.state = ClassicubeServerState.STOPPED;

		// Initialize the server data structures
		this.properties = new ClassicubeServerProperties();
	}

	/**
	 * Returns the instance of this class. If this class was not instantiated
	 * before, a new instance is created.
	 * 
	 * @return The instance of {@link ClassicubeServer}
	 */
	public static synchronized ClassicubeServer getInstance() {
		if(ClassicubeServer.instance == null) {
			ClassicubeServer.instance = new ClassicubeServer();
		}

		return ClassicubeServer.instance;
	}

	/**
	 * Starts the server.
	 * 
	 * @throws IOException      If the server socket could not be created
	 * @throws RuntimeException If the server is not in the
	 *                          {@link ClassicubeServerState#STOPPED} state.
	 */
	public void start() throws IOException, RuntimeException {
		// The state of the server before starting it
		ClassicubeServerState oldState = null;

		// Set the server state to STARTING
		synchronized(this.stateLock) {
			// Save the current server state
			oldState = this.state;

			// Reject the start request if the server is not STOPPED
			if(!this.state.isStartCallAllowed()) {
				throw new RuntimeException("The current server state does not allow starting it.");
			}

			this.state = ClassicubeServerState.STARTING;
		}

		// Bind the server socket
		try {
			this.socket = new ServerSocket(this.properties.getPort(), this.properties.getBacklog(), InetAddress.getByName(this.properties.getIP()));
		} catch(IOException e) {
			// Reset the server state
			synchronized(this.stateLock) {
				this.state = oldState;
			}

			// Propagate the exception
			throw e;
		}

		// Create the listener thread
		this.listenThread = new Thread(new ListenThreadRunnable());

		// Start the listener thread
		this.listenThread.start();

		// Set the server state to STARTED
		synchronized(this.stateLock) {
			this.state = ClassicubeServerState.STARTED;
		}
	}

	/**
	 * Stops the server.
	 * 
	 * @throws RuntimeException If the server is not in the
	 *                          {@link ClassicubeServerState#STARTED} state.
	 */
	public void stop() throws IOException, RuntimeException {
		// The state of the server before stopping it
		ClassicubeServerState oldState = null;

		// Set the server state to STOPPING
		synchronized(this.stateLock) {
			// Save the current server state
			oldState = this.state;

			// Reject the stop request if the server is not STARTED
			if(!this.state.isStopCallAllowed()) {
				throw new RuntimeException("The current server state does not allow stopping it.");
			}

			this.state = ClassicubeServerState.STOPPING;
		}

		// Close the listener socket. This will cause the listener thread to exit as a
		// side effect.
		try {
			this.socket.close();
		} catch(IOException e) {
			// Reset the server state
			synchronized(this.stateLock) {
				this.state = oldState;
			}

			// Propagate the exception
			throw e;
		}

		// Set the server state to STOPPED
		synchronized(this.stateLock) {
			this.state = ClassicubeServerState.STOPPED;
		}
	}

	/**
	 * Returns the server properties object.
	 * 
	 * @return The server properties object
	 */
	public ClassicubeServerProperties getProperties() {
		return this.properties;
	}

	/**
	 * This class contains the code for the listen thread of the server.
	 * 
	 * @author Ankeraout
	 *
	 */
	private class ListenThreadRunnable implements Runnable {
		/**
		 * This method contains the code of the server listen thread. It is thus charged
		 * of listening for incoming client connections, and accepting them.
		 */
		public void run() {
			while(true) {
				try {
					Socket clientSocket = ClassicubeServer.this.socket.accept();

					Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO,
							clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort()
									+ " is connecting...");
				} catch(IOException e) {
					synchronized(ClassicubeServer.this.stateLock) {
						// Check if the exception occurred when the server was stopping. If so, the
						// exception was caused by the call to ClassicubeServer.stop(), which is
						// expected. We do not have to log a message in this situation. Otherwise, the
						// exception testifies of another important problem. We cannot do anything from
						// there, so we just put a log message and exit the thread.
						if(ClassicubeServer.this.state != ClassicubeServerState.STOPPING) {
							Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.SEVERE,
									"Listen thread encountered an exception. The thread is no longer running, and new incoming client connections cannot be accepted.",
									e);
						}
					}
				}
			}
		}
	}

	/**
	 * This enum represents the current state of the server at a given time.
	 * 
	 * @author Ankeraout
	 *
	 */
	private enum ClassicubeServerState {
		/**
		 * This value means that the server is currently stopped. It could be in this
		 * state for 2 reasons:
		 * <ul>
		 * <li>The server was not started before</li>
		 * <li>The server was previously stopped</li>
		 * </ul>
		 */
		STOPPED(true, false),

		/**
		 * This value means that the server is currently starting.
		 */
		STARTING(false, false),

		/**
		 * This value means that the server is currently running, listening for incoming
		 * connections and exchanging data with connected clients. incoming connections.
		 */
		STARTED(false, true),

		/**
		 * This value means that the server is currently stopping.
		 */
		STOPPING(false, false);

		/**
		 * This value determines whether the server can be started when it is in this
		 * state.
		 */
		private boolean canStart;

		/**
		 * This value determines whether the server can be stopped when it is in this
		 * state.
		 */
		private boolean canStop;

		/**
		 * Creates a new {@link ClassicubeServerState} value.
		 * 
		 * @param canStart A value that determines whether the
		 *                 {@link ClassicubeServer#start()} method can be called when
		 *                 the server is in this state.
		 * @param canStop  A value that determines whether the
		 *                 {@link ClassicubeServer#stop()} method can be called when the
		 *                 server is in this state
		 */
		private ClassicubeServerState(boolean canStart, boolean canStop) {
			this.canStart = canStart;
			this.canStop = canStop;
		}

		/**
		 * Returns a boolean value that determines whether the
		 * {@link ClassicubeServer#start()} method can be called when the server is in
		 * this state.
		 * 
		 * @return A boolean value that determines whether the
		 *         {@link ClassicubeServer#start()} method can be called when the server
		 *         is in this state.
		 */
		public boolean isStartCallAllowed() {
			return this.canStart;
		}

		/**
		 * Returns a boolean value that determines whether the
		 * {@link ClassicubeServer#stop()} method can be called when the server is in
		 * this state.
		 * 
		 * @return A boolean value that determines whether the
		 *         {@link ClassicubeServer#stop()} method can be called when the server
		 *         is in this state.
		 */
		public boolean isStopCallAllowed() {
			return this.canStop;
		}
	}
}
