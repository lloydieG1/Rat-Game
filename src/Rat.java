import javafx.scene.canvas.GraphicsContext;

/**
 * Holds every necessary rat behaviour
 * @author Mosaed
 *
 */

public class Rat extends Element {
	private boolean isMale;
	private String initialDirection;
	
	/**
	 * Construct a rat with necessary properties
	 * @param xPos X coordinate to spawn a rat
	 * @param yPos Y coordinate to spawn a rat
	 * @param isMale Sex of rat
	 * @param initialDirection Where the rat first direction is
	 */
	public Rat (ElementType type, Level level, int xPos, int yPos, boolean isMale, String initialDirection) {
        super(type, level, xPos, yPos);
		this.isMale = isMale;
		this.initialDirection = initialDirection;
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

	@Override
	protected void tick() {
		// TODO 
		
	}

	@Override
	protected void render(GraphicsContext g) {
		// TODO 
		
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
