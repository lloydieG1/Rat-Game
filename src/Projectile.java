import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of the Gas element.
 *
 * @author Yazan Adi
 * @author William Randle
 */
public class Projectile extends Element {
    private boolean damage;
    private Image image;
    public static final int MAX_HEALTH = 2;
    private double velx;
    private double vely;

    /**
     * projectile constructor.
     * @param type ElementType the type of element this is
     * @param level Level the level the gas is in
     * @param x int x the x position of the gas
     * @param y int y the y position of the gas
     */
    public Projectile(ElementType type, Level level, int x, int y, int health, double velx, double vely) {
        super(type, level, x, y, Direction.North, health);
        this.health = health;
        damage = false;
        image = ImageLoader.gas;
        tickSpeed = (int) (Game.FPS / 1.2);
        this.velx = velx;
        this.vely = vely;

    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically
     * in time with other elements.
     */
    protected void tick() {
        x+=velx/Game.FPS;
        y+=vely/Game.FPS;

        System.out.println(velx);
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
        if (health <= -MAX_HEALTH) {
            level.removeElement(this);
        }
    }

    /**
     * The logic of the rat, only run at the rat's slower speed.
     */
    private void logic() {

        health--;
    }
    /**
     * Gives the x position for where the element should be rendered on screen.
     * @return The rendered version of X.
     */
    public double renderX() {
        double x = (int) (this.x * Game.gameSize + Game.gameX);

        return x;
    }

    /**
     * Gives the y position for where the element should be rendered on screen.
     * @return The rendered version of Y.
     */
    public double renderY() {
        double y = (int) (this.y * Game.gameSize + Game.gameY);

        return y;

    }


    /**
     * Get Directions.
     * @return Directions
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * Get Age.
     * @return Age
     */
    public int getAge() {
        return age;
    }


    /**
     * Renders gas.
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();

        //calculating the position the rat should be in this frame
        g.drawImage(image, x, y, size, size);
    }
}