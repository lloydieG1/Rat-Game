import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
  private final int dist = 6;
  private int dist2 = dist-1;
  Image image;



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
      image = ImageLoader.grass;

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
      Color dark = Color.color(0.1, 0.2, 0,0.2);
      Color light =Color.color(0.5, 1, 0,0.2);



      if (edges[0]) {


          g.drawImage(image,x+(size*dist2)/dist, y, size/dist, size);
      }
      if (edges[1]) {


          g.drawImage(image,x, y, size/dist, size);

      }

      if (edges[2]) {


          g.drawImage(image,x, y+(size*dist2/dist), size, size/dist);
      }

      if (edges[3]) {


          g.drawImage(image,x, y, size, size/dist);

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
        Color dark = Color.color(0, 0, 0,0.3);
        g.setFill(dark);
         if (edges[0]) {

            g.fillRect(x+(size*dist2)/dist, y+size/dist, size/dist, size-size*2/dist);
             g.fillRect(x+(size*dist2)/dist, y, size/dist, size);
        }
        if (edges[1]) {

            g.fillRect(x, y+size/dist, size/dist, size-size*2/dist);
            g.fillRect(x, y, size/dist, size);
        }

        if (edges[2]) {


            g.fillRect(x+size/dist, y+(size*dist2/dist), size-size*2/dist, size/dist);
            g.fillRect(x, y+(size*dist2/dist), size, size/dist);
        }

        if (edges[3]) {
            g.fillRect(x+size/dist, y, size-size*2/dist, size/dist);
            g.fillRect(x, y, size, size/dist);

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


        g.drawImage(image, x, y, size, size);




    } else if (type.equals(TileType.Path)) {
      g.setFill(Color.color(0.5, 0.5, 0.5));
      g.fillRect(x, y, size, size);
    } else if (type.equals(TileType.Tunnel)) {


        g.drawImage(image, x, y, size, size);
    }
  }

  public void renderDetails(GraphicsContext g) {
      g.setFill(Color.RED);
      if (type.equals(TileType.Path)) {
          renderSideGrass(g, nextToType(TileType.Grass)); //draw sides meeting paths
      } else if (type.equals(TileType.Tunnel)) {
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
