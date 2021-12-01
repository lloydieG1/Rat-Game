import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


/**
 * Description.
 *
 * @author 
 *
 */
public class FemaleChanger extends Element {
  private Image image;	
  private int femaleChangeCount;	

  /**
   * Description.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   * @param health
   */
  public FemaleChanger(ElementType type, Level level, int x, int y, int health) {
    super(type, level, x, y, Direction.North, health);
    // TODO Auto-generated constructor stub
    femaleChangeCount = 0;
    image = ImageLoader.getImage("femaleGenderChange.png", 64);
  }
	
	
  /**
   * Runs Tick() behaviours, allowing them to run logics automatically 
   * in time with other elements.
   */
  @Override
  protected void tick() {
    if (level.getTile(x,y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
      System.out.println("deleting female changer");
    }
    currentTick++;
    if(currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    }
  }
  
  /**
   * The logic of the Element.
   */
  private void logic() {
    if (this.femaleChangeCount == 1){
      level.removeElement(this);
    }
  }
    


	
  /**
   * Renders a Male Changer sign.
   *
   * @param g graphics context
   */
  @Override
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    g.drawImage(image, x, y, size, size);
  }
}
