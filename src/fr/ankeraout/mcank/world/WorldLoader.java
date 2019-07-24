package fr.ankeraout.mcank.world;

import java.io.File;
import java.io.IOException;

/**
 * This interface must be implemented by the world loaders. A world loader is an
 * object that is in charge of loading data from the world files. It is an
 * abstraction layer between the {@link World} class and the actual world file.
 * This makes sure that in case of a server software update, if the world format
 * changes, the server will still be able to load old world files and convert
 * them into the new world format.
 * 
 * @author Ankeraout
 *
 */
public interface WorldLoader {
	/**
	 * Returns the magic value of the world loader
	 * 
	 * @return The magic value of the world loader
	 */
	public long getMagicValue();

	/**
	 * Loads a world from a world file and returns the corresponding {@link World}
	 * object. This method only loads the main attributes of the {@link World}
	 * object, it does not load the block data.
	 * 
	 * @param name The file of the world to load.
	 * @return The world that was loaded
	 * @throws IOException If an I/O error occurs while loading the world file, or
	 *                     if the world format is not recognized (the world file is
	 *                     probably corrupt).
	 */
	public World loadWorld(File file) throws IOException;

	/**
	 * Loads the block data of the given world. This method should probably only be
	 * called by {@link World#load()}.
	 * 
	 * @param world The world to load.
	 * @throws IOException If an I/O error occurs while loading the block data, or
	 *                     if the block data format is not recognized (the world
	 *                     file is probably corrupt).
	 */
	public void loadBlockData(World world) throws IOException;
}
