public class Tile extends Element {
	private int width;
	private int height;
	private String tileType;
	
	public Tile (int width, int height, String tileType) {
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
	
	public String getTileType() {
		return tileType;
	}
}
