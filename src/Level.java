import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for storing and manipulating
 * the parts that make up a level.
 * @author Lloyd
 */
public class Level {
	//data that makes up a level

	private int[] gridSize;
	private Tile[][] tileGrid = null;
	private ArrayList<Element> elements = new ArrayList<Element>(); 
	private int maxRats;
	private int expectedTime;
	private ArrayList<Element> levelItemData = new ArrayList<Element>();
	

	/**
	 * @param gridSize
	 * @param tileGrid
	 * @param elements
	 * @param maxRats
	 * @param expectedTime
	 * @param levelItemData
	 */
	public Level(int[] gridSize, Tile[][] tileGrid, ArrayList<Element> elements, int maxRats, int expectedTime,
			ArrayList<Element> levelItemData) {
		super();
		this.gridSize = gridSize;
		this.tileGrid = tileGrid;
		this.elements = elements;
		this.maxRats = maxRats;
		this.expectedTime = expectedTime;
		this.levelItemData = levelItemData;
	}
}

