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

    private final int x;
    private final int y;

    private final TileType type;

    /**
     * @param type
     * @param y
     * @param x
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
        double factor = Game.factor;
        double x = this.x*factor;
        double y = this.y*factor;
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
}
