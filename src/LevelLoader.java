import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Level loader takes in text input and constructs a scenemanager with info
 * from said text file.
 *
 * @author William Randle, Lloyd, Adrian, Yazan
 */
public class LevelLoader {
  private static final String OPEN_FILE_ERROR = "Could not find ";
  private static final String MAIN_DATA_DELIMITER = "/";
  private static final String INTERNAL_DATA_DELIMITER = ",";
  private static final String TILE_GRID_DELIMITER = ";";
  private static final String SPACE = " ";
  private static final String LEVELS_PATH = "res\\maps\\";

  private static String[] levelData;
  private static Level level;
    
  /**
   * Description.
   *
   * @param levelName the name of the level, used to find the level file
   * @return a populated map of tiles and rats to then be played
   */
  public static Level getLevel(String levelName) {
    String fileName = levelName + ".txt";
    loadFile(fileName);

    levelName = levelName.replace("save\\", "");

    int[] gridSize = parseGridSize(levelData[0]);
    level = new Level(gridSize[0], gridSize[1], Integer.parseInt(levelData[2]), levelName,
                   Integer.parseInt(levelData[4]), Integer.parseInt(levelData[5]));
    parseTiles(levelData[1]);
    parseElements(levelData[3]);
    parseItemData(levelData[6]);

    level.setScore(Integer.parseInt(levelData[7]));

    level.loadSideBar(parseSideBar(levelData[8]));

    return level;
  }

  /**
   * Description.
   *
   * @param fileName
   * @return a long sequence of text that is interpreted by the parseX() methods
   */
  private static Scanner openLevelFile(String fileName) {
    File inputFile = new File(LEVELS_PATH + fileName);
    Scanner in = null;

    //attempts to open file and returns exception if it is not found
    try {
      in = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
      System.out.println(OPEN_FILE_ERROR + fileName);
      System.exit(0);
    }
    return in;
  }

  /**
   * Splits the data from the level file into interpretable pieces.
   */
  private static void loadFile(String fileName) {
    String levelFile = readLevelFile(fileName);
    levelData = levelFile.split(MAIN_DATA_DELIMITER);
  }

  /**
   * Description.
   *
   * @param fileName
   * @return parsed level data to be passed to the parseX() methods
   */
  private static String readLevelFile(String fileName) {
    Scanner in = openLevelFile(fileName);
    String fileText = "";
    while (in.hasNext()) {
      fileText = fileText + in.nextLine();
    }
    in.close();
    return fileText;
  }

  /**
   * Description.
   *
   * @param tileS a char that relates to a type of Tile
   * @return the type of the tile to be taken from TileType.emnum
   */
  private static TileType getTile(char tileChar) {
    //grass tiles are checked first for optimisation as they are the most common
    switch (tileChar) {
      case 'G':
        return TileType.Grass;
      case 'P':
        return TileType.Path;
      case 'T':
        return TileType.Tunnel;
      default:
        System.out.println("Invalid tile type " + tileChar);
        return TileType.Grass;
    }
  }

  /**
   * Description.
   *
   * @param sizeData specifies the size of the game map
   * @return a 2d array representing the x,y coordinates of the game map
   */
  private static int[] parseGridSize(String sizeData) {
    String[] data = sizeData.split(INTERNAL_DATA_DELIMITER);
    int[] outputArr = new int[2];

    int width = Integer.parseInt(data[0]);
    int height = Integer.parseInt(data[1]);

    outputArr[0] = width;
    outputArr[1] = height;

    return outputArr;
  }

  private static int[] parseSideBar(String sideBar) {
    String[] data = sideBar.split(INTERNAL_DATA_DELIMITER);
    int[] outputArr = new int[4];

    int bombs = Integer.parseInt(data[0]);
    int deathRats = Integer.parseInt(data[1]);
    int gas = Integer.parseInt(data[2]);
    int stopSigns = Integer.parseInt(data[3]);// maybe this should be final?

    outputArr[0] = bombs;
    outputArr[1] = deathRats;
    outputArr[2] = gas;
    outputArr[3] = stopSigns;

    return outputArr;
  }

  /**
   * This method parses the list of tiles passed through the level
   * data and then constructs the map sequentially by getting the type
   * and coordinates needed, then placing the tile.
   *
   * @param tileData The full set of tiles comprising this level's game map
   */
  private static void parseTiles(String tileData) {
    String[] tileRows = tileData.split(TILE_GRID_DELIMITER);
    int rowSize = tileRows[0].length();
    int columnSize = tileRows.length;
    //TODO quite a scuffed nested for loop, want to change
    for (int i = rowSize - 1; i >= 0; i--) {
      for (int j = 0; j < columnSize; j++) {
        System.out.println(i);
        char tileLetter = tileRows[j].charAt(i);
        Tile tile = new Tile(getTile(tileLetter), j, i);
        level.addTile(i, j, tile);
        System.out.println(tileLetter);
      }
    }
  }

  /**
   * This method parses the list of rats passed through the level
   * data and then adds them to the map sequentially by getting the type
   * and coordinates needed, then placing the rats.
   *
   * @param ratData The full list of rats and their coordinates for this level
   */
  private static void parseElements(String ratData) {
    String[] seperateRats = ratData.split(SPACE);
    for (int i = 0; i < seperateRats.length; i++) {
      String[] element = seperateRats[i].split(INTERNAL_DATA_DELIMITER);
      int health = Integer.parseInt(element[1]);
      int xPos = Integer.parseInt(element[2]);
      int yPos = Integer.parseInt(element[3]);
      Direction initialDirection = getDirection(element[4]);
      if (element[0].equals("rat")) {

        boolean isMale = Boolean.parseBoolean(element[5]);
          int age = Integer.parseInt(element[6]);
        boolean isSterile = Boolean.parseBoolean(element[7]);
        int timeLeftMating = Integer.parseInt(element[8]);
          boolean isPregnant = Boolean.parseBoolean(element[9]);
        Rat rat = new Rat(ElementType.Rat, level, xPos, yPos, isMale, initialDirection, health, isSterile);
        rat.setMatingTime(timeLeftMating);
        rat.setAge(age);
        level.addElement(rat);
      } else if (element[0].equals("bomb")) {
        Bomb bomb = new Bomb(ElementType.Bomb, level,
                             xPos, yPos, health);
        level.addElement(bomb);
      } else if (element[0].equals("deathRat")) {
          int age = Integer.parseInt(element[5]);
        DeathRat deathRat = new DeathRat(ElementType.DeathRat, level,
                                  xPos, yPos, health, initialDirection);
        deathRat.setAge(age);
        level.addElement(deathRat);
      }else if (element[0].equals("gas")) {
          Gas gas= new Gas(ElementType.Gas, level,
                  xPos, yPos, health);
          level.addElement(gas);
      }
      else if (element[0].equals("sterilize")) {
          Sterilise sterilise= new Sterilise(ElementType.Sterilise, level,
                  xPos, yPos, health);
          level.addElement(sterilise);
      }
      else if (element[0].equals("maleGenderChange")) {
          MaleChanger maleChanger= new MaleChanger(ElementType.Sterilise, level,
                  xPos, yPos, health);
          level.addElement(maleChanger);
      }else if (element[0].equals("femaleGenderChange")) {
          FemaleChanger femaleChanger= new FemaleChanger(ElementType.Sterilise, level,
                  xPos, yPos, health);
          level.addElement(femaleChanger);
      }else if (element[0].equals("stopSign")) {
          StopSign stopSign= new StopSign(ElementType.StopSign, level,
                  xPos, yPos, health);
          level.addElement(stopSign);
      }else if (element[0].equals("poison")) {
          Poison poison= new Poison(ElementType.Poison, level,
                  xPos, yPos, health);
          level.addElement(poison);
      }else if (element[0].equals("explosion")) {
          Explosion explosion= new Explosion(ElementType.Explosion, level,
                  xPos, yPos, health);
          level.addElement(explosion);
      }
    }
  }

  /**
   * Description.
   *
   * @param sDir a direction for the element to move upon initialisation.
   * @return a direction that the rats can use to begin pathing.
   */
  private static Direction getDirection(String sDir) {
    switch (sDir) {
      case "north":
        return Direction.North;
      case "east":
        return Direction.East;
      case "south":
        return Direction.South;
      case "west":
        return Direction.West;
      default:
        System.out.println("Invalid direction " + sDir);
        return Direction.North;
    }
  }

  private static ArrayList<MenuItem> parseItemData(String itemData) {
    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    String[] items = itemData.split(SPACE);
	
    for (int i = 0; i < items.length; i++) {
      System.out.println(items[i]);
    }

    for (int i = 0; i < items.length; i++) {
      String[] individualItem = items[i].split(INTERNAL_DATA_DELIMITER);
      String itemType = individualItem[0];
      int replenishTimer = Integer.parseInt(individualItem[1]);
      int currentTick = Integer.parseInt(individualItem[2]);
      int age = Integer.parseInt(individualItem[3]);
      MenuItem menuItem = new MenuItem(itemType, replenishTimer, age, currentTick);
      level.addMenuItem(menuItem);
    }
    return menuItems;
  }
}



