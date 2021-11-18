public class Tile extends Element {
	private int xPos;
	private int yPos;
	private char tileType;
	
	public Tile (int xPos, int yPos, char tileType) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.tileType = tileType;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public char getTileType() {
		return tileType;
	}
}
