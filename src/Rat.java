import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of Rat elements
 * @author William Randle, Mosaed,
 */
public class Rat extends Element {

    private boolean isMale;

    private boolean isPregnant;

    private static int ADULTHOOD = 5;

    private boolean isSterile;

    private boolean isChild;

    private int breeding = 0;
    private final int BREEDING_TIME = 3;



    
    /**
     * Rat constructor
     * 
     * @param type
     * @param level
     * @param x
     * @param y
     * @param isMale
     * @param dir
     */
    public Rat(ElementType type, Level level, int x, int y, boolean isMale, Direction dir) {
        super(type, level, x, y, dir);

        this.isMale = isMale;

        isChild = true;

    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically 
     * in time with other elements
     */
    protected void tick() {
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;

            if (breeding == 0) {
                logic();
            } else {

                breeding--;
            }

            if (breeding < 0) {
                breeding = 0;
            }

        }

    }


    /**
     * the logic of the rat, only ran at the rats slower speed
     */
    private void logic() {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);

        }

        age++;
        if (age == ADULTHOOD) {
            grow();
        }
        movement();
        if(! isChild) {
            if (isMale == false) {
                breed();
            }
        }

    }


    private void grow() {
        isChild = false;
    }

    public boolean getIsMale () {
        return isMale;
    }

    public Direction getDirection () {
        return dir;
    }

    public int getAge() {
        return age;
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

    /**
     * @param rat
     * 
     * method to create more Rats after Breed() method is called
     */
    public void giveBirth(Rat rat) {
        //TODO After 5 sec change rat isPregnant to false and make baby rats
        rat.isPregnant = false;
    }

    private void breed() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                Rat rat = (Rat) element;
                if (rat.isMale == true) {
                    level.addElementLive(new Rat(ElementType.Rat, level, x, y, Game.random.nextBoolean(), Direction.North));
                    breeding = BREEDING_TIME;
                    nextY = y;
                    nextX = x;
                }
            }
        }
    }



    /**
     * renders a rat
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame
        g.setFill(Color.color(0.8,0.4,0.5));
        if (isChild) {
            g.setFill(Color.color(0.5,.5,0.8));
        }

        if (isMale) {
            g.setFill(Color.color(0.2,0.2,0.6));
        }


        g.fillRect(x, y, size/2, size/2);


    }

}
