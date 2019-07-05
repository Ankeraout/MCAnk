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
	 * This method is called everytime a player calls this command, if he has the
	 * permission to do so.
	 * 
	 * @param caller The {@link Player} who has called the command.
	 * @param args   The arguments passed to the command. Note that the first
	 *               element in this array is the command name.
	 */
	public void onCall(Player caller, String[] args);
}
