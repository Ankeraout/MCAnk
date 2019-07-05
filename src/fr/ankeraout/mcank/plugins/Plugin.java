package fr.ankeraout.mcank.plugins;

/**
 * This interface represents a plugin, which is an extension of the server
 * software that can be loaded dynamically.
 * 
 * @author Ankeraout
 *
 */
public interface Plugin {
	/**
	 * Returns the name of the plugin.
	 * 
	 * @return The name of the plugin.
	 */
	public String getName();

	/**
	 * Returns the version of the plugin.
	 * 
	 * @return The version of the plugin.
	 */
	public String getVersion();

	/**
	 * Returns the author of the plugin.
	 * 
	 * @return The author of the plugin.
	 */
	public String getAuthor();

	/**
	 * This method is called when the plugin is loaded by the server. It can be used
	 * for registering event handlers. The plugin will not be loaded if this method
	 * throws an exception.
	 */
	public void onLoad();

	/**
	 * This method is called when the plugin is unloaded by the server. The plugin
	 * will be considered unloaded even if this method throws an exception.
	 */
	public void onUnload();
}
