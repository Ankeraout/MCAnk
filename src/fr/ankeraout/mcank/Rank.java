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
}
