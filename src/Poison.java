import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Description.
 *
 * @author
 *
 */
public class Poison extends Element {
	
  private Image image;
  public static final int MAX_HEALTH = 1;


  /**
   * Description.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   * @param health
   */
  public Poison(ElementType type, Level level, int x, int y, int health) {
    super(type, level, x, y, Direction.North, health);  

    image = ImageLoader.getImage("poison.png", 64);
  }
  
  private void logic() {
    checkRat();
    if (health <= 0) {
      level.removeElement(this);
    }
  }
  
  /**
   * Description.
   */
  public void checkRat() {
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Rat)) {
    	  Rat rat = (Rat) element;
    	  killRat(rat);
      }
    }
  }

    @Override
    protected void killRat(Rat rat) {
        if (rat.getIsPregnant()) {
            level.removeElement(rat);
            health = health - 1 - rat.getPregnantTime();;
            Game.score = Game.score + 10 + 10 * rat.getPregnantTime();
        } else {
            level.removeElement(rat);
            health--;
            Game.score = Game.score + 10;
        }
    }


  @Override
  protected void tick() {
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
      System.out.println("deleting poison");
    }
    currentTick++;
    if (currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    }
  }

  @Override
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    g.drawImage(image, x, y, size, size);
  }
}
