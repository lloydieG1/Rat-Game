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
    protected double factor = Game.gameSize;

    protected int nextX;
    protected int nextY;
    protected Direction lastDir = Direction.North;

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

    public boolean isVisible(GraphicsContext g) {
        double visibleTiles = Game.VISIBLE_TILES+1;
        boolean visible = true;
        if (renderY() < 0) {
            visible = false;
        }
        if (renderX() < 0) {
            visible = false;
        }
        if (renderX() > g.getCanvas().getWidth()) {
            visible = false;
        }
        if (renderY() > g.getCanvas().getHeight()) {
            visible = false;
        }
        return visible;
    }


    /**
     * handles the movement of the rat
     */
    protected void movement() {
        lastDir = dir;



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
        
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.StopSign)) {
            	StopSign stopsign = (StopSign) element;
                stopsign.blocksUp();
                return false;
            }
        }
        
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.StopSign) && (this.getType().equals(ElementType.Rat))) {
            	Sterilise sterilise = (Sterilise) element;
                sterilise.steriliseRat((Rat) this);
                return false;
            }
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

    /**
     * gets the rendered version of x
     * @return
     */
    protected double renderX() {
        double x = this.x*factor + Game.gameX;
        double nextX = this.nextX*factor+ Game.gameX;

        return interpolate(x, nextX);
    }

    /**
     * smoothly interpolates rotations
     * @param x1
     * @param x2
     * @return
     */
    protected double interpolateDir(double x1, double x2) {
        //makes sure the rat never rotates more than 180 for a turn:
        if (x2-x1 > 180) {
            x2 = x2-360;
        } else if (x2-x1 < -180){
            x2 = x2+360;
        }

        double currentTick = minMax(this.currentTick, 0, tickSpeed/2);
        double tickSpeed = this.tickSpeed/2.0;
        return cosineInterpolation(x1, x2, currentTick, tickSpeed);
    }

    protected double cosineInterpolation(double x1, double x2, double min, double max) {

        double pi = 3.14159265;
        double m2 = (1-Math.cos(min*pi/max))/2;
        return x1+(x2-x1)*m2;
    }

    /**
     * prevents a value being above or below parsed ints
     * @param var
     * @param min
     * @param max
     * @return
     */
    public int minMax(int var, int min, int max) {
        if(var >= max)
            return max;
        else if (var <=min)
            return min;
        else
            return var;

    }

    /**
     * gives a number instead of a Direction value
     * @param dir
     * @return
     */
    protected double dirAsNum(Direction dir) {
        if (dir == Direction.North) {
            return(180);
        } else if (dir == Direction.East) {
            return (90);
        }else if (dir == Direction.South) {
            return(0);
        }else if (dir == Direction.West) {
            return(270);
        }
        return 0;
    }

    /**
     * gets the rendered version of y
     * @return
     */
    protected double renderY() {
        double y = this.y*factor+ Game.gameY;
        double nextY = this.nextY*factor+ Game.gameY;

        return interpolate(y, nextY);
    }


}
