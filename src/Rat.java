/**
 * Holds every necessary rat behaviour
 * @author Mosaed
 *
 */

public class Rat extends Element {
	private int xPos;
	private int yPos;
	private boolean isMale;
	private String initialDirection;
	
	/**
	 * Construct a rat with necessary properties
	 * @param xPos X coordinate to spawn a rat
	 * @param yPos Y coordinate to spawn a rat
	 * @param isMale Sex of rat
	 * @param initialDirection Where the rat first direction is
	 */
	public Rat (int xPos, int yPos, boolean isMale, String initialDirection) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.isMale = isMale;
		this.initialDirection = initialDirection;
	}
	
	public int getXPos () {
		return xPos;
	}
	
	public int getYPos () {
		return yPos;
	}
	
	public boolean getIsMale () {
		return isMale;
	}
	
	public String getDirection () {
		return initialDirection;
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
