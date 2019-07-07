package fr.ankeraout.mcank;

/**
 * This enum contains the IDs of the network packets. Please be careful and take
 * note that the packet IDs are not unique. For example, the packet ID 0 may
 * refer to {@link PacketID#PLAYER_IDENTIFICATION} as well as
 * {@link PacketID#SERVER_IDENTIFICATION}.
 * 
 * @author Ankeraout
 *
 */
public enum PacketID {
	PLAYER_IDENTIFICATION(0x00),
	SERVER_IDENTIFICATION(0x00),
	PING(0x01),
	LEVEL_INITIALIZE(0x02),
	LEVEL_DATA_CHUNK(0x03),
	LEVEL_FINALIZE(0x04),
	CLIENT_SET_BLOCK(0x05),
	SERVER_SET_BLOCK(0x06),
	SPAWN_PLAYER(0x07),
	POSITION_ORIENTATION_ABSOLUTE(0x08),
	POSITION_ORIENTATION_RELATIVE(0x09),
	POSITION_RELATIVE(0x0a),
	ORIENTATION(0x0b),
	DESPAWN_PLAYER(0x0c),
	MESSAGE(0x0d),
	KICK(0x0e),
	UPDATE_USER_TYPE(0x0f);

	/**
	 * The packet ID of the current value in the enum.
	 */
	private int id;

	/**
	 * Creates a new value in the {@link PacketID} enum with the given packet ID.
	 * 
	 * @param id The ID of the packet type to declare.
	 */
	private PacketID(int id) {
		this.id = id;
	}

	/**
	 * Returns the ID of the packet for the current value of the enum.
	 * 
	 * @return The ID of the packet.
	 */
	public int getID() {
		return this.id;
	}
}
