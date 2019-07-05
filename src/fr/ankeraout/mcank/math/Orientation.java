package fr.ankeraout.mcank.math;

/**
 * This class represents the orientation (yaw, pitch) of an entity in a
 * {@link World}. All the values in this class are measured in degrees.
 * 
 * @author Ankeraout
 *
 */
public class Orientation {
	/**
	 * The yaw orientation value.
	 */
	public double yaw;

	/**
	 * The pitch orientation value.
	 */
	public double pitch;

	/**
	 * Initializes a new instance of the {@link Orientation} class with the yaw and
	 * pitch values set to 0.
	 */
	public Orientation() {
		this(0, 0);
	}

	/**
	 * Initializes a new instance of the {@link Orientation} class with the yaw and
	 * pitch values set to the given values.
	 * 
	 * @param yaw   The yaw value of the orientation
	 * @param pitch The pitch value of the orientation
	 */
	public Orientation(double yaw, double pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	/**
	 * Returns the value of the yaw component of the orientation.
	 * 
	 * @return The value of the yaw component of the orientation
	 */
	public double getYaw() {
		return yaw;
	}

	/**
	 * Returns the value of the yaw component of the orientation converted to fit in
	 * a byte. This is used for sending the yaw value through the network, because
	 * the client sends us and expects to receive these values as bytes.
	 * 
	 * @return The value of the yaw component as a byte.
	 */
	public byte getByteYaw() {
		return (byte) (this.yaw * 256 / 360);
	}

	/**
	 * Sets the value of the yaw component of the orientation to the given value.
	 * 
	 * @param yaw The value to which the yaw component of the orientation will be
	 *            set.
	 */
	public void setYaw(double yaw) {
		if (yaw < 0 || yaw >= 360) {
			yaw %= 360;

			if (yaw < 0) {
				yaw += 360;
			}
		}

		this.yaw = yaw;
	}

	/**
	 * Sets the value of the yaw component of the orientation from the given byte
	 * value. The byte value will be converted into degrees. This method is useful
	 * for receiving an orientation from the player via network, because the
	 * Minecraft Classic protocol defines that the orientation of the player has to
	 * be sent as a byte.
	 * 
	 * @param yaw The yaw value of the orientation as a byte.
	 */
	public void setByteYaw(byte yaw) {
		this.setYaw((double) yaw * 360 / 256);
	}

	/**
	 * Returns the value of the pitch component of the orientation.
	 * 
	 * @return The value of the pitch component of the orientation
	 */
	public double getPitch() {
		return pitch;
	}

	/**
	 * Returns the value of the pitch component of the orientation converted to fit
	 * in a byte. This is used for sending the pitch value through the network,
	 * because the client sends us and expects to receive these values as bytes.
	 * 
	 * @return The value of the pitch component as a byte.
	 */
	public byte getBytePitch() {
		return (byte) (this.pitch * 256 / 360);
	}

	/**
	 * Sets the value of the pitch component of the orientation to the given value.
	 * 
	 * @param pitch The value to which the pitch component of the orientation will
	 *              be set.
	 */
	public void setPitch(double pitch) {
		if (pitch < 0 || pitch >= 360) {
			pitch %= 360;

			if (pitch < 0) {
				pitch += 360;
			}
		}

		this.pitch = pitch;
	}

	/**
	 * Sets the value of the yaw component of the orientation from the given byte
	 * value. The byte value will be converted into degrees. This method is useful
	 * for receiving an orientation from the player via network, because the
	 * Minecraft Classic protocol defines that the orientation of the player has to
	 * be sent as a byte.
	 * 
	 * @param yaw The yaw value of the orientation as a byte.
	 */
	public void setBytePitch(byte pitch) {
		this.setPitch((double) pitch * 360 / 256);
	}
}
