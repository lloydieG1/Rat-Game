import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of StopSign elements.
 *
 * @author William Randle, Jack Lennard
 */
public class StopSign extends Element {
	
  Image image;
  public static final int MAX_HEALTH = 5;

  /**
   * Description.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   * @param health
   */
  public StopSign(ElementType type, Level level, int x, int y, int health) {
    super(type, level, x, y, Direction.North, health);
	// TODO Auto-generated constructor stub
    image = ImageLoader.getImage("stopSign.png", 64);
  }

  /**
   * Runs Tick() behaviours, allowing them to run logics automatically 
   * in time with other elements.
   */
  protected void tick() {
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
      System.out.println("deleting stopSign");
    }
    currentTick++;
    if (currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    }
  }

  /**
   * The logic of the StopSign.
   */
  private void logic() {
      changeSkin();
    if (this.health >= MAX_HEALTH) {
      level.removeElement(this);
    }
  }

  public void blocksUp() {

    health++;

  }

  private void changeSkin() {
      if (health == 0) {
          image = ImageLoader.stopSign;
      } else if (health == 1) {
          image = ImageLoader.stopSign1;
      } else if (health == 2) {
        image = ImageLoader.stopSign2;
    } else if (health == 3) {
        image = ImageLoader.stopSign3;
    } else if (health == 4) {
        image = ImageLoader.stopSign4;
    }
  }
              

  /**
   * Renders a Stop Sign.
   *
   * @param g graphics context
   */
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    //calculating the position the Sign should be in this frame
    g.drawImage(image, x, y, size, size);
  }
}
