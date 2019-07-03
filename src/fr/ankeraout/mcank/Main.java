package fr.ankeraout.mcank;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		ClassicubeServer.getInstance().getProperties().loadProperties();
		ClassicubeServer.getInstance().start();
		Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.INFO,
				"The server is started. Listening on " + ClassicubeServer.getInstance().getProperties().getIP() + ":"
						+ ClassicubeServer.getInstance().getProperties().getPort() + ".");
	}
}
