package fr.ankeraout.mcank;

import java.net.Socket;

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
	 * Creates a new {@link Player} object. Two threads will be started after
	 * calling this constructor:
	 * <ul>
	 * <li>The first thread will be constantly reading the incoming data from the
	 * client</li>
	 * <li>The other thread will wait for a certain delay, and then disconnect the
	 * client if he still has not logged in. This delay is configured as the login
	 * timeout.</li>
	 * </ul>
	 */
	public Player(Socket socket) {
		this.socket = socket;

		// Create the player main thread and run it
		new Thread(new PlayerMainLoopRunnable()).run();

		// Create the player login timeout thread and run it
		new Thread(new PlayerLoginTimeoutRunnable()).run();
	}

	/**
	 * This method is the main loop of the player. It is called by the player main
	 * loop thread in the {@link PlayerMainLoopRunnable#run()} method. It is in
	 * charge for reading the incoming data from the client, and interpreting it.
	 */
	private void mainLoop() {

	}

	/**
	 * This method is the main code for the login timeout thread. It consists of
	 * waiting for the defined amount of time (configured as login-timeout in the
	 * configuration file), and then checking if the player has logged in or not. If
	 * the player is logged in, then the thread just dies and nothing happens. If
	 * the player has not logged in yet, it gets kicked from the server.
	 */
	private void loginWait() {

	}

	/**
	 * This class contains the code for the player main loop thread. This class is
	 * an internal private class because it is not supposed to be used anywhere else
	 * than in the {@link Player} class.
	 * 
	 * @author Ankeraout
	 *
	 */
	private class PlayerMainLoopRunnable implements Runnable {
		@Override
		public void run() {
			Player.this.mainLoop();
		}
	}

	private class PlayerLoginTimeoutRunnable implements Runnable {
		@Override
		public void run() {
			Player.this.loginWait();
		}
	}
}
