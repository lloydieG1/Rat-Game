import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {
	private int xPos;
	private int yPos;
	private TileType type;
	
	public Tile (int xPos, int yPos, TileType type) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.type = type;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public TileType getTileType() {
		return type;
	}


    private void render(GraphicsContext g) {
        double factor = 12;
        double x = this.xPos*factor;
        double y = this.yPos*factor;
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
