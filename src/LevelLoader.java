import javafx.scene.Scene;
import javafx.scene.paint.Stop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * level loeader takes in text input and constructs a scenemanager with info
 * from said text file
 * @author william randle
 */
public class LevelLoader {
    private static String resources = "res\\maps";

    public static Level getLevel(String levelName) {
        levelName = levelName+".txt";

        Level level = getEmptyMap(levelName);

        level = populateMap(levelName, level);
        return level;

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
