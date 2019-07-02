package fr.ankeraout.mcank;

import java.io.IOException;

/**
 * This class only contains the entry point of the application. It must not be
 * used in the application code.
 * 
 * @author Ankeraout
 */
public class Main {

	/**
	 * The entry point of the application. Even if it is static and public, this
	 * method must not be called from the application code.
	 * 
	 * @param args The launch arguments of the application
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ClassicubeServer.getInstance().loadProperties();
		ClassicubeServer.getInstance().start();
	}
}
