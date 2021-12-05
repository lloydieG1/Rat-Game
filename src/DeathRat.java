import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class constructs a death rat item and implements all its features.
 * @author Adrian
 * @version 1.0
 */
public class DeathRat extends Element {

    public static final int MAX_HEALTH = 5;
    private static final int KILLING_AGE = 5;
    private int ratsKilled;
    private Image image;


    /**
     * Create the death rat element.
     * @param type   Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     * @param dir    Initial direction of movement.
     */
    public DeathRat(ElementType type, Level level, int x, int y, int health, Direction dir) {
        super(type, level, x, y, Direction.North, health);
        this.dir = dir;
        ratsKilled = 0;
        image = ImageLoader.getImage("deathRat.png", 64);
    }

    /**
     * Run the logic of a death rat.
     */
    private void logic() {
        age++;
        if (age > KILLING_AGE) {
            movement();
            checkRat();
        }
    }

    /**
     * If death rat shares a tile with a rat, kill that rat and 
     * remove this death rat from the level.
     */
    public void checkRat() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                if (element.getType().equals(ElementType.Rat)) {
                    Rat rat = (Rat) element;
                    killRat(rat);
                }
                if (health <= 0) {
                    level.removeElement(this);
                }
            }
        }
    }


    @Override
    protected void killRat(Rat rat) {
        if (rat.getIsPregnant()) {
            level.removeElement(rat);
            health = health - 1 - rat.getPregnantTime();
            ;
            Game.score = Game.score + 10 + 10 * rat.getPregnantTime();
        } else {
            level.removeElement(rat);
            health--;
            Game.score = Game.score + 10;
        }
    }

    @Override
    protected void tick() {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
        }
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
        if (age > KILLING_AGE) {
            checkRat();
        }
    }

    /**
     * Get the direction.
     * @return Initial direction of movement.
     */
    public Direction getDirection() {
        return dir;
    }

    @Override
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();

        //Calculating the position the rat should be in this frame.
        g.save();
        g.translate(x + Game.gameSize / 2.0, y + Game.gameSize / 2.0);
        g.rotate(interpolateDir(dirAsNum(lastDir), dirAsNum(dir)));
        g.drawImage(image, -(size / 2), -(size / 2), size, size);
        g.restore();
    }
}
