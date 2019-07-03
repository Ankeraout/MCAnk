package fr.ankeraout.mcank.worldgen;

public class WorldGeneratorFactory {
	private static WorldGeneratorFactory instance;
	
	private WorldGeneratorFactory() {
		
	}
	
	public static synchronized WorldGeneratorFactory getInstance() {
		if(WorldGeneratorFactory.instance == null) {
			WorldGeneratorFactory.instance = new WorldGeneratorFactory();
		}
		
		return WorldGeneratorFactory.instance;
	}
	
	public WorldGenerator getGenerator(String generatorName) {
		return null;
	}
}
