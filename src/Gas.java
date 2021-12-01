import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Class used for construction and behaviours of the Gas element.
 *
 * @author Yazan
 */
public class Gas extends Element {
  private boolean damage;
  private Image image;

  /**
   * Gas constructor.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   */
  public Gas(ElementType type, Level level, int x, int y, int health) {
    super(type, level, x, y, Direction.North, health);
    this.health = health;
    damage = false;
    image = ImageLoader.getImage("gas.png", 64);
    tickSpeed = (int) (Game.FPS / 1.2);  
  }

  /**
   * Runs Tick() behaviours, allowing them to run logics automatically 
   * in time with other elements.
   */
  protected void tick() {
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
    }
    currentTick++;
    if (currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    }
    if (health <= 0) {
      level.removeElement(this);
    }
  }

  /**
   * The logic of the rat, only run at the rat's slower speed.
   */
  private void logic() {
    age++;
    health--;
    if (isSpreadable(x - 1, y)) {
      level.addElementLive(new Gas(ElementType.Gas, level, (x - 1), y, health));
    }
    if (isSpreadable(x + 1, y)) {
      level.addElementLive(new Gas(ElementType.Gas, level, (x + 1), y, health));
    }
    if (isSpreadable(x, y - 1)) {
      level.addElementLive(new Gas(ElementType.Gas, level, x, y - 1, health));
      System.out.println("test");
    }
    if (isSpreadable(x, y + 1)) {
      level.addElementLive(new Gas(ElementType.Gas, level, x, y + 1, health));
      System.out.println("test");
    }
    
  }

  /**
   * Checks if the tile at parsed position is a eligable tile to go onto.
   *
   * @return boolean if tile is safe
   */
  protected boolean isSpreadable(int x, int y) {
    int boardSize = level.getLength();
    if (x < 0 || y < 0) {
      return false;
    }
    if (x > boardSize - 1 || y > boardSize - 1) {
      return false;
    }
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      return false;
    }
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Gas)) {
        return false;
      }
    }

      for (Element element : level.getElements(x, y)) {
          if (element.getType().equals(ElementType.StopSign)) {
              return false;
          }
      }
    return true;
  }

  /**
   * Get Directions.
   *
   * @return Directions
   */
  public Direction getDirection() {
    return dir;
  }
  
  /**
   * Get Age.
   *
   * @return Age
   */
  public int getAge() {
    return age;
  }






  /**
   * Renders a rat.
   *
   * @param g graphics context
   */
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    
    //calculating the position the rat should be in this frame
    g.drawImage(image, x, y, size, size);
  }
}