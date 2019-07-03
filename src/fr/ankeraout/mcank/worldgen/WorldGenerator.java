package fr.ankeraout.mcank.worldgen;

/**
 * This abstract class defines how a world generator should be made. All the
 * world generators must extend this class.
 * 
 * @author Ankeraout
 *
 */
public abstract class WorldGenerator {
	/**
	 * This method generates a chunk of the world.
	 * @param worldData The block data array of the world.
	 * @param worldWidth The width of the world, in blocks.
	 * @param worldHeight The height of the world, in blocks.
	 * @param worldDepth The depth of the world, in blocks.
	 * @param chunkX The X position of the chunk, in chunks (16 blocks).
	 * @param chunkZ The Z position of the chunk, in chunks (16 blocks).
	 * @param seed The seed for the chunk generation.
	 */
	public abstract void generateChunk(int[] worldData, int worldWidth, int worldHeight, int worldDepth, int chunkX,
			int chunkZ, long seed);

	/**
	 * This method generates the entire world.
	 * @param worldData The block data array of the world.
	 * @param worldWidth The width of the world, in blocks.
	 * @param worldHeight The height of the world, in blocks.
	 * @param worldDepth The depth of the world, in blocks.
	 * @param seed The seed for the world generation.
	 */
	public void generateWorld(int[] worldData, int worldWidth, int worldHeight, int worldDepth, long seed) {
		// Generate chunks
		for (int x = 0; x < worldWidth / 16; x++) {
			for (int z = 0; z < worldDepth / 16; z++) {
				this.generateChunk(worldData, worldWidth, worldHeight, worldDepth, x, z, seed);
			}
		}
	}
}
