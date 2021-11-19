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
    private static final String MAIN_DATA_DELIMITER = "+";
    private static final String INTERNAL_DATA_DELIMITER = ",";
    private static final String NEWLINE = "\n";

    private static String fileName;


    public static Level getLevel(String levelName) {
        levelName = levelName+".txt";


        fileName = levelName;
        loadFile();

        int[] gridSize = parseGridSize(levelData[0]);
        Tile[][] tileGrid = createTileGrid(levelData[0]);
        int maxRats = Integer.parseInt(levelData[2]);
        ArrayList<Element> elements = parseRats(levelData[3]);
        int expectedTime = Integer.parseInt(levelData[4]);
        Level level = getEmptyMap(levelName);

        level = populateMap(levelName, level);

        return level;

    }

    private static Scanner openLevelFile(String fileName) {
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


    private static void loadFile() {
        String levelFile = readLevelFile(fileName);
        levelData = levelFile.split(MAIN_DATA_DELIMITER);

    }

    private static Level getEmptyMap(String levelName) {
        Level level = null;
        File levelFile = new File(resources + "\\" + levelName);
        try {

            Scanner map = new Scanner(levelFile);
            int size = map.nextInt();
            level = new Level(size, size);

            for (int j =0; j < size; j++) {
                for (int i =0; i < size; i++) {

                    level.addTile(i, j, new Tile(getTile(map.next()), i, j));
                }
            }
            map.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return level;
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

    private static Level populateMap(String levelName, Level level) {

        File levelFile = new File(resources + "\\" + levelName);
        try {

            Scanner map = new Scanner(levelFile);
            while (!(map.nextLine().contains("/"))) {
                //
            }
            while (map.hasNextLine()) {
                String elementType = map.next();
                if (elementType.equals("rat")) {
                    int x = map.nextInt();
                    int y = map.nextInt();
                    String gender = map.next();
                    boolean isMale = gender.equals("m");
                    Rat rat = new Rat(ElementType.Rat, level, x, y, isMale);
                    level.addElement(rat);
                }
            }


            map.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return level;
    }


    private static TileType getTile(String tileS) {
        if (tileS.equals("p")) {
            return TileType.Path;
        } else if( tileS.equals("t")) {
            return TileType.Tunnel;
        } else {
            return TileType.Grass;
        }
    }

}
