package fr.ankeraout.mcank.worldgen;

public interface WorldGenerator {
	public void generateChunk(int[] worldData, int worldWidth, int worldHeight, int worldDepth, int chunkX, int chunkY, long seed);
}
