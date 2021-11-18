import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelLoader {
	//constants
	private final String OPEN_FILE_ERROR = "Could not find ";
	private final String MAIN_DATA_DELIMITER = "/";
	private final String INTERNAL_DATA_DELIMITER = ",";
	private final String NEWLINE = "\n";
	
    /**
     * takes a plaintext level file and parses the
     * data into this instance of a level.
     * @param fileName name of level file
     */
	public Level levelLoader(String fileName) {
		String levelFile = readLevelFile(fileName);
		
		String[] levelData = levelFile.split(MAIN_DATA_DELIMITER);
		
		int[] gridSize = parseGridSize(levelData[0]);
		Tile[][] tileGrid = createTileGrid(levelData[0]);
		int maxRats = Integer.parseInt(levelData[2]);
		ArrayList<Element> elements = parseRats(levelData[3]);
		int expectedTime = Integer.parseInt(levelData[4]);
		//private ArrayList<Element> levelItemData = new ArrayList<Element>();
		
		//TODO ITEM DATA HAS NOT YET BEEN IMPLEMENTED SO THATS WHY THE FINAL VALUE IN CONSTRUCTOR IS NULL
		Level currentLevel = new Level(gridSize, tileGrid, elements, maxRats, expectedTime, null);
		
		return currentLevel;
		 
	}
	
	private Scanner openLevelFile(String fileName) {
		File inputFile = new File(fileName);
		Scanner in = null;
		
		//attempts to open file and returns exception if it is not found
		try {
			in = new Scanner (inputFile);
		}
		catch (FileNotFoundException e) {
			System.out.println (OPEN_FILE_ERROR + fileName);
			System.exit (0);
		}
		
		return in;
	}
	
	private String readLevelFile(String fileName) {
		Scanner in = openLevelFile(fileName);
		
		String fileText = "";
		while(in.hasNext()) {
			fileText = fileText + in.nextLine();
		}
		
		in.close();
		
		return fileText;
	}
	
	private int[] parseGridSize(String sizeData){
		String[] data = sizeData.split(INTERNAL_DATA_DELIMITER);
		int[] outputArr = new int[2];
		
		int width = Integer.parseInt(data[0]);
		int height = Integer.parseInt(data[1]);
		
		outputArr[0] = width;
		outputArr[1] = height;
		
		return outputArr;
	}
	
	private Tile[][] createTileGrid(String gridSize){
		String[] widthAndHeight = gridSize.split(",");
		Tile[][] arr = new Tile[Integer.parseInt(widthAndHeight[0])][Integer.parseInt(widthAndHeight[0])];
		return arr;
	}
	
	private ArrayList<Tile> parseTiles(String tileData){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//TODO quite a scuffed nested for loop, want to change
		String[] tileRows = tileData.split(NEWLINE);
		for (int i = 0; i < tileRows.length; i++) {
			for (int j = 0; j < tileRows[i].length(); j++) {
				char tileLetter = tileRows[i].charAt(j);
				
				//TODO this will be Tile(j, gridSize[0] - i, tileLetter) once Tile is implemented
				//Tile tile = new Tile();
				tileGrid[j][i] = new Tile(j, i, tileLetter);
				
			}
		}
		
		return tiles;
	}
	
	
	private ArrayList<Element> parseRats(String ratData){
		ArrayList<Element> rats = new ArrayList<Element>();
		
		String[] seperateRats = ratData.split(" ");
		for (int i = 0; i < seperateRats.length; i++) {
			String[] individualRat = seperateRats[i].split(INTERNAL_DATA_DELIMITER);
			
			int xPos = Integer.parseInt(individualRat[0]);
			int yPos = Integer.parseInt(individualRat[1]);
			boolean isMale = Boolean.parseBoolean(individualRat[2]);
			String initialDirection = individualRat[3];
			
			Rat rat = new Rat(xPos, yPos, isMale, initialDirection);
			rats.add(rat);
		}
		
		return rats;
	}

	//TODO figure out how data for items in menu work
//	private ArrayList<Element> parseItem(String itemData){
//		ArrayList<Element> tiles = new ArrayList<>();
//		
//		return tiles;
//	}

	
//	public static void main(String[] args) {		
//		Level currentLevel = new Level();
//		currentLevel.parseLevelFile("./src/TestLevel.txt");
//		
//	}	
}
