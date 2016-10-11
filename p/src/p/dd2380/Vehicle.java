package p.dd2380;

import java.io.Serializable;

/**
 * 
 * @author yimingf
 *
 * a simplest car with some interfaces with outside the world and the Network
 * 
 */

public class Vehicle implements Comparable<Vehicle>, Serializable {
	// the unique number
	private int number;
	// the max speed a vehicle could achieve
	private double maxSpeed;
	// the current speed
	private double currentSpeed;
	// the acceleration (how much speed could change during one timestamp)
	private double acceleration;
	// the track the vehicle is on
	private int track;
	// how long the car has run
	private double dist;
	
	Vehicle() {}
	Vehicle(int number, double maxSpeed, double acceleration, int track, double dist) {
		this.number = number;
		this.maxSpeed = maxSpeed;
		this.currentSpeed = maxSpeed;
		this.track = track;
		this.dist = dist;
	}
	
	// getters
	public int getNumber() {
		return this.number;
	}
	
	public double getCurrentSpeed() {
		return this.currentSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getTrack() {
		return this.track;
	}
	
	public double getDist() {
		return this.dist;
	}
	
	public void makeAction(int actionType) {
		switch (actionType) {
		case 0:
			this.run();
			break;
        case 1:
        	this.accelerate();
            break;
        case 2:
        	this.decelerate();
        	break;
        case 3:
        	this.turn(false); // turn left
            break;
        case 4:
        	this.turn(true); // turn right
            break;
        default:
        	System.out.println("*** invalid action type ***");
            break;
	    }
	}
	
	private void run() {
		this.dist += this.currentSpeed;
	}
	
	private void accelerate() {
		// accelerate when safety is guaranteed
		// safety is detected by the network
		// cannot accelerate over max speed
		if (this.currentSpeed + this.acceleration >= this.maxSpeed) {
			this.currentSpeed = this.maxSpeed;
		} else {
			this.currentSpeed += this.acceleration;
		}
		
		this.run();
	}
	
	private void decelerate() {
		// decelerate
		if (this.currentSpeed - this.acceleration <= 0.0) {
			this.currentSpeed = 0.0;
		} else {
			this.currentSpeed -= this.acceleration;
		}
		
		this.run();
	}
	
	private void turn(boolean isTurnRight) {
		// if isTurnRight = true turn to the right track
		// otherwise turn to the left track
		// turning is accompanied w/deceleration for compulsory safety
		// if at right/left edge of track cannot turn right/left (detected by the Road)
		if (isTurnRight) {
			this.track++;
		} else {
			this.track--;
		}
		
		this.decelerate();
	}
	
	public int compareTo(Vehicle other) {
    	return Double.compare(other.dist, dist);
    }
	
}
