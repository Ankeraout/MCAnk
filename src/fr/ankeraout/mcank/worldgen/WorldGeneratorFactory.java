package fr.ankeraout.mcank.worldgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WorldGeneratorFactory {
	private static WorldGeneratorFactory instance;
	
	private HashMap<String, WorldGenerator> worldGenerators;

	private WorldGeneratorFactory() {
		// Register the default map generators
		this.worldGenerators = new HashMap<String, WorldGenerator>();
		this.worldGenerators.put("flatgrass", new FlatgrassWorldGenerator());
	}

	public static synchronized WorldGeneratorFactory getInstance() {
		if (WorldGeneratorFactory.instance == null) {
			WorldGeneratorFactory.instance = new WorldGeneratorFactory();
		}

		return WorldGeneratorFactory.instance;
	}

	public WorldGenerator getGenerator(String generatorName) {
		return this.worldGenerators.get(generatorName);
	}
	
	public void addGenerator(String generatorName, WorldGenerator generator) {
		if(this.worldGenerators.containsKey(generatorName)) {
			throw new IllegalArgumentException("A world generator with the name \"" + generatorName + "\" is already registered.");
		}
		
		this.worldGenerators.put(generatorName, generator);
	}
	
	public Set<String> getAvailableGenerators() {
		return new HashSet<String>(this.worldGenerators.keySet());
	}
}
