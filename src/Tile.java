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
  private final int dist = 5;
  private int dist2 = dist-1;



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
   * get if tile is next to grass
   *
   * @return boolean if the tile is next to grass
   */
  public TileType getType() {
    return type;
  }

  private boolean isNotOnEdge() {
      return (x > 0 && y > 0 && x < Game.currentLevel.getMapBounds()[0]-1 && y < Game.currentLevel.getMapBounds()[1]-1);
  }

  private boolean[] nextToType(TileType type) {

      boolean[] edges = new boolean[4];
      if (isNotOnEdge()) {
          edges[0]=(Game.currentLevel.getTile(x + 1, y).getType().equals(type));
          edges[1] = (Game.currentLevel.getTile(x - 1, y).getType().equals(type));
          edges[2] = (Game.currentLevel.getTile(x, y + 1).getType().equals(type));
          edges[3] = (Game.currentLevel.getTile(x, y - 1).getType().equals(type));

      } else {
          edges[0]=false;
          edges[1]=false;
          edges[2]=false;
          edges[3]=false;
      }
      return edges;
  }

    /**
     * renders the side for a grass tile
     * @param g
     * @param edges
     */
  private void renderSideGrass(GraphicsContext g, boolean[] edges) {
      double factor = (int) Game.gameSize;
      double x = (int) (this.x * factor + Game.gameX);
      double y = (int) (this.y * factor + Game.gameY);
      double size = Game.gameSize;
      Color dark = Color.color(0.2, 0.4, 0,0.5);
      Color light =Color.color(0.4, 0.8, 0,0.5);
      Color normal = Color.color(0.3, 0.6, 0);


      if (edges[0]) {

          g.setFill(normal);
          g.fillRect(x+(size*dist2)/dist, y, size/dist, size);
      }
      if (edges[1]) {

          g.setFill(normal);
          g.fillRect(x, y, size/dist, size);

      }

      if (edges[2]) {

          g.setFill(normal);
          g.fillRect(x, y+(size*dist2/dist), size, size/dist);
      }

      if (edges[3]) {

          g.setFill(normal);
          g.fillRect(x, y, size, size/dist);

      }

      if (edges[0]) {

          g.setFill(dark);
          g.fillRect(x+(size*dist2)/dist, y, size/dist, size);
      }
      if (edges[1]) {

          g.setFill(light);
          g.fillRect(x, y, size/dist, size);

      }

      if (edges[2]) {

          g.setFill(light);
          g.fillRect(x, y+(size*dist2/dist), size, size/dist);
      }

      if (edges[3]) {

          g.setFill(dark);
          g.fillRect(x, y, size, size/dist);

      }
  }

    private void renderSideTunnel(GraphicsContext g, boolean[] edges) {
        double factor = (int) Game.gameSize;
        double x = (int) (this.x * factor + Game.gameX);
        double y = (int) (this.y * factor + Game.gameY);
        double size = Game.gameSize;
        Color dark = Color.color(0, 0, 0,0.6);

         if (edges[0]) {

            g.setFill(dark);
            g.fillRect(x+(size*dist2)/dist, y+size/dist, size/dist, size-size*2/dist);
        }
        if (edges[1]) {

            g.setFill(dark);
            g.fillRect(x, y+size/dist, size/dist, size-size*2/dist);

        }

        if (edges[2]) {

            g.setFill(dark);
            g.fillRect(x+size/dist, y+(size*dist2/dist), size-size*2/dist, size/dist);
        }

        if (edges[3]) {

            g.setFill(dark);
            g.fillRect(x+size/dist, y, size-size*2/dist, size/dist);

        }
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
        g.fillRect(x, y, size, size);//draw tile




    } else if (type.equals(TileType.Path)) {
      g.setFill(Color.color(0.5, 0.5, 0.5));
      g.fillRect(x, y, size, size);
        renderSideGrass(g, nextToType(TileType.Grass)); //draw sides meeting paths
    } else if (type.equals(TileType.Tunnel)) {


        g.setFill(Color.color(0.3, 0.6, 0));
        g.fillRect(x, y, size, size);
        renderSideTunnel(g,nextToType(TileType.Path));
    }
  }

  /**
   * Description.
   *
   * @param g
   * @param width
   */
  public void minirender(GraphicsContext g, double width) {
    //check if this tile is visible for the 4 directions

    double sizeX = g.getCanvas().getWidth() / width;
    double sizeY = g.getCanvas().getHeight() / width;
    double x = this.x * sizeX;
    double y = this.y * sizeY;
    g.setFill(Color.RED);
    if (type.equals(TileType.Grass)) {
      g.setFill(Color.color(0.3, 0.6, 0));
      g.fillRect(x, y, sizeX, sizeY);
    } else if (type.equals(TileType.Path)) {
        g.setFill(Color.color(0.5, 0.5, 0.5));
        g.fillRect(x, y, sizeX, sizeY);
    } else if (type.equals(TileType.Tunnel)) {

        g.setFill(Color.color(0.2, 0.4, 0));

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
