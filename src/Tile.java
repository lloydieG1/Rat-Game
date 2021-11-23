import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * a tile will store the elements insde it.
 * @author william randle
 */
public class Tile {

    private final int x;
    private final int y;

    private final TileType type;


    public Tile(TileType type, int y, int x) {
        this.type = type;
        this.x = x;
        this.y = y;

    }





    public TileType getType() {


        return type;
    }



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
