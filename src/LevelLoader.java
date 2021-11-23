import javafx.scene.Scene;
import javafx.scene.paint.Stop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * level loeader takes in text input and constructs a scenemanager with info
 * from said text file
 * @author william randle
 */
public class LevelLoader {
    private static String resources = "res\\maps";

    private static String[] levelData;

    private static final String OPEN_FILE_ERROR = "Could not find ";
    private static final String MAIN_DATA_DELIMITER = "/";
    private static final String INTERNAL_DATA_DELIMITER = ",";
    private static final String NEWLINE = ";";

    private static String fileName;

    private static Level level;


    public static Level getLevel(String levelName) {

        fileName = levelName+".txt";
        loadFile();

        int[] gridSize = parseGridSize(levelData[0]);
        level = new Level(gridSize[0],gridSize[1]);
        parseTiles(levelData[1]);
        parseRats(levelData[3]);

        return level;

    }

    private static Scanner openLevelFile(String fileName) {
        File inputFile = new File("res\\maps\\" + fileName);
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


    private static void loadFile() {
        String levelFile = readLevelFile(fileName);
        levelData = levelFile.split(MAIN_DATA_DELIMITER);

    }

      private static String readLevelFile(String fileName) {
        Scanner in = openLevelFile(fileName);

        String fileText = "";
        while(in.hasNext()) {
            fileText = fileText + in.nextLine();
        }

        in.close();

        return fileText;
    }

        private static TileType getTile(char tileS) {
        if (tileS == 'P') {

            return TileType.Path;

        } else if( tileS == 'T') {
            return TileType.Tunnel;
        } else if( tileS == 'G') {

            return TileType.Grass;
        } else {
            return TileType.Grass;
        }
    }



        private static int[] parseGridSize(String sizeData){
            String[] data = sizeData.split(INTERNAL_DATA_DELIMITER);
            int[] outputArr = new int[2];

            int width = Integer.parseInt(data[0]);
            int height = Integer.parseInt(data[1]);

            outputArr[0] = width;
            outputArr[1] = height;

            return outputArr;
        }



        private static void parseTiles(String tileData){


            String[] tileRows = tileData.split(NEWLINE);
            int rowSize=tileRows[0].length();
            int columnSize=tileRows.length;
            //TODO quite a scuffed nested for loop, want to change


            for (int i = 0; i < rowSize; i++) {

                for (int j = 0; j < columnSize; j++) {
                    char tileLetter = tileRows[j].charAt(i);
                    Tile tile = new Tile(getTile(tileLetter), j, i);

                    level.addTile(rowSize-i, j, tile);
                    System.out.println(tileLetter);

                }
            }


        }


        private static void parseRats(String ratData){
            String[] seperateRats = ratData.split(" ");
            for (int i = 0; i < seperateRats.length; i++) {
                String[] individualRat = seperateRats[i].split(INTERNAL_DATA_DELIMITER);

                int xPos = Integer.parseInt(individualRat[0]);
                int yPos = Integer.parseInt(individualRat[1]);
                boolean isMale = Boolean.parseBoolean(individualRat[2]);
                Direction initialDirection = getDirection(individualRat[3]);

                Rat rat = new Rat(ElementType.Rat, level, xPos, yPos, isMale, initialDirection);
                level.addElement(rat);
            }


        }

        private static Direction getDirection(String sDir) {
            if (sDir.equals("north")) {
                return Direction.North;
            } else if (sDir.equals("east")) {
                return Direction.East;
            } else if (sDir.equals("south")) {
                return Direction.South;
            } else if (sDir.equals("west")){
                return Direction.West;
            } else {
                System.out.println("wrong input direction");
                return Direction.North;

            }
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



