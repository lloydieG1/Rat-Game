import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * a tile occupies an x,y position on the map and, depending on tile type,
 * can be occupied by a number of Elements.
 * @author william randle
 * @version 1
 */
public class Tile {

    private final int x; //x position of the tile
    private final int y; //y position of the tile

    private final TileType type;



    /**
     * @param type the variant of tile
     * @param y the y position of the tile
     * @param x the x position of the tile
     * 
     * Tile Constructor
     */
    public Tile(TileType type, int y, int x) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the Type of the specified tile 
     */
    public TileType getType() {


        return type;
    }

    /**
     * @param g Graphics Context
     * 
     * draws a Tile on the Map
     */
    public void render(GraphicsContext g) {
        double factor = (int)Game.gameSize;
        double x = (int)(this.x*factor + Game.gameX);
        double y = (int)(this.y*factor + Game.gameY);
        double size = Game.gameSize;
        g.setFill(Color.RED);

        if (type.equals(TileType.Grass)) {
            g.setFill(Color.color(0.3,0.6,0));
            g.fillRect(x, y, size, size);
        } else if (type.equals(TileType.Path)) {
            g.setFill(Color.color(0.4,0.3,0));
            g.fillRect(x, y, size, size);
        }else if (type.equals(TileType.Tunnel)) {
            g.setFill(Color.color(0.4,0,0));
            g.fillRect(x, y, size, size);
        }
    }



    public void minirender(GraphicsContext g, double width) {
        //check if this tile is visible for the 4 directions

        double size = g.getCanvas().getHeight()/width;
        double x = this.x*size;
        double y = this.y*size;
        g.setFill(Color.RED);
        if (type.equals(TileType.Grass)) {


            g.setFill(Color.color(0.3,0.6,0));
            g.fillRect(x, y, size, size);
        } else if (type.equals(TileType.Path)) {
            g.setFill(Color.color(0.4,0.3,0));
            g.fillRect(x, y, size, size);
        }else if (type.equals(TileType.Tunnel)) {
            g.setFill(Color.color(0.4,0,0));
            g.fillRect(x, y, size, size);
        }




    }

    public String tileType() {
        if (type.equals(TileType.Grass)) {
            return "G";
        } else if (type.equals(TileType.Path)) {
            return "P";
        }else if (type.equals(TileType.Tunnel)) {
            return "T";
        } else {
            return "-";
        }
    }


    public String asString() {
        return tileType();
    }
}
