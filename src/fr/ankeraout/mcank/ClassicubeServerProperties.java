package fr.ankeraout.mcank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the code for loading, saving, and checking the server
 * properties.
 * 
 * @author Ankeraout
 *
 */
public class ClassicubeServerProperties extends Properties {
	private static final long serialVersionUID = 2617911347398811397L;

	/**
	 * This is the list of the checked properties. All the properties defined in
	 * this table are guaranteed to be present in this object and they are also
	 * guaranteed to have the expected type.
	 */
	private static final PropertyRecord[] propertyRecords = new PropertyRecord[] { new PropertyRecord("name", "STRING"),
			new PropertyRecord("motd", "STRING"), new PropertyRecord("ip", "STRING"),
			new PropertyRecord("port", "INTEGER"), new PropertyRecord("public", "BOOLEAN"),
			new PropertyRecord("advertise", "BOOLEAN"), new PropertyRecord("default-world", "STRING"),
			new PropertyRecord("default-rank", "STRING"), new PropertyRecord("verify-names", "BOOLEAN"),
			new PropertyRecord("login-timeout", "INTEGER"), new PropertyRecord("afk-timeout", "INTEGER"),
			new PropertyRecord("whitelist", "BOOLEAN"), new PropertyRecord("save-interval", "INTEGER"),
			new PropertyRecord("tcp-no-delay", "BOOLEAN"), new PropertyRecord("max-players", "INTEGER"),
			new PropertyRecord("mark-new-lines", "BOOLEAN"), new PropertyRecord("tick-interval", "INTEGER"),
			new PropertyRecord("backlog", "INTEGER") };

	/**
	 * The path to the server properties file.
	 */
	private static final String PROPERTIES_FILE_PATH = "server.properties";

	/**
	 * The path to the default server.properties file. This file should be loaded as
	 * a resource.
	 */
	private static final String DEFAULT_PROPERTIES_FILE_PATH = "/server-default.properties";

	/**
	 * Creates a new instance of the {@link ClassicubeServerProperties} class, with
	 * no properties loaded.
	 */
	public ClassicubeServerProperties() {
		super();
	}

	/**
	 * Creates a new instance of the {@link ClassicubeServerProperties} class, with
	 * the given properties loaded. The type of the properties will be checked.
	 * 
	 * @param p The properties to load by default in the properties object.
	 * 
	 * @throws RuntimeException If the type of at least one of the checked
	 *                          properties in the given {@link Properties} object is
	 *                          not the expected type.
	 */
	public ClassicubeServerProperties(Properties p) {
		// Check the properties types
		ClassicubeServerProperties.checkProperties(p);

		// Set the properties in this object
		this.merge(p);
	}

	/**
	 * Takes the properties from the given {@link Properties} object, and puts all
	 * its values in this object, without checking their type.
	 * 
	 * @param p The object that contains the property values to take.
	 */
	private void merge(Properties p) {
		for(Object key : p.keySet()) {
			super.put(key, p.get(key));
		}
	}

	/**
	 * Check the types of the expected properties in the given properties object.
	 * 
	 * @param p The properties object to check
	 * @throws RuntimeException If the type of at least one of the properties is
	 *                          wrong.
	 */
	private static void checkProperties(Properties p) throws RuntimeException {
		for(PropertyRecord propertyRecord : ClassicubeServerProperties.propertyRecords) {
			if(p.containsKey(propertyRecord.getPropertyName())) {
				switch(propertyRecord.getPropertyType()) {
					case "STRING":
						// Nothing to do, the property is already a String
						break;
					case "INTEGER":
						// Try to parse the property value as as integer
						try {
							Integer.parseInt(p.getProperty(propertyRecord.getPropertyName()));
						} catch(NumberFormatException e) {
							// Failed to parse the integer, we throw an exception.
							throw new RuntimeException("Failed to parse an integer value for the following property: "
									+ propertyRecord.getPropertyName());
						}
						break;
					case "BOOLEAN":
						// Try to parse a boolean value
						if(!p.getProperty(propertyRecord.getPropertyName()).equalsIgnoreCase("true")
								&& !p.getProperty(propertyRecord.getPropertyName()).equalsIgnoreCase("false")) {
							// Failed to parse the boolean value, we throw an exception.
							throw new RuntimeException("Failed to parse a boolean value for the following property: "
									+ propertyRecord.getPropertyName());
						}
						break;
					default:
						// If this ever happens, this definitely is a bug. The type needs to be added to
						// this switch.
						throw new RuntimeException(
								"Unknown property type \"" + propertyRecord.getPropertyType() + "\".");
				}
			}
		}
	}

	/**
	 * Loads the default server properties.
	 */
	public void loadDefaultProperties() {
		try {
			this.load(System.class.getResourceAsStream(ClassicubeServerProperties.DEFAULT_PROPERTIES_FILE_PATH));
		} catch(IOException e) {
			throw new RuntimeException("An exception occurred while loading the default properties file.", e);
		}
	}

	/**
	 * Loads the server configuration from the properties file.
	 * 
	 * @throws IOException If an I/O exception occurred while reading the server
	 *                     configuration file or if the format of the file is not
	 *                     recognized.
	 */
	public void loadProperties() throws IOException {
		// Reload the server default properties
		this.loadDefaultProperties();

		// Create the File object for future checks
		File propertiesFilePath = new File(ClassicubeServerProperties.PROPERTIES_FILE_PATH);

		// If the properties file does not exist, create it with the default values
		if(!propertiesFilePath.exists()) {
			// Copy the default properties file
			Files.copy(System.class.getResourceAsStream(ClassicubeServerProperties.DEFAULT_PROPERTIES_FILE_PATH),
					new File(ClassicubeServerProperties.PROPERTIES_FILE_PATH).toPath());

			// Send a warning to the user
			Logger.getLogger(ClassicubeServer.LOGGER_NAME).log(Level.WARNING,
					"The server properties file was not found. Loaded defaults.");

			// We don't need to load it again because the default values were already loaded
			// previously. We just need to exit the method.
			return;
		} else if(!propertiesFilePath.isFile()) {
			throw new RuntimeException("The path to the server properties file leads to something that is not a file.");
		}

		// Load the server properties file
		this.load(new FileInputStream(ClassicubeServerProperties.PROPERTIES_FILE_PATH));
	}

	/**
	 * Loads the given properties file and checks the type of all the properties.
	 * 
	 * @param inputStream The {@link InputStream} from where the properties will be
	 *                    loaded.
	 * 
	 * @throws IOException      If an I/O error occurred while reading the
	 *                          properties stream, or if the contents of the stream
	 *                          do not respect the properties file format.
	 * 
	 * @throws RuntimeException If at least one of the properties in the stream has
	 *                          a wrong type.
	 */
	@Override
	public void load(InputStream inputStream) throws IOException {
		// Load the properties
		Properties p = new Properties();
		p.load(inputStream);

		// Check the type of the properties
		ClassicubeServerProperties.checkProperties(p);

		// Load the properties
		this.merge(p);
	}

	/**
	 * Returns the value of the given property as a boolean value.
	 * 
	 * @return The value of the given property as a boolean value.
	 * @throws RuntimeException If the given property does not have a boolean value.
	 */
	private boolean getAsBoolean(String propertyName) {
		String value = (String)this.get(propertyName);

		if(value.equalsIgnoreCase("true")) {
			return true;
		} else if(value.equalsIgnoreCase("false")) {
			return false;
		} else {
			throw new RuntimeException("The given property does not have a boolean value.");
		}
	}

	/**
	 * Returns the server socket backlog value.
	 * 
	 * @return The server socket backlog value.
	 */
	public int getBacklog() {
		return Integer.parseInt((String)this.get("backlog"));
	}

	/**
	 * Returns the TCP port number that the server will be listening to.
	 * 
	 * @return The TCP port number that the server will be listening to
	 */
	public int getPort() {
		return Integer.parseInt((String)this.get("port"));
	}

	/**
	 * Returns the IP address of the interface that the server should be listening
	 * on.
	 * 
	 * @return The IP address of the interface that the server should be listening
	 *         on.
	 */
	public String getIP() {
		return (String)this.get("ip");
	}

	/**
	 * Returns the name of the server.
	 * 
	 * @return The server's name.
	 */
	public String getName() {
		return (String)this.get("name");
	}

	/**
	 * Returns the message of the day of the server.
	 * 
	 * @return The message of the day of the server.
	 */
	public String getMotd() {
		return (String)this.get("motd");
	}

	/**
	 * Returns a boolean value that determines whether the server is public or not.
	 * 
	 * @return A boolean value that determines whether the server is public or not.
	 */
	public boolean isPublic() {
		return this.getAsBoolean("public");
	}

	/**
	 * Returns a boolean value that determines whether the server should be
	 * advertising on the public server list server.
	 * 
	 * @return A boolean value that determines whether the server should be
	 *         advertising on the public server list server.
	 */
	public boolean getAdvertise() {
		return this.getAsBoolean("advertise");
	}

	/**
	 * Returns the name of the default world
	 * 
	 * @return The name of the default world
	 */
	public String getDefaultWorld() {
		return (String)this.get("default-world");
	}

	/**
	 * Returns the name of the default rank
	 * 
	 * @return The name of the default rank
	 */
	public String getDefaultRank() {
		return (String)this.get("default-rank");
	}

	/**
	 * Returns a boolean value that determines whether the name verification should
	 * be enabled on the server.
	 * 
	 * @return A boolean value that determines whether the name verification should
	 *         be enabled on the server.
	 */
	public boolean getNameVerification() {
		return this.getAsBoolean("verify-names");
	}

	/**
	 * Returns the amount of time in milliseconds before a player gets kicked for
	 * inactivity during login phase.
	 * 
	 * @return The amount of time in milliseconds before a player gets kicked for
	 *         inactivity during login phase.
	 */
	public int getLoginTimeout() {
		return Integer.parseInt((String)this.get("login-timeout"));
	}

	/**
	 * Returns the amount of time in milliseconds before a player gets kicked for
	 * inactivity while playing.
	 * 
	 * @return The amount of time in milliseconds before a player gets kicked for
	 *         inactivity while playing.
	 */
	public int getAfkTimeout() {
		return Integer.parseInt((String)this.get("afk-timeout"));
	}

	/**
	 * Returns a boolean value that determines whether the white list should be
	 * enabled.
	 * 
	 * @return A boolean value that determines whether the white list should be
	 *         enabled.
	 */
	public boolean getWhitelist() {
		return this.getAsBoolean("whitelist");
	}

	/**
	 * Returns the interval of time between two world saves in milliseconds.
	 * 
	 * @return The interval of time between two world saves in milliseconds.
	 */
	public int getSaveInterval() {
		return Integer.parseInt((String)this.get("save-interval"));
	}

	/**
	 * Returns a boolean value that determines whether the Nagle's algorithm should
	 * be disabled on the server.
	 * 
	 * @return A boolean value that determines whether the Nagle's algorithm should
	 *         be disabled on the server.
	 */
	public boolean getTcpNoDelay() {
		return this.getAsBoolean("tcp-no-delay");
	}

	/**
	 * Returns the maximum amount of players allowed on the server.
	 * 
	 * @return The maximum amount of players allowed on the server.
	 */
	public int getMaxPlayers() {
		return Integer.parseInt((String)this.get("max-players"));
	}

	/**
	 * Returns a boolean value that determines whether the new lines in a chat
	 * message should be marked.
	 * 
	 * @return A boolean value that determines whether the new lines in a chat
	 *         message should be marked.
	 */
	public boolean getMarkNewLines() {
		return this.getAsBoolean("mark-new-lines");
	}

	/**
	 * Returns the time interval between 2 server ticks in milliseconds.
	 * @return The time interval between 2 server ticks in milliseconds.
	 */
	public int getTickInterval() {
		return Integer.parseInt((String)this.get("tick-interval"));
	}

	/**
	 * This class defines a property record, that is basically an association
	 * between a property name and a property type.
	 * 
	 * @author Ankeraout
	 *
	 */
	private static class PropertyRecord {
		/**
		 * The name of the checked property
		 */
		private String propertyName;

		/**
		 * The expected type of the checked property
		 */
		private String propertyType;

		/**
		 * Creates a new record for a checked property.
		 * 
		 * @param propertyName The name of the checked property
		 * @param propertyType The expected type of the checked property. This is a
		 *                     {@link String} that can have one of these values:
		 *                     <ul>
		 *                     <li>STRING</li>
		 *                     <li>INTEGER</li>
		 *                     <li>BOOLEAN</li>
		 *                     </ul>
		 */
		public PropertyRecord(String propertyName, String propertyType) {
			this.propertyName = propertyName;
			this.propertyType = propertyType;
		}

		/**
		 * Returns the name of the checked property.
		 * 
		 * @return The name of the checked property
		 */
		public String getPropertyName() {
			return this.propertyName;
		}

		/**
		 * Returns the expected type of the checked property.
		 * 
		 * @return The expected type of the checked property.
		 */
		public String getPropertyType() {
			return this.propertyType;
		}
	}
}
