package fr.ankeraout.mcank.worldgen;

import fr.ankeraout.mcank.Blocks;

public class FlatgrassWorldGenerator extends WorldGenerator {
	@Override
	public void generateChunk(int[] worldData, int worldWidth, int worldHeight, int worldDepth, int chunkX, int chunkZ,
			long seed) {
		int chunkBlockX = chunkX * 16;
		int chunkBlockZ = chunkZ * 16;
		
		for(int y = 0; y < (worldHeight / 2) - 1; y++) {
			for(int x = chunkBlockX; x < chunkBlockX + 16; x++) {
				for(int z = chunkBlockZ; z < chunkBlockZ + 16; z++) {
					worldData[(y * worldDepth + z) * worldWidth + x] = Blocks.DIRT.getBlockId();
				}
			}
		}
		
		for(int x = chunkBlockX; x < chunkBlockX + 16; x++) {
			for(int z = chunkBlockZ; z < chunkBlockZ + 16; z++) {
				worldData[((worldHeight / 2) * worldDepth + z) * worldWidth + x] = Blocks.DIRT.getBlockId();
			}
		}
	}
	
	@Override
	public void generateWorld(int[] worldData, int worldWidth, int worldHeight, int worldDepth, long seed) {
		for(int y = 0; y < (worldHeight / 2) - 1; y++) {
			for(int x = 0; x < worldWidth; x++) {
				for(int z = 0; z < worldDepth; z++) {
					worldData[(y * worldDepth + z) * worldWidth + x] = Blocks.DIRT.getBlockId();
				}
			}
		}
		
		for(int x = 0; x < worldWidth; x++) {
			for(int z = 0; z < worldDepth; z++) {
				worldData[((worldHeight / 2) * worldDepth + z) * worldWidth + x] = Blocks.GRASS.getBlockId();
			}
		}
	}

}
