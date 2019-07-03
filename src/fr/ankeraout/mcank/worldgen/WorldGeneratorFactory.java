package fr.ankeraout.mcank.worldgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a list of all the available world generators. It is a
 * singleton factory, which means that it can be accessed from anywhere in the
 * code.
 * 
 * @author Ankeraout
 *
 */
public class WorldGeneratorFactory {
	/**
	 * The unique instance of this class (singleton).
	 */
	private static WorldGeneratorFactory instance;

	/**
	 * The list of all the available world generators.
	 */
	private HashMap<String, WorldGenerator> worldGenerators;

	/**
	 * This constructor is made private in order to prevent external instantiation
	 * because this class is a singleton. It initializes the list of world
	 * generators with the default ones.
	 */
	private WorldGeneratorFactory() {
		// Register the default map generators
		this.worldGenerators = new HashMap<String, WorldGenerator>();
		this.worldGenerators.put("flatgrass", new FlatgrassWorldGenerator());
	}

	/**
	 * Returns the unique instance of the {@link WorldGeneratorFactory} class.
	 * 
	 * @return The unique instance of the {@link WorldGeneratorFactory} class.
	 */
	public static synchronized WorldGeneratorFactory getInstance() {
		if (WorldGeneratorFactory.instance == null) {
			WorldGeneratorFactory.instance = new WorldGeneratorFactory();
		}

		return WorldGeneratorFactory.instance;
	}

	/**
	 * Returns the generator from its name. If no generator was previously
	 * registered with the given name, then this method will return
	 * <code>null</code>.
	 * 
	 * @param generatorName The name of the generator. (ex: flatgrass)
	 * @return The generator that was registered with the given name.
	 */
	public WorldGenerator getGenerator(String generatorName) {
		return this.worldGenerators.get(generatorName);
	}

	/**
	 * Registers a new generator on the generator list. If another generator was
	 * already previously registered with the same generator name, then this method
	 * will throw an {@link IllegalArgumentException}.
	 * 
	 * @param generatorName The name of the generator
	 * @param generator     The {@link WorldGenerator} object that will be returned
	 *                      by {@link WorldGeneratorFactory#getGenerator(String)}
	 *                      when getting the generator with the given name.
	 * @throws IllegalArgumentException If another generator was already previously
	 *                                  registered with the same generator name.
	 */
	public void addGenerator(String generatorName, WorldGenerator generator) {
		if (this.worldGenerators.containsKey(generatorName)) {
			throw new IllegalArgumentException(
					"A world generator with the name \"" + generatorName + "\" is already registered.");
		}

		this.worldGenerators.put(generatorName, generator);
	}

	/**
	 * Returns a set that contains the names of all the available generators.
	 * @return A set that contains the names of all the available generators.
	 */
	public Set<String> getAvailableGenerators() {
		return new HashSet<String>(this.worldGenerators.keySet());
	}
}
