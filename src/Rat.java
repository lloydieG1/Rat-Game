import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Holds every necessary rat behaviour
 * @author Mosaed
 *
 */

public class Rat extends Element {
	private boolean isMale;
	private String initialDirection;
	private int age;
	private boolean isPregnant;
	private boolean isSterile;
	
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
		isPregnant = false;
		isSterile = false;
	}

	public boolean getIsMale () {
		return isMale;
	}
	
	public String getDirection () {
		return initialDirection;
	}
	
	public int getAge() {
		return age;
	}
	
	protected void tick() {
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
            }
    }

	
	private void logic() {
        age++;
    }
		
	/**
	 * Make sure all mating conditions between 2 rats are met
	 * Check 2 rats are adults, not sterile of the opposite sex and the female rat is not pregnant
	 * @param rat1 first rat to check sex
	 * @param rat2 second rat to check sex
	 * @return Whether two rats do not have the same sex
	 */
	public boolean canMate (Rat rat1, Rat rat2) {
		
		if (rat1.age > 2 && rat2.age > 2) {
			if (!rat1.isSterile && !rat2.isSterile) {
				if(rat1.isMale && !rat2.isMale && !rat2.isPregnant) {
					rat2.isPregnant = true;
					giveBirth(rat2);
					return true;
				} else if (!rat1.isMale && rat2.isMale && !rat1.isPregnant) {
					rat1.isPregnant = true;
					giveBirth(rat1);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	public void giveBirth(Rat rat) {
		//TODO After 5 sec change rat isPregnant to false and make baby rats
		rat.isPregnant = false;
	}


    protected void render(GraphicsContext g) {
        double factor = 12;
        double x = this.x*factor;
        double y = this.y*factor;
        int size = Game.gameSize/2;
        g.setFill(Color.RED);
        g.fillRect(x, y, size, size);


    }
	
}
