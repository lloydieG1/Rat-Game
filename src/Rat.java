import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Class used for construction and behaviours of Rat elements.
 * @author William Randle
 * @author Mosaed
 * @author Tafon Taffe
 */
public class Rat extends Element {

    private static final int MATING_TIME = 4;
    private static final int ADULT_AGE = 8;
    private boolean isMale;
    private boolean isSterile;
    private int timeLeftInMating;
    private int timeLeftPregnant = 0;
    private Image image;
    private static final int PREGNANT_TIME = 4;
    private static final int MAX_HEALTH = 3;
    private static final int CHILD_LIKELIHOOD = 4;

    /**
     * Create Rat.
     *
     * @param type   Element Type.
     * @param level  Level number.
     * @param x      The x coordinate position.
     * @param y      The x coordinate position.
     * @param isMale Boolean for gender check.
     * @param dir    Direction taken.
     * @param health Remaining Health.
     */
    public Rat(ElementType type, Level level, int x, int y, boolean isMale,
               Direction dir, int health, boolean sterile) {
        super(type, level, x, y, dir, health);
        this.isMale = isMale;
        this.isSterile = sterile;

        if (getIsChild()) {
            tickSpeed = Game.FPS / 2;
            image = ImageLoader.ratChild;
        }
        develop();
    }

    @Override
    public void setAge(int age) {
        this.age = age;
        develop();
    }

    /**
     * Check Sterility.
     * @return Boolean for Sterility.
     */
    public boolean isSterile() {
        return isSterile;
    }
    /**
     * Set mating time.
     * @param timeLeftInMating Remaining Time.
     */
    public void setMatingTime(int timeLeftInMating) {
        this.timeLeftInMating = timeLeftInMating;
    }

    /**
     * sets the time left for the rats pregnancy
     * @param pregnantTime int time for pregnancy
     */
    public void setPregnantTime(int pregnantTime) {
        this.timeLeftPregnant = pregnantTime;
    }

    /**
     * gives the time left for pregnancy
     * @return int time for pregnancy
     */
    public int getPregnantTime() {

        return timeLeftPregnant;
    }


    /**
     * ticks the rat
     */
    protected void tick() {
        if (isFinishedMating()) {
            if (!getIsChild()) {
                if (!(isMale)) {
                    breed();
                }
            }
        }
        if (health < 1) {
            Game.score = Game.score + 1;
            level.removeElement(this);
        }

        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            level.removeElement(this);

        }

        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

        if (isFinishedMating()) {
            if (!getIsChild()) {
                if (!(isMale)) {
                    breed();
                }
            }
        }
    }

    /**
     * The logic of the rat, only ran at the rats slower speed.
     */
    private void logic() {
        timeLeftInMating--;
        if (timeLeftInMating < 0) {
            timeLeftInMating = 0;
        }
        age++;
        develop();

        //Can move only if mating time is finished
        if (isFinishedMating()) {
            pregnant();
            movement();
        }

        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Gas)) {
                health--;
            }
            if (health <= 0) {
                killRat(this);

            }
        }
    }

    /**
     * Keep track how much time elapsed on mating.
     * @return Whether mating time is finished
     */
    public boolean isFinishedMating() {
        return timeLeftInMating <= 0;
    }


    /**
     * returns if the rat is a child
     * @return boolean if the rat is a child
     */
    public boolean getIsChild() {
        return age < ADULT_AGE;
    }

    /**
     * changes the image and speed for rat if it is now an adult
     */
    private void develop() {
        if (!getIsChild()) {
            tickSpeed = Game.FPS;
            if (isMale) {
                image = ImageLoader.ratMale;
            } else {
                image = ImageLoader.ratFemale;
            }
        }
    }

    /**
     * 
     * @return boolean if the rat is male
     */
    public boolean getIsMale() {
        return isMale;
    }


    /**
     * sets the gender of the rat
     * @param isMale the gender of hte rat
     */
    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }


    /**
     * @return boolean if the rat is pregnant
     */
    public boolean getIsPregnant() {
        return isPregnant();
    }


    /**
     *makes the rat sterile
     */
    public void makeSterile() {
        this.isSterile = true;
    }

    /**
     * 
     * @return boolean if the rat is pregnant
     */
    private boolean isPregnant() {
        return timeLeftPregnant > 0;
    }


    /**
     * runs the logic for when a rat is pregnant
     */
    private void pregnant() {
        timeLeftPregnant--;
        if (timeLeftPregnant < 0) {
            timeLeftPregnant = 0;
        }
        if (isPregnant()) {

            tickSpeed = Game.FPS * 2;
            Rat rat = new Rat(ElementType.Rat, level, x, y,
                    Game.random.nextBoolean(), Direction.North, MAX_HEALTH, false);
            rat.setAge(0);
            rat.setMatingTime(0);
            level.addElementLive(rat);
        }
    }


    /**
     *
     * @return boolean if the rat can breed
     */
    public boolean canBreed() {
        if (isMale) {
            if (!getIsChild()) {
                if (!isSterile) {
                    if ((timeLeftInMating <= 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * breeds with another rat
     */
    private void breed() {
        if (!isPregnant()) {
            for (Element element : level.getElements(x, y)) {
                if (element.getType().equals(ElementType.Rat)) {
                    Rat rat = (Rat) element;

                    if (rat.canBreed()) {
                        rat.alignPosition();
                        rat.setMatingTime(MATING_TIME);
                        timeLeftInMating = MATING_TIME;
                        timeLeftPregnant = PREGNANT_TIME;
                        alignPosition();
                    }

                }
            }
        }
    }

    /**
     * Renders a rat.
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.save();
        g.translate(x + Game.gameSize / 2.0, y + Game.gameSize / 2.0);
        g.rotate(interpolateDir(dirAsNum(lastDir), dirAsNum(dir)));
        g.drawImage(image, -(size / 2), -(size / 2), size, size);
        g.restore();

    }

    @Override
    protected String extraInfo() {
        return "," + isMale + "," + isSterile + "," + timeLeftInMating + "," + timeLeftPregnant;
    }

}
