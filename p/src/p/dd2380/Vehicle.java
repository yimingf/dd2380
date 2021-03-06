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
	// initial speed rate
	private static double INIT_SPEED_RATE = 0.5;
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
	
	Vehicle(int number, double maxSpeed, double acceleration, int track, double dist) {
		this.number = number;
		this.maxSpeed = maxSpeed;
		this.currentSpeed = INIT_SPEED_RATE * maxSpeed;
		this.track = track;
		this.dist = dist;
		this.acceleration = acceleration;
	}
	
	Vehicle(Vehicle v) {
		this.number = v.number;
		this.maxSpeed = v.maxSpeed;
		this.currentSpeed = v.currentSpeed;
		this.track = v.track;
		this.dist = v.dist;
		this.acceleration = v.acceleration;
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
		if (this.currentSpeed + this.acceleration >= this.maxSpeed) {
			this.currentSpeed = this.maxSpeed;
		} else {
			this.currentSpeed += this.acceleration;
		}
		
		this.run();
	}
	
	private void decelerate() {
		if (this.currentSpeed - this.acceleration <= 0.0) {
			this.currentSpeed = 0.0;
		} else {
			this.currentSpeed -= this.acceleration;
		}
		
		this.run();
	}
	
	private void turn(boolean isTurnRight) {
		if (isTurnRight) {
			this.track++;
		} else {
			this.track--;
		}
		
		this.decelerate();
	}
	
	public int compareTo(Vehicle other) {
    	return Double.compare(dist, other.dist);
    }
	
}
