import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * This class constructs a tower which shoots at rats
 * @author William Randle
 */
public class Tower extends Element {

    private Image image;
    public static final int MAX_HEALTH = 1;

    public static final int RADIUS = 2;

    private Rat closestRat;
    private double distX = 1;
    private double distY = 1;

    private static final int MAX_VALUE = 100000;

    /**
     * Create the poison element.
     * @param type	 Element type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The y coordinate position.
     * @param health Remaining health.
     */
    public Tower(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);

        image = ImageLoader.getImage("tower.png", 64);
    }

    private void logic() {
        if (!(closestRat.getX() == getX() && closestRat.getY() == getY())) {
            level.addElementLive(new Projectile(ElementType.Projectile, level, getX(), getY(), 3, getDistX()/2, getDistY()/2));
        }
    }

    private double getDistance(double distX, double distY) {
        return Math.sqrt(distX*distX + distY*distY);
    }

    private double getDistX() {
        return closestRat.getX()-x;

    }

    private double getDistY() {
        return closestRat.getY()-y;

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
        scanRats();
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
    }

    /**
     * scans rats in the surrounding radius and chooses the closest one
     */
    private void scanRats() {
        closestRat = new Rat(ElementType.Rat, level, getX(), getY(), true, Direction.North, 4, false);
        double distance = MAX_VALUE;

        int radius = RADIUS;
        for (int x = getX() - radius; x <= this.x + radius; x++) {
            for (int y = getY() - radius; y <= this.y + radius; y++) {
                for (Element element : level.getElements(x, y)) {
                    if (element.getType().equals(ElementType.Rat)) {
                        Rat rat = (Rat) element;
                        double dist1X = rat.getX()-x;
                        double dist1Y = rat.getY()-y;
                        if (Math.sqrt(dist1X*dist1X + dist1Y*dist1Y) < distance) {
                            distance = (Math.sqrt(distX*distX + distY*distY));
                            closestRat = rat;
                            distX = dist1X;
                            distY = dist1Y;
                        }
                    }
                }
            }
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

        double rotation = (Math.toDegrees(Math.atan2(((closestRat.renderY()- y)), (closestRat.renderX()-x)))) - 90;

        g.save();
        g.translate(x + Game.gameSize / 2.0, y + Game.gameSize / 2.0);
        g.rotate(rotation);
        g.drawImage(image, -(size / 2), -(size / 2), size, size);
        g.restore();
    }
}
