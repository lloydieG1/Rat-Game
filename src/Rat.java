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
	 * Check if 2 rats are the opposite sex
	 * @param rat1 first rat to check sex
	 * @param rat2 second rat to check sex
	 * @return Whether two rats do not have the same sex
	 */
	public boolean canMate (Rat rat1, Rat rat2) {
		return rat1.isMale && !rat2.isMale;
	}
	
//	public static void main(String[] args) {
//		ArrayList <Rat> rats = new ArrayList <Rat>();
//		rats.add(new Rat (1, 2, true, "South"));
//	  }
}
