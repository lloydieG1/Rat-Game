import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


/**
 * Class used for construction and behaviours of Steriliser elements.
 *
 * @author William Randle
 * @author Jack Lennard,
 */
public class Sterilise extends Element {

    protected int sterilisedRats;

    private Image image;
    private Image aura;

    public static final int MAX_HEALTH = 4;

    /**
     * Description.
     *
     * @param type
     * @param level
     * @param x
     * @param y
     * @param health
     */
    public Sterilise(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);
        // TODO Auto-generated constructor stub
        image = ImageLoader.sterilize;

        aura = ImageLoader.sterilizeAura;
        sterilisedRats = 0;
    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically
     * in time with other elements.
     */
    protected void tick() {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
            System.out.println("deleting sterilise");
        }
        sterilise();
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
    }

    /**
     * The logic of the Element.
     */
    private void logic() {


        if (health <= 0) {
            level.removeElement(this);
        }
        health--;
    }

    public void sterilise() {
        int radius = 1;
        for (int x = this.x - radius; x <= this.x + radius; x++) {
            for (int y = this.y - radius; y <= this.y + radius; y++) {
                for (Element element : level.getElements(x, y)) {
                    if (element.getType().equals(ElementType.Rat)) {
                        Rat rat = (Rat) element;
                        if (!rat.isSterile()) {
                            rat.makeSterile();
                        }

                    }
                }
            }
        }
    }


    /**
     * Renders a Sterilise sign.
     *
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        g.save();
        g.setGlobalAlpha(currentTick * 1.0 / tickSpeed * 1.0);
        g.drawImage(aura, x - (size / Math.cos(currentTick * 1.0 / tickSpeed * 1.0)) / 2 + size / 2,
                y - (size / Math.cos(currentTick * 1.0 / tickSpeed * 1.0)) / 2 + size / 2,
                size / Math.cos(currentTick * 1.0 / tickSpeed * 1.0), size /
                        Math.cos(currentTick * 1.0 / tickSpeed * 1.0));
        g.restore();
        g.drawImage(image, x, y, size, size);

    }
}
