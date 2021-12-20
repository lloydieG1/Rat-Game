import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class constructs Poison item and implements all its features.
 * @author William Randle
 * @author Tafon Taffe
 * @author Adrian Zabica
 */
public class Poison extends Element {

    private Image image;
    public static final int MAX_HEALTH = 1;

    /**
     * Create the poison element.
     * @param type	 Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     */
    public Poison(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);
        
        image = ImageLoader.getImage("poison.png", 64);
    }

    private void logic() {
        //spawn a projectile entity whicih moves until it hits a rat
    	
    }

    /**
     * If poison shares a tile with a rat, kill that rat and remove 
     * this poison from the level.
     */
    public void checkRat() {
        boolean alreadyPoisoned = false;
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat) && !alreadyPoisoned) {
                Rat rat = (Rat) element;
                killRat(rat);
                alreadyPoisoned = true;
                level.removeElement(this);
            }
        }
    }

    @Override
    protected void killRat(Rat rat) {
        if (rat.getIsPregnant()) {
            level.removeElement(rat);
            health = health - 1 - rat.getPregnantTime();
            Game.score = Game.score + 10 + 10 * rat.getPregnantTime();

        } else {
            level.removeElement(rat);
            health--;
            Game.score = Game.score + 10;
        }

    }


    @Override
    protected void tick() {
        if (level.getTile(x, y).getType().equals(TileType.Path)) {
            level.removeElement(this);
            System.out.println("deleting poison");
        }
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
    }

    /**
     * renders a poison element
     * @param g The Graphics.
     */
    @Override
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        g.save();
        g.translate(x + Game.gameSize / 2.0, y + Game.gameSize / 2.0);
        g.rotate(interpolateDir(dirAsNum(lastDir), dirAsNum(dir)));
        g.drawImage(image, -(size / 2), -(size / 2), size, size);
        g.restore();

    }
}
