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
