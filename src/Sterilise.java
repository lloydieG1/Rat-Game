import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


/**
 * Class used for construction and behaviours of Steriliser elements.
 *
 * @author William Randle, Jack Lennard,
 */
public class Sterilise extends Element {
	
  protected int sterilisedRats;
	
  private Image image;

  /**
   * Description.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   * @param health
   */
  public Sterilise(ElementType type, Level level, int x, int y, int health) {
    super(type, level, x, y, Direction.North, health);
    // TODO Auto-generated constructor stub
    image = ImageLoader.sterilize;
    sterilisedRats = 0;
  }
  
  /**
   * Runs Tick() behaviours, allowing them to run logics automatically 
   * in time with other elements.
   */
  protected void tick() {
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
      System.out.println("deleting sterilise");
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
    if (this.sterilisedRats > 4) {
      level.removeElement(this);
    }
  }

  public void steriliseRat(Rat rat) {
    sterilisedRats++;
    rat.makeSterile();
  }
              

  /**
   * Renders a Sterilise sign.
   *
   * @param g graphics context
   */
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    g.drawImage(image, x, y, size, size);
  }
}
