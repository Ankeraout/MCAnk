package fr.ankeraout.mcank;

import java.io.Serializable;

import fr.ankeraout.mcank.worldgen.WorldGenerator;

/**
 * This class contains the code for the game worlds. A server can have multiple
 * game worlds, and players can go from one world to another by using a chat
 * command.
 * 
 * @author Ankeraout
 *
 */
public class World implements Serializable {
	private static final long serialVersionUID = 2201071094020526724L;

	/**
	 * The name of the world.
	 */
	private String name;

	/**
	 * The message of the day of the world. This should be <code>null</code> if the
	 * world has no message of the day.
	 */
	private String motd;

	/**
	 * The width of the world in blocks. This must be a multiple of 16.
	 */
	private int width;

	/**
	 * The height of the world in blocks. This must be a multiple of 16.
	 */
	private int height;

	/**
	 * The depth of the world in blocks. This must be a multiple of 16.
	 */
	private int depth;

	/**
	 * The block data of the world. The index of a block can be calculated from its
	 * coordinates with the following formula:
	 * <code>blockDataIndex = (y * depth + z) * width + x</code> where
	 * blockDataIndex is the index of the block in the array at the given (x, y, z)
	 * coordinates.
	 */
	private int blockData[];

	/**
	 * The position on the X axis where a player is teleported to when joining this
	 * world.
	 */
	private float spawnX;

	/**
	 * The position on the Y axis where a player is teleported to when joining this
	 * world.
	 */
	private float spawnY;

	/**
	 * The position on the Z axis where a player is teleported to when joining this
	 * world.
	 */
	private float spawnZ;

	/**
	 * The value to which the player's yaw is set when joining this world in
	 * degrees.
	 */
	private float spawnYaw;

	/**
	 * The value to which the player's pitch is set when joining this world in
	 * degrees.
	 */
	private float spawnPitch;

	/**
	 * The minimum required permission level to be able to build in this world.
	 */
	private int buildPermission;

	/**
	 * The minimum required permission level to be able to visit this world.
	 */
	private int visitPermission;

	/**
	 * Creates a new world with the given name, and generates the chunks in it using
	 * the given {@link WorldGenerator} and the given seed.
	 * 
	 * @param name      The name of the new world to create
	 * @param width     The width of the new world in blocks. This value must be a
	 *                  multiple of 16 between 16 and 1024.
	 * @param height    The height of the new world in blocks. This value must be a
	 *                  multiple of 16 between 16 and 1024.
	 * @param depth     The depth of the new world in blocks. This value must be a
	 *                  multiple of 16 between 16 and 1024.
	 * @param generator The generator of the world.
	 * @param seed      The seed for generating this world. This will be used to
	 *                  feed the generator.
	 * 
	 * @throws IllegalArgumentException If one of the argument values is incorrect.
	 */
	public World(String name, int width, int height, int depth, WorldGenerator generator, long seed) {
		if (width % 16 != 0) {
			throw new IllegalArgumentException("The width value is not a multiple of 16.");
		}

		if (width < 16 || width > 1024) {
			throw new IllegalArgumentException("The width value is out of bounds.");
		}

		if (height % 16 != 0) {
			throw new IllegalArgumentException("The height value is not a multiple of 16.");
		}

		if (height < 16 || height > 1024) {
			throw new IllegalArgumentException("The height value is out of bounds.");
		}

		if (depth % 16 != 0) {
			throw new IllegalArgumentException("The depth value is not a multiple of 16.");
		}

		if (depth < 16 || depth > 1024) {
			throw new IllegalArgumentException("The depth value is out of bounds.");
		}

		this.name = name;
		this.motd = null;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.blockData = new int[width * height * depth];
		this.spawnX = width / 2f;
		this.spawnY = height / 2f;
		this.spawnZ = depth / 2f;
		this.spawnYaw = 0f;
		this.spawnPitch = 0f;
		this.buildPermission = 0;
		this.visitPermission = 0;

		// Generate world
		generator.generateWorld(this.blockData, width, height, depth, seed);
	}
}
