package fr.ankeraout.mcank.math;

/**
 * This class represents a position in a {@link World}. It is represented as 3
 * coordinates of type <code>double</code>: x, y and z.
 * 
 * @author Ankeraout
 *
 */
public class Position {
	/**
	 * The position of on the X axis
	 */
	private double x;

	/**
	 * The position on the Y axis
	 */
	private double y;

	/**
	 * The position on the Z axis
	 */
	private double z;

	/**
	 * Creates a new instance of the {@link Position} class, with coordinates set to
	 * (0, 0, 0).
	 */
	public Position() {
		this(0, 0, 0);
	}

	/**
	 * Creates a new instance of the {@link Position} class, with the given
	 * coordinates.
	 * 
	 * @param x The position on the X axis.
	 * @param y The position on the Y axis.
	 * @param z The position on the Z axis.
	 */
	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the position on the X axis.
	 * 
	 * @return The position on the X axis.
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Returns the position on the Y axis.
	 * 
	 * @return The position on the Y axis.
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Returns the position on the Z axis.
	 * 
	 * @return The position on the Z axis.
	 */
	public double getZ() {
		return this.z;
	}

	/**
	 * Returns the position on the X axis as a <code>short</code>.
	 * 
	 * @return The position on the X axis as a <code>short</code>.
	 */
	public short getShortX() {
		return (short) (this.x * 32);
	}

	/**
	 * Returns the position on the Y axis as a <code>short</code>.
	 * 
	 * @return The position on the Y axis as a <code>short</code>.
	 */
	public short getShortY() {
		return (short) (this.y * 32);
	}

	/**
	 * Returns the position on the Z axis as a <code>short</code>.
	 * 
	 * @return The position on the Z axis as a <code>short</code>.
	 */
	public short getShortZ() {
		return (short) (this.z * 32);
	}

	/**
	 * Sets the X component of the position to the given value.
	 * 
	 * @param x The position value on the X axis.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the Y component of the position to the given value.
	 * 
	 * @param y The position value on the Y axis.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the Z component of the position to the given value.
	 * 
	 * @param z The position value on the Z axis.
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Sets the X component of the position based on the given value of type short.
	 * 
	 * @param x The value to which the position on the X axis will be set.
	 */
	public void setShortX(short x) {
		this.x = x / 32.0;
	}

	/**
	 * Sets the Y component of the position based on the given value of type short.
	 * 
	 * @param y The value to which the position on the Y axis will be set.
	 */
	public void setShortY(short y) {
		this.y = y / 32.0;
	}

	/**
	 * Sets the Z component of the position based on the given value of type short.
	 * 
	 * @param z The value to which the position on the Z axis will be set.
	 */
	public void setShortZ(short z) {
		this.z = z / 32.0;
	}

	/**
	 * Adds up 2 {@link Position} and returns the result as a {@link Position}. None
	 * of the {@link Position} objects are modified.
	 * 
	 * @param vector The {@link Position} to add to this {@link Position}
	 * @return A new instance of the {@link Position} class that contains the result
	 *         of the addition.
	 */
	public Position add(Position vector) {
		return new Position(this.x + vector.x, this.y + vector.y, this.z + vector.z);
	}

	/**
	 * Substracts 2 {@link Position} and returns the result as a {@link Position}.
	 * None of the {@link Position} objects are modified by this method.
	 * 
	 * @param vector The {@link Position} to subtract to this {@link Position}.
	 * @return A new instance of the {@link Position} class that contains the result
	 *         of the subtraction.
	 */
	public Position subtract(Position vector) {
		return new Position(this.x - vector.x, this.y - vector.y, this.z - vector.z);
	}

	/**
	 * Computes the distance between this position and the given position. None of
	 * the {@link Position} objects are modified.
	 * 
	 * @param vector The {@link Position} to which the distance will be calculated.
	 * @return The distance between the 2 positions.
	 */
	public double distance(Position vector) {
		double dx = this.x - vector.x;
		double dy = this.y - vector.y;
		double dz = this.z - vector.z;

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
