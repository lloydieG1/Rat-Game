import javafx.scene.canvas.GraphicsContext;

/**
 * @author William Randle
 * @version 3
 */

/**
 * elements are objects on the map which can be changed, such as rats or items.
 */
public abstract class Element {
    protected int x; //x position in the map
    protected int y; //y position in the map
    protected ElementType type; //the type of element
    protected int tickSpeed = Game.FPS/2; //means logic only happens once this many ticks have passed
    protected int currentTick = 0; //the current tick in the cycle (eg 3/(maxfps))
    protected Level level; //the level the element is in so it can use getElements
    protected int age = 0; //age of the element in ticks
    protected Direction dir;

    protected boolean flagRemoval = false; 
    //determines if level should remove this element at the end of the tick

    protected double size = Game.gameSize;

    protected double factor = Game.factor;

    protected int nextX;
    protected int nextY;

    /**
     * constructs an element with x,y, level it is in and type.
     * @param type
     * @param level
     * @param x
     * @param y
     */
    public Element(ElementType type, Level level, int x, int y, Direction dir) {
        this.y = y;
        this.x = x;
        this.type = type;
        this.level = level;
        this.dir = dir;

        this.nextX = x;
        this.nextY = y;
    }

    /**
     * returns the type of an element
     * @return
     */
    public ElementType getType() {

        return type;
    }

    /**
     * returns the x position
     * @return int x position in the map
     */
    public int getX() {

        return x;
    }

    /**
     * returns the y position
     * @return int y position in the map
     */
    public int getY() {
        return y;
    }

    /**
     * executes element logics to allow time-based interactions.
     */
    protected abstract void tick();

    /**
     * draws the element on the canvas
     * @param g
     */
    protected abstract void render(GraphicsContext g);

    /**
     * @return if the element needs to be removed from the level at the end of the tick.
     */
    public boolean isFlagRemoval() {

        return isFlagRemoval();
    }


    /**
     * handles the movement of the rat
     */
    protected void movement() {



        x = nextX;
        y = nextY;
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

        this.nextX = checkX;
        this.nextY = checkY;



    }

    /**
     *  checks if the tile at parsed position is a eligable tile to go onto
     *
     * @return boolean if tile is safe
     */
    protected boolean isTile(int x, int y) {
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
    protected boolean isStuck() {
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
    protected Direction rightDir(Direction dir) {
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
     * smoothly interpolates number frames
     * @param x1
     * @param x2
     * @return
     */
    protected double interpolate(double x1, double x2) {

        return x1+((x2-x1)/tickSpeed)*currentTick;
    }

    protected double renderX() {
        double x = this.x*factor;
        double nextX = this.nextX*factor;

        return interpolate(x, nextX);
    }


    protected double renderY() {
        double y = this.y*factor;
        double nextY = this.nextY*factor;

        return interpolate(y, nextY);
    }


}
