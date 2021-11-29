import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of the Gas element
 * @author Yazan
 */
public class Gas extends Element {
	
    private int health;
    private boolean damage;
    private Image image;

    /**
     * Gas constructor
     * 
     * @param type
     * @param level
     * @param x
     * @param y
     */
    public Gas(ElementType type, Level level, int x, int y) {
        super(type, level, x, y, Direction.North);
        health = 6;
        damage = false;
        image = ImageLoader.getImage("gas.png", 64);
      
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
     * the logic of the rat, only run at the rat's slower speed
     */
    private void logic() {
        age++;
        movement();
    }

    public Direction getDirection () {
        return dir;
    }

    public int getAge() {
        return age;
    }






    /**
     * renders a rat
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.drawImage(image, x - size / 4, y - size / 4);


    }

}