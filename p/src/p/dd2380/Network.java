package p.dd2380;

import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author yimingf
 *
 * maybe the most important part of the game
 * I will put AI interface inside this class
 * the Network will get the location of the vehicles and then find a better solution.
 * private
 * ========
 * 
 * action number is interpreted in Road as:
 * --- without changingprivate track (meaning original track needs to be checked) ---
 * 0 	-> simply run, do nothing
 * 1 	-> accelerate
 * 2 	-> decelerate
 * --- with changing track (meaning the other track needs to be checked) ---
 * 3 	-> turn left (with decelerate) self.track--
 * 4 	-> turn right (with decelerate) self.track++
 * 
 * ========
 * 
 * action type is interpreted as: (subject to change)
 * 0 	-> baby strategy (do nothing)
 * 1 	-> safety-first (decelerate when possible danger detected)
 * 2 	-> aggressive (accelerate most and ignore some small dangers)
 * 
 */

public class Network {
	public static double SAFETY_RATE = 5.0;
	public static double SAFETY_DIST = 100.0;
	public static double WARNING_DIST = 50.0;
	
	public Action makeStrategy(HashMap<Integer, Vector<Vehicle>> vehicles, int actionType) {
		Action a = new Action();
		double MOST_SAFE_DIST = SAFETY_RATE * SAFETY_DIST;
		
		if (actionType == Main.ACTION_SAFETYFIRST) {
			for (int i = 0; i < vehicles.size(); i++) {
				Vector<Vehicle> vs = vehicles.get(i);
				for (int j = 0; j < vs.size(); j++) {
					Vehicle v = vs.get(j);
					double foo = v.getDist();
					if (j == vs.size()-1) { // last element, keep on accelerating
						a.putAction(v.getNumber(), 1);
						continue;
					}
					double bar = vs.get(j+1).getDist();
					double baaz = bar - foo; // distance diff.
					System.out.println("diff: " + baaz);
					
					if (baaz > MOST_SAFE_DIST) { // safe then accelerate
						a.putAction(v.getNumber(), 1);
					} else if (baaz > SAFETY_DIST) { // run as usual
						a.putAction(v.getNumber(), 0);
					} else if (baaz > WARNING_DIST) { // decelerate
						a.putAction(v.getNumber(), 2);
					} else { // most dangerous situation. need turning
						int baz = whichEdge(v);
						if (baz == 1) { // only right
							a.putAction(v.getNumber(), 4);
						} else if (baz == 2) { // only left
							a.putAction(v.getNumber(), 3);
						} else { // a naive determinating method
							a.putAction(v.getNumber(), (vehicles.get(i+1).size() > vehicles.get(i-1).size()) ? 3 : 4);
						}
					}
				}
			}
		} else if (actionType == Main.ACTION_BABY) {
			for (int i = 0; i < vehicles.size(); i++) {
				Vector<Vehicle> vs = vehicles.get(i);
				for (Vehicle v : vs) {
					a.putAction(v.getNumber(), 0);
				}
			}
		}

		System.out.println("action size: " + a.getSize());
		return a;
	}
	
	private int whichEdge (Vehicle v) {
		int foo = v.getTrack();
		if (foo == 0) {
			return 1; // cannot turn left
		} else if (foo == Main.NUMBER_OF_TRACKS-1) {
			return 2; // cannot turn right
		} else {
			return 0; // can turn either way
		}
	}
}
