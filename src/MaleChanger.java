import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


/**
 * This class constructs Male Sex Change item and implements all its features.
 *
 * @author Adrian.
 * @version 1.0
 */
public class MaleChanger extends Element {

    private Image image;
    private int maleChangeCount;

    /**
     * Create Male Sex Changer item.
     * @param type   Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     */
    public MaleChanger(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);
        
        maleChangeCount = 0;
        image = ImageLoader.getImage("maleGenderChange.png", 64);
    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically
     * in time with other elements.
     */
    @Override
    protected void tick() {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
            System.out.println("deleting male changer");
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
        if (this.maleChangeCount == 1) {
            level.removeElement(this);
        }
    }

    /**
     * Change rat to male if rat was female.
     */
    private void changeSex() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                Rat rat = (Rat) element;
                if (!rat.getIsMale()) {
                    rat.setIsMale(true);
                }
                maleChangeCount++;
            }
        }
    }

    /**
     * Renders a Male Changer sign.
     * @param g GraphicsContext.
     */
    @Override
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        g.drawImage(image, x, y, size, size);
    }
}
