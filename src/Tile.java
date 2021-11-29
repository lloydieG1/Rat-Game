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
        double factor = Game.gameSize;
        double x = this.x*factor + Game.gameX;
        double y = this.y*factor + Game.gameY;
        int size = Game.gameSize;
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


    public boolean isVisible() {
        int visibleTiles = Game.VISIBLE_TILES+1;
        boolean visible = true;
        if (this.y-1 < -(Game.gameY/Game.gameSize)) {
            visible = false;
        }
        if (this.x-1 < -(Game.gameX/Game.gameSize)) {
            visible = false;
        }
        if (this.x-1 > -(Game.gameX/Game.gameSize) + visibleTiles) {
            visible = false;
        }
        if (this.y-1 > -(Game.gameY/Game.gameSize) + visibleTiles) {
            visible = false;
        }
        return visible;
    }


    public void minirender(GraphicsContext g, int width) {
        //check if this tile is visible for the 4 directions


        double size = g.getCanvas().getHeight()/width;
        double x = this.x*size;
        double y = this.y*size;
        g.setFill(Color.RED);
        if (type.equals(TileType.Grass)) {


            g.setFill(Color.color(0, 1, 0));
            g.fillRect(x, y, size, size);
        } else if (type.equals(TileType.Path)) {
            g.setFill(Color.color(0.6,0.5,0.3));
            g.fillRect(x, y, size, size);
        }else if (type.equals(TileType.Tunnel)) {
            g.setFill(Color.color(0.4,0.5,0));
            g.fillRect(x, y, size, size);
        }

        if (!(isVisible())) {
            g.setFill(Color.color(0, 0, 0, 0.3));
            g.fillRect(x, y, size, size);
        }


    }
}
