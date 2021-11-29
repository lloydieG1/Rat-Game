import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.image.Image;
        import javafx.scene.paint.Color;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;


/**
 * Class used for construction and behaviours of Rat elements
 * @author William Randle, Mosaed,
 */
public class Rat extends Element {

    private boolean isMale;

    private boolean isPregnant;

    private static int ADULTHOOD = 5;

    private boolean isSterile;

    private boolean isChild;

    private int breeding = 0;
    private final int BREEDING_TIME = 5;
    private Image image;



    /**
     * Rat constructor
     *
     * @param type
     * @param level
     * @param x
     * @param y
     * @param isMale
     * @param dir
     */
    public Rat(ElementType type, Level level, int x, int y, boolean isMale, Direction dir) {
        super(type, level, x, y, dir);

        this.isMale = isMale;

        isChild = true;

        image = ImageLoader.getImage("ratChild.png", 64);

    }

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically
     * in time with other elements
     */
    protected void tick() {
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

    }


    /**
     * the logic of the rat, only ran at the rats slower speed
     */
    private void logic() {
        if (breeding == 0) {
        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);

        }

        age++;
        if (age == ADULTHOOD) {
            grow();
        }
        movement();
        if(! isChild) {
            if (isMale == false) {
                breed();
            }
        }
        } else {
            breeding--;
        }

        if (breeding < 0) {
            breeding = 0;
        }
    }



    private void grow() {

        isChild = false;


            if (isMale) {
                image = ImageLoader.getImage("ratMale.png", 64);

            } else {
                image = ImageLoader.getImage("ratFemale.png", 64);
            }


    }

    public boolean getIsMale () {
        return isMale;
    }

    public Direction getDirection () {
        return dir;
    }

    public int getAge() {
        return age;
    }



    private void breed() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                Rat rat = (Rat) element;
                if (rat.isMale == true) {
                    level.addElementLive(new Rat(ElementType.Rat, level, x, y, Game.random.nextBoolean(), Direction.North));
                    breeding = BREEDING_TIME;
                    nextY = y;
                    nextX = x;
                }
            }
        }
    }



    /**
     * renders a rat
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.save();
        g.translate(x+Game.gameSize/2.0, y+Game.gameSize/2.0);
        g.rotate(interpolateDir(dirAsNum(lastDir),dirAsNum(dir)));
        g.drawImage(image,-(size/2), -(size/2), size, size);
        g.restore();

    }

}
