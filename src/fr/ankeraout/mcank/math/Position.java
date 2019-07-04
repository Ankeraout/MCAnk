package fr.ankeraout.mcank.math;

public class Position {
	private double x;
	private double y;
	private double z;

	public Position() {
		this(0, 0, 0);
	}

	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}
	
	public short getShortX() {
		return (short)(this.x * 32);
	}
	
	public short getShortY() {
		return (short)(this.y * 32);
	}
	
	public short getShortZ() {
		return (short)(this.z * 32);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public void setShortX(short x) {
		this.x = x / 32.0;
	}
	
	public void setShortY(short y) {
		this.y = y / 32.0;
	}
	
	public void setShortZ(short z) {
		this.z = z / 32.0;
	}

	public Position add(Position vector) {
		return new Position(this.x + vector.x, this.y + vector.y, this.z + vector.z);
	}

	public Position subtract(Position vector) {
		return new Position(this.x - vector.x, this.y - vector.y, this.z - vector.z);
	}
	
	public double distance(Position vector) {
		double dx = this.x - vector.x;
		double dy = this.y - vector.y;
		double dz = this.z - vector.z;
		
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
