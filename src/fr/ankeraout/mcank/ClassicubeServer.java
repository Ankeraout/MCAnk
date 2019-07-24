package fr.ankeraout.mcank;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.ankeraout.mcank.util.StringUtils;
import fr.ankeraout.mcank.world.World;
import fr.ankeraout.mcank.world.WorldLoaderFactory;
import fr.ankeraout.mcank.worldgen.WorldGeneratorFactory;

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
	 * The current version of the game protocol.
	 */
	public static final int PROTOCOL_VERSION = 0x07;

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
	 * The salt of the server. This value is strictly confidential and should not be
	 * accessible from anywhere in the code, to prevent any plugin from stealing
	 * this value and using it maliciously. For security reasons, it is regenerated
	 * on every server start, and not kept anywhere on the disk.
	 */
	private String salt;

	/**
	 * The list of all the worlds of the server.
	 */
	private HashMap<String, World> worlds;

	/**
	 * The list of all the ranks of the server.
	 */
	private HashMap<String, Rank> ranks;

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
		this.worlds = new HashMap<String, World>();
		this.ranks = new HashMap<String, Rank>();

		// The salt is not generated yet.
		this.salt = null;
	}

	/**
	 * Returns the instance of this class. If this class was not instantiated
	 * before, a new instance is created.
	 * 
	 * @return The instance of {@link ClassicubeServer}
	 */
	public static synchronized ClassicubeServer getInstance() {
		if (ClassicubeServer.instance == null) {
			ClassicubeServer.instance = new ClassicubeServer();
		}

		return ClassicubeServer.instance;
	}

	/**
	 * Generates a new salt for the server.
	 */
	private void generateSalt() {
		StringBuilder stringBuilder = new StringBuilder();
		Random random = new SecureRandom();

		for (int i = 0; i < 16; i++) {
			int randomValue = random.nextInt(62);

			if (randomValue < 10) {
				stringBuilder.append((char) ('0' + randomValue));
			} else if (randomValue < 36) {
				stringBuilder.append((char) ('a' + randomValue - 10));
			} else {
				stringBuilder.append((char) ('A' + randomValue - 36));
			}
		}

		this.salt = stringBuilder.toString();
	}

	/**
	 * Starts the server.
	 * 
	 * @throws IOException      If the server socket could not be created
	 * @throws RuntimeException If the server is not in the
	 *                          {@link ClassicubeServerState#STOPPED} state or if an
	 *                          error occurs while starting it.
	 */
	public void start() throws IOException, RuntimeException {
		// The state of the server before starting it
		ClassicubeServerState oldState = null;

		// Set the server state to STARTING
		synchronized (this.stateLock) {
			// Save the current server state
			oldState = this.state;

			// Reject the start request if the server is not STOPPED
			if (!this.state.isStartCallAllowed()) {
				throw new RuntimeException("The current server state does not allow starting it.");
			}

			this.state = ClassicubeServerState.STARTING;
		}

		// Detect worlds
		try {
			this.loadWorlds();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load worlds.", e);
		}

		// If the main world does not exist, generate a 128^3 flatgrass map.
		if (!this.worlds.containsKey(this.properties.getDefaultWorld())) {
			Random random = new Random();
			this.worlds.put(this.properties.getDefaultWorld(), new World(this.properties.getDefaultWorld(), 128, 128,
					128, WorldGeneratorFactory.getInstance().getGenerator("flatgrass"), random.nextLong()));
		}

		// Load main world
		if (!this.getWorldByName(this.properties.getDefaultWorld()).isLoaded()) {
			this.getWorldByName(this.properties.getDefaultWorld()).load();
		}

		// TODO: Detect and load ranks
		try {
			this.loadRanks();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load ranks.", e);
		}

		// If the default rank does not exist, generate the default rank with permission
		// level 0.
		if (!this.ranks.containsKey(this.properties.getDefaultRank())) {
			this.ranks.put(this.properties.getDefaultRank(), new Rank(this.properties.getDefaultRank(), '7', 0, false));
		}

		// Generate the server salt
		this.generateSalt();

		Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO, "Server salt: " + this.salt + ".");

		// Bind the server socket
		try {
			this.socket = new ServerSocket(this.properties.getPort(), this.properties.getBacklog(),
					InetAddress.getByName(this.properties.getIP()));
		} catch (IOException e) {
			// Reset the server state
			synchronized (this.stateLock) {
				this.state = oldState;
			}

			// Propagate the exception
			throw e;
		}

		// Create the listener thread
		this.listenThread = new Thread(() -> listenThreadMain());

		// Start the listener thread
		this.listenThread.start();

		// Set the server state to STARTED
		synchronized (this.stateLock) {
			this.state = ClassicubeServerState.STARTED;
		}
	}

	/**
	 * Detects and loads the worlds. If the main world does not exist, then this
	 * method will create it.
	 * 
	 * @throws IOException If an exception occurs while loading a world.
	 */
	private void loadWorlds() throws IOException {
		File worldsDirectory = new File("worlds");
		File[] worldFiles = worldsDirectory.listFiles();

		if (worldFiles == null) {
			// No worlds to load
			return;
		}

		for (File file : worldFiles) {
			// Read file magic value
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			long magic = dis.readLong();
			dis.close();

			this.worlds.put(file.getName(), WorldLoaderFactory.getInstance().getWorldLoader(magic).loadWorld(file));
		}
	}

	private void loadRanks() throws IOException {

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
		synchronized (this.stateLock) {
			// Save the current server state
			oldState = this.state;

			// Reject the stop request if the server is not STARTED
			if (!this.state.isStopCallAllowed()) {
				throw new RuntimeException("The current server state does not allow stopping it.");
			}

			this.state = ClassicubeServerState.STOPPING;
		}

		// Close the listener socket. This will cause the listener thread to exit as a
		// side effect.
		try {
			this.socket.close();
		} catch (IOException e) {
			// Reset the server state
			synchronized (this.stateLock) {
				this.state = oldState;
			}

			// Propagate the exception
			throw e;
		}

		// Set the server state to STOPPED
		synchronized (this.stateLock) {
			this.state = ClassicubeServerState.STOPPED;

			// Forget the server salt
			this.salt = null;
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
	 * This method contains the main code for the listen thread. It is in charge for
	 * waiting for incoming client connections, and accepting them.
	 */
	private void listenThreadMain() {
		while (true) {
			try {
				Socket clientSocket = ClassicubeServer.this.socket.accept();

				Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO,
						clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort()
								+ " is connecting...");

				new Player(clientSocket);
			} catch (IOException e) {
				synchronized (ClassicubeServer.this.stateLock) {
					// Check if the exception occurred when the server was stopping. If so, the
					// exception was caused by the call to ClassicubeServer.stop(), which is
					// expected. We do not have to log a message in this situation. Otherwise, the
					// exception testifies of another important problem. We cannot do anything from
					// there, so we just put a log message and exit the thread.
					if (ClassicubeServer.this.state != ClassicubeServerState.STOPPING) {
						Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.SEVERE,
								"Listen thread encountered an exception. The thread is no longer running, and new incoming client connections cannot be accepted.",
								e);
					}
				}
			}
		}
	}

	/**
	 * Returns a boolean value that determines whether the name verification key is
	 * correct or not. If it is correct, then this method will return
	 * <code>true</code>, otherwise it will return <code>false</code>.
	 * 
	 * @param name   The login of the player
	 * @param mppass The name verification key of the player
	 * @return Returns a boolean value that determines whether the name verification
	 *         key is correct or not.
	 * @throws RuntimeException if the server is not in the
	 *                          {@link ClassicubeServerState#STARTED} state, or if
	 *                          the JRE does not support MD5 hash algorithm.
	 */
	public boolean verifyName(String name, String mppass) {
		// Get the salt
		String salt = null;

		synchronized (this.stateLock) {
			// If the server is not in the STARTED state, then the salt is not generated
			// yet, so we just throw an exception.
			if (this.state != ClassicubeServerState.STARTED) {
				throw new RuntimeException("The server is not started.");
			}

			salt = this.salt;
		}

		// Get the message digest instance for MD5
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// This should never happen
			throw new RuntimeException("The server does not support MD5 hash algorithm.");
		}

		// Compute the expected name verification key
		byte[] hashResult = messageDigest.digest((salt + name).getBytes(Charset.forName("US-ASCII")));

		// Check the correspondance of the values
		return StringUtils.arrayToHex(hashResult).equalsIgnoreCase(mppass);
	}

	/**
	 * Returns a world by its name, or <code>null</code> if no world has this name.
	 * 
	 * @param worldName The name of the world.
	 * @return The world with the given name.
	 */
	public World getWorldByName(String worldName) {
		return this.worlds.get(worldName);
	}

	/**
	 * Returns a rank by its name, or <code>null</code> if no rank has this name.
	 * 
	 * @param rankName The name of the rank.
	 * @return The rank with the given name.
	 */
	public Rank getRankByName(String rankName) {
		return this.ranks.get(rankName);
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
