import javafx.scene.canvas.GraphicsContext;

/**
 * elements are things in the map which can move, like rat
 * @author william randle
 */
public abstract class Element {
    protected int x; //x position in the map
    protected int y; //y position in the map
    protected ElementType type; //the type of element
    protected int tickSpeed = Game.fps/2; //means logic only happens once this many ticks have passed
    protected int currentTick =0; //the current tick in the cycle (eg 3/(maxfps))
    protected Level level; //the level the element is in so it can use getElements
    protected int age =0; //age of the element in ticks
    protected Direction dir;

    protected boolean flagRemoval = false; //determines if level should remove this element at the end of the tick

    protected double size = Game.gameSize;

    protected double factor = Game.factor;

    /**
     * constructs an element with x,y, level it is in and type.
     * @param type
     * @param level
     * @param x
     * @param y
     */
    public Element(ElementType type, Level level, int x, int y) {
        this.y = y;
        this.x = x;
        this.type = type;
        this.level = level;
    }

    /**
     * returns the type of element
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
     * executes elements logic
     */
    protected abstract void tick();

    /**
     * draws the element on the canvas
     * @param g
     */
    protected abstract void render(GraphicsContext g);

    /**
     *
     * @return if the element needs to be removed from the level at the end of the tick.
     */
    public boolean isFlagRemoval() {

        return isFlagRemoval();
    }

}
