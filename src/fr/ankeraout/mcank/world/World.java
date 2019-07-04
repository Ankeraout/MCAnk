package fr.ankeraout.mcank.world;

import java.io.IOException;

import fr.ankeraout.mcank.worldgen.WorldGenerator;

/**
 * This class contains the code for the game worlds. A server can have multiple
 * game worlds, and players can go from one world to another by using a chat
 * command.
 * 
 * @author Ankeraout
 *
 */
public class World {
	/**
	 * This lock protects the block data and the load status of the world.
	 */
	private Object worldLock;

	/**
	 * Contains the status of the world. See {@link WorldLoadStatus} for more
	 * information.
	 */
	private WorldLoadState loadState;

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
		if(width % 16 != 0) {
			throw new IllegalArgumentException("The width value is not a multiple of 16.");
		}

		if(width < 16 || width > 1024) {
			throw new IllegalArgumentException("The width value is out of bounds.");
		}

		if(height % 16 != 0) {
			throw new IllegalArgumentException("The height value is not a multiple of 16.");
		}

		if(height < 16 || height > 1024) {
			throw new IllegalArgumentException("The height value is out of bounds.");
		}

		if(depth % 16 != 0) {
			throw new IllegalArgumentException("The depth value is not a multiple of 16.");
		}

		if(depth < 16 || depth > 1024) {
			throw new IllegalArgumentException("The depth value is out of bounds.");
		}

		// Initialize locks
		this.worldLock = new Object();

		// Initialize world attributes
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

	/**
	 * Loads the world.
	 * 
	 * @throws IOException      If the world file could not be read
	 * @throws RuntimeException If the world is not in the
	 *                          {@link WorldLoadState#UNLOADED} state.
	 */
	public void load() throws IOException, RuntimeException {
		// The state of the world before loading it
		WorldLoadState oldState = null;

		// Set the world state to LOADING
		synchronized(this.worldLock) {
			// Save the current world state
			oldState = this.loadState;

			// Reject the load request if the world is not UNLOADED
			if(!this.loadState.isStartCallAllowed()) {
				throw new RuntimeException("The current world state does not allow loading it.");
			}

			this.loadState = WorldLoadState.LOADING;
		}

		// TODO

		// Set the world state to LOADED
		synchronized(this.worldLock) {
			this.loadState = WorldLoadState.LOADED;
		}
	}

	/**
	 * Saves the world file.
	 * 
	 * @throws IOException If the world file could not be written.
	 */
	public void save() throws IOException {
		synchronized(this.worldLock) {
			
		}
	}

	/**
	 * Unloads the world.
	 * 
	 * @throws IOException      If the world file could not be written. If this
	 *                          happens, the world unloading request is aborted to
	 *                          prevent any data loss.
	 * @throws RuntimeException If the world is not in the
	 *                          {@link WorldLoadState#LOADED} state.
	 */
	public void unload() throws IOException, RuntimeException {
		// The state of the world before stopping it
		WorldLoadState oldState = null;

		// Set the world state to UNLOADING
		synchronized(this.worldLock) {
			// Save the current world state
			oldState = this.loadState;

			// Reject the unload request if the world is not LOADED
			if(!this.loadState.isStopCallAllowed()) {
				throw new RuntimeException("The current world state does not allow unloading it.");
			}

			this.loadState = WorldLoadState.UNLOADING;
		}

		try {
			this.save();
		} catch(IOException e) {
			// Reset the world state
			synchronized(this.worldLock) {
				this.loadState = oldState;
			}
		}

		// TODO: kick all the players outside of this world

		// Set the world state to UNLOADED
		synchronized(this.worldLock) {
			this.loadState = WorldLoadState.UNLOADED;

			// Break the reference to the world data, allowing the garbage
			// collector to destroy the object
			this.blockData = null;
		}
	}

	/**
	 * This enum represents the current state of the world at a given time.
	 * 
	 * @author Ankeraout
	 *
	 */
	private enum WorldLoadState {
		/**
		 * This value means that the world is currently unloaded. It could be in this
		 * state for 2 reasons:
		 * <ul>
		 * <li>The world was not loaded before</li>
		 * <li>The world was previously unloaded</li>
		 * </ul>
		 */
		UNLOADED(true, false),

		/**
		 * This value means that the world is currently loading.
		 */
		LOADING(false, false),

		/**
		 * This value means that the world is currently loaded.
		 */
		LOADED(false, true),

		/**
		 * This value means that the world is currently unloading.
		 */
		UNLOADING(false, false);

		/**
		 * This value determines whether the world can be loaded when it is in this
		 * state.
		 */
		private boolean canLoad;

		/**
		 * This value determines whether the world can be unloaded when it is in this
		 * state.
		 */
		private boolean canUnload;

		/**
		 * Creates a new {@link WorldLoadState} value.
		 * 
		 * @param canLoad   A value that determines whether the {@link World#load()}
		 *                  method can be called when the world is in this state.
		 * @param canUnload A value that determines whether the {@link World#unload()}
		 *                  method can be called when the world is in this state
		 */
		private WorldLoadState(boolean canLoad, boolean canUnload) {
			this.canLoad = canLoad;
			this.canUnload = canUnload;
		}

		/**
		 * Returns a boolean value that determines whether the {@link World#load()}
		 * method can be called when the world is in this state.
		 * 
		 * @return A boolean value that determines whether the {@link World#load()}
		 *         method can be called when the world is in this state.
		 */
		public boolean isStartCallAllowed() {
			return this.canLoad;
		}

		/**
		 * Returns a boolean value that determines whether the {@link World#unload()}
		 * method can be called when the world is in this state.
		 * 
		 * @return A boolean value that determines whether the {@link World#unload()}
		 *         method can be called when the world is in this state.
		 */
		public boolean isStopCallAllowed() {
			return this.canUnload;
		}
	}
}
