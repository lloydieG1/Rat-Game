import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of StopSign elements
 * @author William Randle, Jack Lennard,
 */
public class StopSign extends Element {

    Image image;
	protected int blocks = 0;

    public StopSign(ElementType type, Level level, int x, int y, int health) {
		super(type, level, x, y, Direction.North, health);
		// TODO Auto-generated constructor stub

        image = ImageLoader.getImage("stopSign.png", 64);
	}

    /**
     * Runs Tick() behaviours, allowing them to run logics automatically 
     * in time with other elements
     */
    protected void tick() {
        if (level.getTile(x,y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
            System.out.println("deleting deathRat");
        }

        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

    }

    /**
     * the logic of the StopSign
     */
    private void logic() {
        if (this.blocks > 4){
        	level.removeElement(this);        }
    }

    public void blocksUp() {
        blocks++ ;
    }
              

    /**
     * renders a Stop Sign
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the Sign should be in this frame

        g.drawImage(image,x, y, size, size);


    }



}
