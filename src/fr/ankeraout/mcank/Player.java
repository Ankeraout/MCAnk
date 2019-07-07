package fr.ankeraout.mcank;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.ankeraout.mcank.io.ClassicubeInputStream;
import fr.ankeraout.mcank.io.ClassicubeOutputStream;
import fr.ankeraout.mcank.math.Orientation;
import fr.ankeraout.mcank.math.Position;
import fr.ankeraout.mcank.world.World;

/**
 * This class represents a connected ClassiCube player. It is in charge for
 * managing communication with the client by receiving its packets and sending
 * the appropriate responses. This class can then be seen as an abstraction
 * layer over the client socket.
 * 
 * @author Ankeraout
 *
 */
public class Player {
	/**
	 * The name of the player (login)
	 */
	private String name;

	/**
	 * The current position of the player
	 */
	private Position position;

	/**
	 * The current orientation of the player
	 */
	private Orientation orientation;

	/**
	 * The current world of the player
	 */
	private World world;

	/**
	 * The current rank of the player
	 */
	private Rank rank;

	/**
	 * The network socket connected to the player
	 */
	private Socket socket;

	/**
	 * The thread that will be responsible for reading the incoming data from the
	 * client, raising player events, and communicating with the client.
	 */
	private Thread playerMainLoopThread;

	/**
	 * The thread that will be responsible for kicking the client if he has not
	 * logged in before the end of the login timer.
	 */
	private Thread playerloginTimeoutThread;

	/**
	 * The input stream that will be used for reading incoming client packets.
	 */
	private ClassicubeInputStream inputStream;

	/**
	 * The output stream that will be used for sending packets to the client.
	 */
	private ClassicubeOutputStream outputStream;

	/**
	 * The lock for the output stream. This prevents packet data from being written
	 * in the wrong order if at least two threads try to write at the same moment.
	 */
	private Object outputStreamLock;

	/**
	 * Creates a new {@link Player} object. Two threads will be started after
	 * calling this constructor:
	 * <ul>
	 * <li>The first thread will be constantly reading the incoming data from the
	 * client</li>
	 * <li>The other thread will wait for a certain delay, and then disconnect the
	 * client if he still has not logged in. This delay is configured as the login
	 * timeout.</li>
	 * </ul>
	 * 
	 * @throws IOException if an exception occurs while initializing the player
	 *                     socket.
	 */
	public Player(Socket socket) throws IOException {
		this.socket = socket;
		
		// Initialize the locks
		this.outputStreamLock = new Object();

		// Retrieve the socket streams.
		this.inputStream = new ClassicubeInputStream(socket.getInputStream());
		this.outputStream = new ClassicubeOutputStream(socket.getOutputStream());

		// Create the player main thread and run it
		this.playerMainLoopThread = new Thread(() -> this.mainLoop());
		this.playerMainLoopThread.start();

		// Create the player login timeout thread and run it
		this.playerloginTimeoutThread = new Thread(() -> this.loginWait());
		this.playerloginTimeoutThread.start();
	}

	/**
	 * This method contains the code for name verification. If this option is
	 * enabled on the server, this method will be called and will check if the
	 * player was correctly logged in by Classicube API.
	 * 
	 * @param mppass The verification key given by the player.
	 * @throws Exception If the name verification fails. The message of the
	 *                   exception is the message to send to the client.
	 */
	private void verifyName(String mppass) throws Exception {
		try {
			if(!ClassicubeServer.getInstance().verifyName(this.name, mppass)) {
				throw new Exception("Name verification failed.");
			}
		} catch(RuntimeException e) {
			this.kick(e.getMessage());
		}
	}

	/**
	 * This method contains the code for the login phase of the player connection.
	 * It is in charge for receiving client information, checking it, and
	 * registering the player in the player list. At the end of its execution, this
	 * method also kills the player login timeout thread to make sure the player
	 * does not get disconnected for taking too long to login if he has logged in.
	 * 
	 * @throws IOException if an exception occurs while communicating with the
	 *                     client. This can happen if the player disconnects during
	 *                     the login phase.
	 */
	private void doLogin() throws IOException {
		// Wait for client identification packet and read it
		if (this.inputStream.read() != PacketID.PLAYER_IDENTIFICATION.getID()) {
			this.kick("Expected first packet to be player identification.");
			return;
		}

		// Check client protocol version
		if (this.inputStream.read() != ClassicubeServer.PROTOCOL_VERSION) {
			this.kick("Connection refused: wrong protocol version.");
			return;
		}

		// Get player username
		this.name = this.inputStream.readClassicubeString();

		// Read the verification key
		String mppass = this.inputStream.readClassicubeString();

		// Do name verification
		if (ClassicubeServer.getInstance().getProperties().getNameVerification()) {
			try {
				this.verifyName(mppass);
			} catch (Exception e) {
				this.kick(e.getMessage());
				return;
			}
		}

		// Read the unused byte
		this.inputStream.read();
		
		Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO, this.name + " is connecting...");

		// Kill the player login timeout thread
		this.playerloginTimeoutThread.interrupt();
	}

	/**
	 * This method is the main loop of the player. It is called by the player main
	 * loop thread in the {@link PlayerMainLoopRunnable#run()} method. It is in
	 * charge for reading the incoming data from the client, and interpreting it. If
	 * the client supports CPE, then the CPE negotiation is done in this method.
	 */
	private void mainLoop() {
		try {
			// Do player login phase
			this.doLogin();
		} catch (IOException e) {
			// Log an error message
			Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO,
					this.socket.getRemoteSocketAddress().toString() + " disconnected during the login phase.");

			// Terminate the thread
			return;
		}
		
		// Kill the login timeout thread
		this.playerloginTimeoutThread.interrupt();
		
		// TODO: Teleport player to the main world
		// TODO: Read incoming player packets
	}

	/**
	 * This method is the main code for the login timeout thread. It consists of
	 * waiting for the defined amount of time (configured as login-timeout in the
	 * configuration file), and then checking if the player has logged in or not. If
	 * the player is logged in, then the thread just dies and nothing happens. If
	 * the player has not logged in yet, it gets kicked from the server.
	 */
	private void loginWait() {
		boolean kick = true;

		try {
			Thread.sleep(ClassicubeServer.getInstance().getProperties().getLoginTimeout());
		} catch (InterruptedException e) {
			// The player has logged in because the main loop thread has interrupted this
			// one.
			kick = false;
		}

		if (kick) {
			Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO,
					this.socket.getRemoteSocketAddress().toString() + " took too long to login.");

			try {
				this.kick("You took too long to login.");
			} catch (IOException e) {
				Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.WARNING,
						"Failed to send kick message to " + this.socket.getRemoteSocketAddress().toString() + ".");
			}
		}
	}

	/**
	 * Kicks the player and shows him the given error message.
	 * 
	 * @param reason The reason why the player has been kicked.
	 * @throws IOException If an error occurred while sending the packet to the
	 *                     player.
	 */
	public void kick(String reason) throws IOException {
		String identifier = null;
		
		if(this.name == null) {
			identifier = this.socket.getRemoteSocketAddress().toString();
		} else {
			identifier = this.name;
		}
		
		Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO, "Player \"" + identifier + "\" was kicked. Reason: " + reason);
		
		synchronized (this.outputStreamLock) {
			// Send the kick packet
			this.outputStream.write(PacketID.KICK.getID());
			this.outputStream.writeClassicubeString(reason);
			this.outputStream.flush();

			// Close the connection
			this.socket.close();
		}
	}
}
