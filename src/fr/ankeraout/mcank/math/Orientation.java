package fr.ankeraout.mcank.math;

public class Orientation {
	public double yaw;
	public double pitch;
	
	public Orientation() {
		this(0, 0);
	}
	
	public Orientation(double yaw, double pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}
}
