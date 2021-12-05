import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * This class implements the explosion the Bomb item.
 * @author William randle
 */
public class Explosion extends Element {

    int maxHealth = 5;
    private Image blast;
    private boolean isCenter;


    /**
     * Create the explosion of bomb item.
     * @param type   Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     */
    public Explosion(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);

        if (isCenter) {
            blast = ImageLoader.getImage("blastCenter.png", 32);
        } else {
            blast = ImageLoader.getImage("blast.png", 32);
        }
        tickSpeed = Game.FPS / 15;
        lastDir = dir;
        this.isCenter = isCenter;
    }

    protected void tick() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                Rat rat = (Rat) element;
                killRat(rat);
            }
            if (!element.getType().equals(ElementType.Explosion))
                level.removeElement(element);
        }
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

    }


    private void logic() {
        health--;
        if (health <= 0) {
            level.removeElement(this);
        }
    }

    /**
     * Render the explosion.
     */
    protected void render(GraphicsContext g) {
        double size = Game.gameSize;

        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();

        //calculating the position the rat should be in this frame
        g.setFill(Color.color(1 - (health * 0.5 / maxHealth), (health * 0.9 / maxHealth), 0));
        g.fillRect(x, y, size, size);
    }
}
