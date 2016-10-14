package p.dd2380;

import java.util.*;

/**
 * 
 * @author yimingf
 *
 * record the informations 
 */

public class Road {
	
	// the number of vehicle running in this instance
	private int numVehicles;
	// the number of tracks'
	private int tracks;
	// a vector storing all vehicles
	private HashMap<Integer, Vector<Vehicle>> vehicles;
	// how long the road will be
	private double totalDistance;
	// a monitor to print out messages at every time stamp
	private Reporter r;
	// a network to make strategies
	private Network n;
	// timestamp
	private int timestamp;
	
	Road(int tracks, int totalDistance) {
		this.numVehicles = 0;
		this.tracks = tracks;
		this.vehicles = new HashMap<Integer, Vector<Vehicle>>();
		this.totalDistance = totalDistance;
		this.r = new Reporter();
		this.n = new Network();
		this.timestamp = 0;
		
		for (int i = 0; i < tracks; i++) {
			this.vehicles.put(i, new Vector<Vehicle>()); // every track -> a vector
		}
		this.r.report();
	}
	
	// getters
	public int getNumVehicles() {
		return this.numVehicles;
	}
	
	public int getNumTracks() {
		return this.tracks;
	}
	
	public double getTotalDistance() {
		return this.totalDistance;
	}
	
	// add a new vehicle to the specified track
	public void addVehicle(int number, double maxSpeed, double acceleration, int track, double dist) {
		Vehicle v = new Vehicle(number, maxSpeed, acceleration, track, dist);
		this.vehicles.get(track).add(v);
		
		this.r.report(number, maxSpeed, acceleration, track, dist);
	}
	
	public void addVehicle(Vehicle v, int track) {
		System.out.println("id " + v.getNumber() + " + track: " + track);
		this.vehicles.get(track).add(v);
	}
	
	// sort the vehicle vectors with distance
	public void sortTrack() {
		for (int i = 0; i < tracks; i++) {
			Collections.sort(this.vehicles.get(i));
		}
	}
	
	public boolean isFinished() {
		// for every vehicles if one vehicle has (dist >= totalDistance) return true
		// otherwise return false
		for (int i = 0; i < vehicles.size(); i++) {
			Vector<Vehicle> vs = vehicles.get(i);
			for (Vehicle v : vs) {
				if (v.getDist() >= totalDistance) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean makeStrategy(int actionType) {
		Action a = this.n.makeStrategy(this.vehicles, actionType);
		return this.makeAction(a);
	}
	
	public boolean makeAction(Action a) {
		HashMap<Integer, Vector<Integer>> oldTimestamp = new HashMap<Integer, Vector<Integer>>();
		HashMap<Integer, Integer> tracks = new HashMap<Integer, Integer>();
		HashMap<Integer, Vector<Integer>> newTimestamp = new HashMap<Integer, Vector<Integer>>();
		HashMap<Integer, Boolean> ifDone = new HashMap<Integer, Boolean>();
		
		for (int i = 0; i < this.tracks; i++) {
			oldTimestamp.put(i, new Vector<Integer>()); // every track -> a vector
		}
		for (int i = 0; i < this.tracks; i++) {
			newTimestamp.put(i, new Vector<Integer>()); // every track -> a vector
		}
		
		for (int i = 0; i < vehicles.size(); i++) {
			Vector<Vehicle> vs = vehicles.get(i);
			for (Vehicle v : vs) {
				// track -> ranks in the track
				oldTimestamp.get(i).add(v.getNumber());
			}
		}
		
		
		// make actions
		a.printActions();
		for (int i = 0; i < vehicles.size(); i++) {
			Vector<Vehicle> vs = vehicles.get(i);
			for (int j = 0; j < vs.size(); j++) {
				if (j >= vs.size()) {
					break;
				}
				Vehicle v = vs.get(j);
				if (ifDone.containsKey(v.getNumber()) && ifDone.get(v.getNumber()) == true) {
					continue;
				}
				int track = -1;
				int foo = a.getAction(v.getNumber());
				int bar = v.getTrack();
				if (foo == 3 || foo == 4) { // turning
					track = (foo == 4) ? (bar+1) : (bar-1); // get the new track
					Vehicle nv = this.deleteVehicle(i, v); // delete the old car from old list
					nv.makeAction(foo);
					ifDone.put(nv.getNumber(), true);
					this.addVehicle(nv, track);
				} else {
					track = bar; // no change
					v.makeAction(foo);
					ifDone.put(v.getNumber(), true);
				}
				tracks.put(v.getNumber(), track); // record the new tracks first
			}
		}
		
		// sort before adding into the new timestamp hashmap (ascending order?)
		this.sortTrack();
		for (int i = 0; i < vehicles.size(); i++) {
			Vector<Vehicle> vs = vehicles.get(i);
			for (Vehicle v : vs) {
				newTimestamp.get(i).add(v.getNumber());
			}
		}
		
		if (this.crashIsFound(oldTimestamp, newTimestamp, tracks)) {
			this.monitorOutput(timestamp, true, false);
			return false;
		} else {
			return true;
		}
		
	}
	
	public void updateTime() {
		this.timestamp++;
	}
	
	public void monitorOutput(int timestamp, boolean ifCrash, boolean ifFinished) {
		if (timestamp == Main.TIMESTAMP || ifFinished) {
			this.r.reportSuccess(timestamp);
		} else {
			this.r.report(this.vehicles, timestamp, ifCrash);
		}
	}
	
	private boolean crashIsFound(HashMap<Integer, Vector<Integer>> old, HashMap<Integer, Vector<Integer>> ny, HashMap<Integer, Integer> tracks) {
		for (int i = 0; i < old.size(); i++) {
			Vector<Integer> vehicles = old.get(i);
			for (int j = 0; j < vehicles.size(); j++) {
				int id = vehicles.get(j);
				int track = tracks.get(id);
				
				Vector<Integer> newTrack = ny.get(track);
				List<Integer> front = vehicles.subList(j+1, vehicles.size());
				List<Integer> back = newTrack.subList(0, newTrack.indexOf(id));
				
				front.retainAll(back);
				if (!back.isEmpty() && !front.isEmpty()) {
					this.r.reportCrash(this.timestamp+1, track, id);
					return true;
				}
			}
		}
		return false; // no crash happens
	}
	
	private Vehicle deleteVehicle(int track, Vehicle v) {
		Vehicle nv = new Vehicle(v);
		this.vehicles.get(track).remove(v);
		return nv;
	}

}
