package p.dd2380;

import java.util.*;

/**
 * 
 * @author yimingf
 *
 * an action has this data type:
 * Hashmap<Integer, Integer>: number -> action number
 * 
 * action number is interpreted in Road as:
 * --- without changing track (meaning original track needs to be checked) ---
 * 0 	-> simply run, do nothing
 * 1 	-> accelerate
 * 2 	-> decelerate
 * --- with changing track (meaning the other track needs to be checked) ---
 * 3 	-> turn left (with decelerate) self.track--
 * 4 	-> turn right (with decelerate) self.track++
 */

public class Action {
	private HashMap<Integer, Integer> actions;
	
	public void putAction(int id, int actionType) {
		this.actions.put(id, actionType);
	}
	
	public int getAction(int id) {
		return this.actions.get(id);
	}
	
	public int getSize() {
		return this.actions.size();
	}
	
	public void printActions() {
		for (int i = 0; i < actions.size(); i++) {
			System.out.println("action number of " + i + ": " + actions.get(i));
		}
	}
	
	Action() {
		this.actions = new HashMap<Integer, Integer>();
	}
}
