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
        age++;
        if (age == ADULTHOOD) {
            grow();
        }
        if(! isChild) {
            if (isMale == false) {
                breed();
            }
        }

        movement();
        if(! isChild) {
            if (isMale == false) {
                breed();
            }
        }

        System.out.println(x + " " + y);
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
                }
            }
        }
    }


    /**
     * handles the movement of the rat
     */
    private void movement() {
        //update to the position decided last tick.

        int checkX = 0;
        int checkY = 0;

        int randomNum = Game.random.nextInt(4);

        // changes direction until nolonger stuck
        while (isStuck()) {
            dir = rightDir(dir);
        }

        if (randomNum == 0) {
            checkX = x;
            checkY = y +1;
        } else if (randomNum == 1) {
            checkX = x+1;
            checkY = y;
        }else if (randomNum == 2) {
            checkX = x;
            checkY = y-1;
        }else if (randomNum == 3) {
            checkX = x-1;
            checkY = y;
        }

        while (!(isTile(checkX, checkY))) {
            randomNum = Game.random.nextInt(4);

            if (randomNum == 0) {
                checkX = x;
                checkY = y +1;
            } else if (randomNum == 1) {
                checkX = x+1;
                checkY = y;
            }else if (randomNum == 2) {
                checkX = x;
                checkY = y-1;
            }else if (randomNum == 3) {
                checkX = x-1;
                checkY = y;
            }
        }

        if (checkY == y+1) {
            dir = Direction.North;
        } else if (checkY == y-1) {
            dir = Direction.South;
        }else if (checkX == x+1) {
            dir = Direction.East;
        }else if (checkX == x-1) {
            dir = Direction.West;
        }

        this.x = checkX;
        this.y = checkY;


    }




    /**
     *  checks if the tile at parsed position is a eligable tile to go onto
     *
     * @return boolean if tile is safe
     */
    private boolean isTile(int x, int y) {
        int boardSize = level.getLength();

        if (x < 0 || y < 0) {
            return false;
        }

        if (x > boardSize-1 || y > boardSize-1) {
            return false;
        }

        if (dir.equals(Direction.North) && y == this.y-1 ) {
            return false;
        } else if (dir.equals(Direction.East) && x == this.x-1 ) {
            return false;
        }else if (dir.equals(Direction.South) && y == this.y+1 ) {
            return false;
        }else if (dir.equals(Direction.West) && x == this.x+1 ) {
            return false;
        }

        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            return false;
        }


        return true;
    }





    /**
     * returns true if the rat cannot move to a nearby tile
     * @return
     */
    private boolean isStuck() {
        int checkX= x;
        int checkY = y;
        int totalPaths = 0; //if paths accesable is < 2 then stuck

        if(isTile(checkX, checkY+1)) {
            totalPaths++;
        }

        if(isTile(checkX+1, checkY)) {
            totalPaths++;
        }

        if(isTile(checkX, checkY-1)) {
            totalPaths++;
        }

        if(isTile(checkX-1, checkY)) {
            totalPaths++;
        }

        return totalPaths<1;




    }

    //returns dir to the right
    private Direction rightDir(Direction dir) {
        if (dir.equals(Direction.North)) {
            return Direction.East;
        } else if (dir.equals(Direction.East)) {
            return Direction.South;
        } else if (dir.equals(Direction.South)) {
            return Direction.West;
        } else {
            return Direction.North;
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
