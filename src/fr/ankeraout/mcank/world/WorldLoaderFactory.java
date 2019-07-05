package fr.ankeraout.mcank.world;

import java.util.HashMap;

/**
 * This class contains the code for storing the different {@link WorldLoader}
 * objects. It is a singleton, which means that it is accessible from anywhere
 * in the code, and that only one instance of this object can exist at a given
 * time. The fact that this factory is a singleton allows player plugins (for
 * example) to add their own world loading code as an extension.
 * 
 * @author Ankeraout
 *
 */
public class WorldLoaderFactory {
	/**
	 * The unique instance of this class (singleton).
	 */
	private static WorldLoaderFactory instance;

	/**
	 * The list of all the available world loaders.
	 */
	private HashMap<Long, WorldLoader> worldLoaders;

	/**
	 * This constructor is made private in order to prevent external instantiation
	 * because this class is a singleton. It initializes the list of world save
	 * managers with the default ones.
	 */
	private WorldLoaderFactory() {
		// Register world loaders
		this.worldLoaders = new HashMap<Long, WorldLoader>();
	}

	/**
	 * Returns the unique instance of the {@link WorldLoaderFactory} class.
	 * 
	 * @return The unique instance of the {@link WorldLoaderFactory} class.
	 */
	public static synchronized WorldLoaderFactory getInstance() {
		if (WorldLoaderFactory.instance == null) {
			WorldLoaderFactory.instance = new WorldLoaderFactory();
		}

		return WorldLoaderFactory.instance;
	}

	/**
	 * Returns the world loader from its magic value. If no world loader was
	 * previously registered with the given name, then this method will return
	 * <code>null</code>.
	 * 
	 * @param magicValue The magic value of the world loader. (ex: flatgrass)
	 * @return The world loader that was registered with the given magic value.
	 */
	public WorldLoader getWorldLoader(Long magicValue) {
		return this.worldLoaders.get(magicValue);
	}

	/**
	 * Registers a new world loader on the world loader list. If another world
	 * loader was already previously registered with the same name, then this method
	 * will throw an {@link IllegalArgumentException}.
	 * 
	 * @param worldLoader The {@link WorldLoader} object that will be returned by
	 *                    {@link WorldLoaderFactory#getWorldLoader(Long)} when
	 *                    getting the world loader.
	 * @throws IllegalArgumentException If another world loader with the same magic
	 *                                  value was already previously registered.
	 */
	public void addWorldLoader(WorldLoader worldLoader) {
		if (this.worldLoaders.containsKey(worldLoader.getMagicValue())) {
			throw new IllegalArgumentException("A world loader with the same magic value is already registered.");
		}

		this.worldLoaders.put(worldLoader.getMagicValue(), worldLoader);
	}
}
