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
	
	public void report(HashMap<Integer, Vector<Vehicle>> vehicles, int timestamp, boolean ifCrash) {
		String foo = "--- [time] at timestamp " + timestamp + " ---\n";
		if (ifCrash) {
			foo += "some crash happened and EVERYTHING IS DOOMED.\n";
			foo += "we... are... DESPAIR... now...\n";
		} else {
			foo += "everything seems normal.\n";
		}
		
		
		for (int i = 0; i < vehicles.size(); i++) {
			foo += "--- at track " + i + ": ---\n";
			Vector<Vehicle> vs = vehicles.get(i);
			for (Vehicle v : vs) {
				foo += "vehicle id: " + v.getNumber() + "/ ";
				foo += "speed: " + v.getCurrentSpeed() + "/ ";
				foo += "dist: " + v.getDist() + "\n";
			}
		}
		
		System.out.println(foo);
	}
	
	public void reportCrash(int timestamp, int track, int id) {
		String foo = "--- [crash] at timestamp " + timestamp + " ---\n";
		foo += "oooooooooooooooooooooooooooops!!!!!!!!!!!\n";
		foo += "now track " + track + " IS JUST A PIECE OF SHIT!!!!!\n";
		foo += "driver " + id + ": I FEEEEEEEEEEEL LIKE DYING!! walaRAOLEFIEOFEOIFANFOI\n";
		foo += "WHOEVER DESIGNED THIS F**KING SYSTEM";
		
		System.out.println(foo);
	}
	
	public void reportSuccess(int timestamp) {
		String foo = "--- [succ] congratulations!\n";
		foo += "you made it after " + timestamp + " timestamps!!\n";
		foo += "but our princess BEACH is in another castle!\n";
		foo += "would you like to pick up another pieces of shit?\n";
		
		System.out.println(foo);
	}

}
