import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is to construct bombs and hold all its features.
 * 
 * @author Jack
 */
public class Bomb extends Element {
	private Image image;
	public static final int BLAST_TIME = 4;

	/**
	 * Construct a bomb element.
	 * 
	 * @param type   Element type
	 * @param level  Level number
	 * @param x      X position
	 * @param y      Y position
	 * @param health Remaining health till explosion
	 */
	public Bomb(ElementType type, Level level, int x, int y, int health) {
		super(type, level, x, y, Direction.North, health);
		image = ImageLoader.bomb;
	}

	protected void tick() {
		if (level.getTile(x, y).getType().equals(TileType.Grass)) {
			level.removeElement(this);
			System.out.println("deleting deathRat");
		}
		currentTick++;
		if (currentTick > tickSpeed) {
			currentTick = 0;
			logic();
		}
	}

	private void logic() {
		health--;
		if (health <= 0) {
			explode();
			level.removeElement(this);
		}
	}

	private void explode() {
		level.addElementLive(new Explosion(ElementType.Explosion, level, (x), y, BLAST_TIME));

		int tempX = x;
		int tempY = y;
		tempX++;

		while (isSpreadable(tempX, tempY)) {
			level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX), tempY, BLAST_TIME));
			tempX++;
		}
		tempX = x;
		tempY = y;
		tempX--;

		while (isSpreadable(tempX, tempY)) {
			level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX), tempY, BLAST_TIME));
			tempX--;
		}
		tempX = x;
		tempY = y;
		tempY++;

		while (isSpreadable(tempX, tempY)) {
			level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX), tempY, BLAST_TIME));
			tempY++;
		}
		tempX = x;
		tempY = y;
		tempY--;

		while (isSpreadable(tempX, tempY)) {
			level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX), tempY, BLAST_TIME));
			tempY--;
		}
	}

	/**
	 * checks if the tile at parsed position is a eligible tile to go onto.
	 *
	 * @return boolean if tile is safe
	 */
	protected boolean isSpreadable(int x, int y) {
		int boardSize = level.getLength();

		if (x < 0 || y < 0) {
			return false;
		}

		if (x > boardSize - 1 || y > boardSize - 1) {
			return false;
		}

		if (level.getTile(x, y).getType().equals(TileType.Grass)) {
			return false;
		}

		return true;
	}

	protected void render(GraphicsContext g) {
		// get the current interpolated frame positions of rat.
		double x = renderX();
		double y = renderY();

		// calculating the position the rat should be in this frame
		g.drawImage(image, x, y, size, size);

		g.setFont(new Font("mono-space", size / 4));

		g.setFill(Color.color(1 - (health * 1.0 / BLAST_TIME), (health * 1.0 / BLAST_TIME), 0));
		g.fillText(Integer.toString(health), x + size / 2.3, y + size / 1.4);

	}
}
