import java.util.ArrayList;

/**
 * Holds every necessary rat behaviour
 * @author Mosaed
 *
 */

public class Rat {
	private int x;
	private int y;
	private boolean isMale;
	private String initialDirection;
	static ArrayList <Rat> rats = new ArrayList <Rat>();
	
	/**
	 * Construct a rat with necessary properties
	 * @param x X coordinate to spawn a rat
	 * @param y Y coordinate to spawn a rat
	 * @param isMale Sex of rat
	 * @param initialDirection Where the rat first direction is
	 */
	public Rat (int x, int y, boolean isMale, String initialDirection) {
		this.x = x;
		this.y = y;
		this.isMale = isMale;
		this.initialDirection = initialDirection;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public boolean getIsMale () {
		return isMale;
	}
	
	public String getInitialDirection () {
		return initialDirection;
	}
	
	/**
	 * Create a rat
	 * @param x X coordinate to spawn a rat
	 * @param y Y coordinate to spawn a rat
	 * @param isMale Sex of rat
	 * @param initialDirection Where the rat first direction is
	 * @return A rat object
	 */
	public static Rat createRat (int x, int y, boolean isMale, String initialDirection) {
		 Rat rat = new Rat(x, y, isMale, initialDirection);
		 return rat;
	}
	
	/**
	 * Add a rat to rats array list
	 * @param The rat to add to rats array list
	 */
	public static void addRat (Rat rat) {
		rats.add(rat);
	}
	
	/**
	 * Check if 2 rats are of the opposite sex
	 * @param rat1 first rat to check sex
	 * @param rat2 second rat to check sex
	 * @return Whether two rats do not have the same sex
	 */
	public boolean canMate (Rat rat1, Rat rat2) {
		return rat1.isMale && !rat2.isMale;
	}
	
	
	
	// For testing
//	public static void main (String args[]) {
//		addRat(createRat(1, 3, true, "South"));
//		addRat(createRat(2, 3, false, "Nouth"));
//		
//		for (int i=0; i < rats.size(); i++) {
//			System.out.println(rats.get(i).getX());
//		}
//	}
	
}
