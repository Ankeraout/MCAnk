package fr.ankeraout.mcank;

import java.io.File;
import java.io.Serializable;

import fr.ankeraout.mcank.worldgen.WorldGenerator;

/**
 * This class contains the code for the game worlds.
 * 
 * @author Ankeraout
 *
 */
public class World implements Serializable {
	private static final long serialVersionUID = 2201071094020526724L;

	private String name;
	private String motd;
	private File worldFile;
	private int width;
	private int height;
	private int depth;
	private int blockData[];
	private float spawnX;
	private float spawnY;
	private float spawnZ;
	private float spawnYaw;
	private float spawnPitch;
	private int buildPermission;
	private int visitPermission;

	public World(String name, int width, int height, int depth, WorldGenerator generator, long seed) {
		
	}
}
