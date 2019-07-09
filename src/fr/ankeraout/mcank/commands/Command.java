package fr.ankeraout.mcank.commands;

import fr.ankeraout.mcank.Player;

/**
 * This interfaces represents a command that can be called by a player in the
 * chat. Every command must implement this interface.
 * 
 * @author Ankeraout
 *
 */
public interface Command {
	/**
	 * Returns the name of the command (cuboid, kick, etc)
	 * 
	 * @return The name of the command
	 */
	public String getName();

	/**
	 * The aliases of the command. The name of the command must not be included in
	 * this collection.
	 * 
	 * @return The aliases of the command.
	 */
	public String[] getAliases();

	/**
	 * The default permission level of the command. This method should return one
	 * value from the following ones:
	 * <ul>
	 * <li>0: If the command should be available to everyone</li>
	 * <li>20: If the command should be available to people who have little experience on the server</li>
	 * <li>40: If the command should be available to people who have experience on the server</li>
	 * <li>60: If the command should be available to people who have a lot of experience on the server</li>
	 * <li>80: If the command should be available to the server moderators</li>
	 * <li>100: If the command should be available to the server administrators</li>
	 * </ul>
	 * 
	 * @return The default permission level of the command.
	 */
	public int getDefaultPermissionLevel();

	/**
	 * This method is called everytime a player calls this command, if he has the
	 * permission to do so.
	 * 
	 * @param caller The {@link Player} who has called the command.
	 * @param args   The arguments passed to the command. Note that the first
	 *               element in this array is the command name.
	 */
	public void onCall(Player caller, String[] args);
}
