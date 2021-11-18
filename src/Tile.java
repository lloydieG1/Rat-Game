public class Tile extends Element {
	private int width;
	private int height;
	private char tileType;
	
	public Tile (int width, int height, char tileType) {
		this.width = width;
		this.height = height;
		this.tileType = tileType;
	}
	
	public int getRow () {
		return width;
	}
	
	public int getColumn() {
		return height;
	}
	
	public char getTileType() {
		return tileType;
	}
}
