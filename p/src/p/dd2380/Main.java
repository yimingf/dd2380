package p.dd2380;

/**
 * 
 * @author yimingf
 *
 * the main class of the project.
 * at every timestamp the system calls the Network to control those cars
 * after controlling cars attempt their moves
 * if crashing happens report it with Monitor and terminate the program
 * otherwise continue until preset timestamp has run out
 * which means the AI has successfully passed the test
 * 
 */

public class Main {
	
	private static int NUMBER_OF_TRACKS = 3;
	private static int TOTAL_DISTANCE = 2000;
	private static int TIMESTAMP = 10;
	
	private static int ACTION_NORMAL = 0;
	private static int ACTION_SAFETYFIRST = 1;
	private static int ACTION_AGGRESSIVE = 2;
	
	public static void main(String[] args) {
		Road r = new Road(NUMBER_OF_TRACKS, TOTAL_DISTANCE);
		
		// addVehicle(int number, double maxSpeed, double acceleration, int track, double dist)
		r.addVehicle(0, 200, 10, 0, 400);
		r.addVehicle(1, 300, 20, 0, 0);
		r.addVehicle(2, 300, 20, 1, 200);
		r.addVehicle(3, 300, 20, 2, 0);
		r.addVehicle(4, 300, 20, 2, 400);
		
		for (int i = 0; i < TIMESTAMP; i++) {
			if (r.makeStrategy(ACTION_NORMAL)) { // no crash happens
				r.monitorOutput(i);
			} else {
				break;
			}
		}
	}
}
