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
     * @param dir
     */
    public Gas(ElementType type, Level level, int x, int y, Direction dir) {
        super(type, level, x, y, dir);
        health = 6;
        damage = false;
        
      
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

        g.setFill(Color.color(0.2,0.2,0.3));
        g.fillRect(x, y, size/2, size/2);


    }

}