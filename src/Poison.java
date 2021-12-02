import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Description.
 *
 * @author
 *
 */
public class Poison extends Element {
	
  private Image image;  
  private int ratKilled;

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
    ratKilled = 0;
    image = ImageLoader.getImage("poison.png", 64);
  }
  
  private void logic() {
    poisonKill();
    if (ratKilled == 1) {
      level.removeElement(this);
    }
  }
  
  /**
   * Description.
   */
  public void poisonKill() {
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Rat)) {
        level.removeElement(element);
        ratKilled++;
        Game.score = Game.score + 10;
      }
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
