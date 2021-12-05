import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
  private final int detailThickness = 6;
  private final int iterations = 3;
  private final double opacityFactor = 0.6;

  private final Color dark = Color.color(0.1, 0.2, 0, 0.2);
  private final  Color light = Color.color(0.5, 1, 0, 0.2);

  private final Color extraDark = Color.color(0.0, 0.1, 0, 0.2);
  private final Color extraLight = Color.color(0.6, 1, 0, 0.3);

  private double size = Game.gameSize;

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

    if (type.equals(TileType.Grass)) {
        image = ImageLoader.grass;
    } else {
        image = ImageLoader.tunnel;
    }

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
      return (x > 0 && y > 0 && x < Game.currentLevel.getMapBounds()[0] - 1 && y
    		  < Game.currentLevel.getMapBounds()[1] - 1);
  }

  private boolean[] nextToType(TileType type) {

      boolean[] edges = new boolean[4];
      
      if (isNotOnEdge()) {
          edges[0] = (Game.currentLevel.getTile(x + 1, y).getType().equals(type));
          edges[1] = (Game.currentLevel.getTile(x - 1, y).getType().equals(type));
          edges[2] = (Game.currentLevel.getTile(x, y + 1).getType().equals(type));
          edges[3] = (Game.currentLevel.getTile(x, y - 1).getType().equals(type));
      } else {
          edges[0] = false;
          edges[1] = false;
          edges[2] = false;
          edges[3] = false;
      }
      return edges;
  }



    /**
     * renders the side for a grass tile
     * @param g
     * @param edges
     */
  private void renderSideGrass(GraphicsContext g, boolean[] edges, int shadetype, double width) {
      double factor = (int) Game.gameSize;
      double x = (int) (this.x * factor + Game.gameX);
      double y = (int) (this.y * factor + Game.gameY);
      size = Game.gameSize;
      if (edges[0]) {


          g.drawImage(image, x + (size * (detailThickness - 1)) / detailThickness,
        		      y, size / detailThickness, size);
      }
      if (edges[1]) {
          g.drawImage(image, x, y, size / detailThickness, size);
      }
      if (edges[2]) {
          g.drawImage(image, x, y + (size * (detailThickness - 1) / detailThickness),
        		  size, size / detailThickness);
      }
      if (edges[3]) {
          g.drawImage(image, x, y, size, size / detailThickness);
      }
      if (!(shadetype ==3)) {
          shadeDown(g, edges, shadetype, width);
      }


  }


  private void shadeDown(GraphicsContext g, boolean[] edges, int shadeType, double width) {
      double factor = (int) Game.gameSize;
      double x = (int) (this.x * factor + Game.gameX);
      double y = (int) (this.y * factor + Game.gameY);
      size = Game.gameSize;
      if (shadeType == 0) {
          g.setFill(light);
      } else {
          g.setFill(extraDark);
      }
      if (edges[1]) {
          renderLeftPanel(g, x, y, width);
      }
      if (edges[2]) {
          renderDownPanel(g, x, y, width);
      }
      if (shadeType == 0) {
          g.setFill(dark);
      } else {
          g.setFill(extraDark);
      }
      if (edges[0]) {
          renderRightPanel(g, x, y, width);
      }
      if (edges[3]) {
          renderUpPanel(g, x, y, width);
      }
  }



    private void renderRightPanel(GraphicsContext g, double x, double y, double detailThickness) {
      g.fillRect(x + (size * (detailThickness - 1)) / detailThickness, y,
    		     size / detailThickness, size);
    }

    private void renderLeftPanel(GraphicsContext g, double x, double y, double detailThickness) {
        g.fillRect(x, y, size / detailThickness, size);
    }
    
    private void renderDownPanel(GraphicsContext g, double x, double y, double detailThickness) {
        g.fillRect(x, y + (size * (detailThickness - 1) / detailThickness), size, size / detailThickness);
    }
    
    private void renderUpPanel(GraphicsContext g, double x, double y, double detailThickness) {
        g.fillRect(x, y, size, size / detailThickness);
    }

    private void renderSideTunnel(GraphicsContext g, boolean[] edges) {
        double currentDistance = detailThickness;
        for (int i = 0; i < iterations; i++) {
            currentDistance = currentDistance * (1 + 1.0 / iterations);
            gradientTunnel(currentDistance, g, edges, opacityFactor / iterations);
        }
    }

    /**
     *
     * @param dist thickness of the side of a tile

     * @param g
     */
    private void gradientTunnel(double dist, GraphicsContext g, boolean[] edges, double opacity) {
        double factor = (int) Game.gameSize;
        double x = (int) (this.x * factor + Game.gameX);
        double y = (int) (this.y * factor + Game.gameY);
        double size = Game.gameSize;
        Color dark = Color.color(0, 0, 0, opacity);
        g.setFill(dark);
        
        if (edges[0]) { //right
            g.fillRect(x + (size * (dist - 1)) / dist, y + size / dist,
            		   size / dist, size - size * 2 / dist);
        }
        if (edges[1]) { //left
            g.fillRect(x, y + size / dist, size / dist, size - size * 2 / dist);
        }
        if (edges[2]) {//down
           g.fillRect(x + size / dist, y + (size * (dist - 1) / dist),
        		      size - size * 2 / dist, size / dist);
        }
        if (edges[3]) {//up
            g.fillRect(x + size / dist, y, size - size * 2 / dist, size / dist);
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
          renderSideTunnel(g, nextToType(TileType.Tunnel));
          renderSideGrass(g, nextToType(TileType.Grass), 3, detailThickness);
          
      } else if (type.equals(TileType.Tunnel)) {
          renderSideGrass(g, nextToType(TileType.Path), 0, detailThickness);
      } else if (type.equals(TileType.Grass)) {
          renderSideGrass(g, nextToType(TileType.Tunnel), 0, detailThickness);
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
        g.setFill(Color.color(0.25, 0.5, 0));
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
