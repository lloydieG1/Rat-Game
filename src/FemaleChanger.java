import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

/**
 * This class constructs Female Sex Change item and implements all its features.
 *
 * @author Adrian
 * @version 2.0
 */
public class FemaleChanger extends Element {
    private Image image;
    private int femaleChangeCount;

    /**
     * Create Female Sex Changer.
     * @param type   Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     */
    public FemaleChanger(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);
        // TODO Auto-generated constructor stub
        femaleChangeCount = 0;
        image = ImageLoader.getImage("femaleGenderChange.png", 64);
    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically in time
     * with other elements.
     */
    @Override
    protected void tick() {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
            System.out.println("deleting female changer");
        }
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
        changeSex();
        if (this.femaleChangeCount == 1) {
            level.removeElement(this);
        }
    }

    /**
     * Change the sex of a rat if Female Gender Item shares a tile with a male rat. 
     */
    private void changeSex() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                Rat rat = (Rat) element;
                if (rat.getIsMale()) {
                    rat.setIsMale(false);

                }
                femaleChangeCount++;
            }
        }
    }

    /**
     * Renders a Male Changer sign.
     * @param g graphics context
     */
    @Override
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        g.drawImage(image, x, y, size, size);
    }
}
