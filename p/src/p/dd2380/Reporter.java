package p.dd2380;

import java.util.*;

/**
 * reporting system variables into the terminal screen.
 * @author yimingf
 *
 */

public class Reporter {
	public void report() { // initialization
		String foo = "--- [init] welcome to the car world! ---";
		System.out.println(foo);
	}
	
	public void report(int id, double maxSpeed, double acceleration, int track, double dist) {
		String foo = "--- [nvec] new vehicle added ---\n";
		foo += "id: " + id + "\n";
		foo += "maxSpeed: " + maxSpeed + "\n";
		foo += "acceleration: " + acceleration + "\n";
		foo += "track: " + track + "\n";
		foo += "dist: " + dist + "\n";
		System.out.println(foo);
	}
	
	public void report(HashMap<Integer, Vector<Vehicle>> vehicles, int timestamp) {
		String foo = "--- [time] at timestamp " + timestamp + " ---\n";
		foo += "everything seems normal.\n";
		
		for (int i = 0; i < vehicles.size(); i++) {
			foo += "--- at track " + i + ": ---\n";
			Vector<Vehicle> vs = vehicles.get(i);
			for (Vehicle v : vs) {
				foo += "vehicle id: " + v.getNumber() + "/";
				foo += "speed: " + v.getCurrentSpeed() + "/";
				foo += "dist: " + v.getDist() + "\n";
			}
		}
		
		System.out.println(foo);
	}
	
	public void reportCrash(int timestamp, Vehicle bar, Vehicle baz) {
		String foo = "--- [crash] at timestamp " + timestamp + " ---\n";
		foo += "oooooooooooooooooooooooooooops!!!!!!!!!!!\n";
		foo += "now track " + bar.getTrack() + " IS JUST A PIECE OF SHIT!!!!!\n";
		foo += "driver " + bar.getNumber() + ": I FEEEEEEEEEEEL LIKE DYING!! walaRAOLEFIEOFEOIFANFOI\n";
		foo += "driver " + baz.getNumber() + ": O I'M BURNING! I FEEEEEL LIKE HELL!!!!\n";
	}
}
