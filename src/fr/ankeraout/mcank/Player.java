package fr.ankeraout.mcank;

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

}
