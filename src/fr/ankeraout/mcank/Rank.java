package fr.ankeraout.mcank;

/**
 * This class represents a player rank. A player rank basically defines the
 * level of permission a player has.
 * 
 * @author Ankeraout
 *
 */
public class Rank {
	/**
	 * The name of the rank (guest, builder, etc)
	 */
	private String name;

	/**
	 * The color of the nickname of the players who have this rank
	 */
	private char color;

	/**
	 * The permission level of this rank. This can be any value. A rank with a
	 * higher permission level will have more permissions than a rank with a lower
	 * permission level, because the permissions are calculated on a minimum rank
	 * level.
	 */
	private int permissionLevel;

	/**
	 * Creates a new rank with the given informations.
	 * 
	 * @param name            The name of the rank.
	 * @param color           The color of the nicknames of the players who have
	 *                        this rank.
	 * @param permissionLevel The permission level of this rank.
	 */
	public Rank(String name, char color, int permissionLevel) {
		this.name = name;
		this.color = color;
		this.permissionLevel = permissionLevel;
	}

	/**
	 * Returns the name of the rank.
	 * 
	 * @return The name of the rank.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the color of the nicknames of the players who have this rank.
	 * 
	 * @return The color of the nicknames of the players who have this rank.
	 */
	public char getColor() {
		return this.color;
	}

	/**
	 * Returns the permission level of this rank.
	 * 
	 * @return The permission level of this rank.
	 */
	public int getPermissionLevel() {
		return this.permissionLevel;
	}
}
