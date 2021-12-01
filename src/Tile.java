import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * A tile occupies an x,y position on the map and, depending on tile type,
 * can be occupied by a number of Elements.
 *
 * @author William Randle
 */
public class Tile {

  private final int x; //x position of the tile
  private final int y; //y position of the tile

  private final TileType type;



  /**
   * Tile Constructor.
   *
   * @param type the variant of tile
   * @param y the y position of the tile
   * @param x the x position of the tile
   */
  public Tile(TileType type, int y, int x) {
    this.type = type;
    this.x = x;
    this.y = y;
  }

  /**
   * Get Tile Type.
   *
   * @return the Type of the specified tile 
   */
  public TileType getType() {
    return type;
  }

  /**
   * Draws a Tile on the Map.
   *
   * @param g Graphics Context
   */
  public void render(GraphicsContext g) {
    double factor = (int) Game.gameSize;
    double x = (int) (this.x * factor + Game.gameX);
    double y = (int) (this.y * factor + Game.gameY);
    double size = Game.gameSize;
    g.setFill(Color.RED);

    if (type.equals(TileType.Grass)) {
      g.setFill(Color.color(0.3, 0.6, 0));
      g.fillRect(x, y, size, size);
    } else if (type.equals(TileType.Path)) {
      g.setFill(Color.color(0.4, 0.3, 0));
      g.fillRect(x, y, size, size);
    } else if (type.equals(TileType.Tunnel)) {
      g.setFill(Color.color(0.4, 0, 0));
      g.fillRect(x, y, size, size);
    }
  }

  /**
   * Description.
   *
   * @param g
   * @param width
   */
  public void minirender(GraphicsContext g, double widthY,double widthX) {
    //check if this tile is visible for the 4 directions

    double sizeX = g.getCanvas().getWidth() / widthX;
    double sizeY = g.getCanvas().getHeight() / widthY;
    double x = this.x * sizeX;
    double y = this.y * sizeY;
    g.setFill(Color.RED);
    if (type.equals(TileType.Grass)) {
      g.setFill(Color.color(0.3, 0.6, 0));
      g.fillRect(x, y, sizeX, sizeY);
    } else if (type.equals(TileType.Path)) {
      g.setFill(Color.color(0.4, 0.3, 0));
        g.fillRect(x, y, sizeX, sizeY);
    } else if (type.equals(TileType.Tunnel)) {
      g.setFill(Color.color(0.4, 0, 0));
        g.fillRect(x, y, sizeX, sizeY);
    }
  }

  /**
   * Description.
   *
   * @return
   */
  public String tileType() {
    if (type.equals(TileType.Grass)) {
      return "G";
    } else if (type.equals(TileType.Path)) {
      return "P";
    } else if (type.equals(TileType.Tunnel)) {
      return "T";
    } else {
      return "-";
    }
  }

  public String asString() {
    return tileType();
  }
}
