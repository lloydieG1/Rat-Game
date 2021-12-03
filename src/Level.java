import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Scene manager contains and manages all of the tiles and elements in a level.
 *
 * @author William Randle, Lloyd, Adrian, Yazan
 */

public class Level {
  public int timer = 0;
  public String level;
  
  private Tile[][] tiles; // 2d array of all the tiles and what they contain

  private ArrayList<Element> elements;
  private ArrayList<Element> nextElements = new ArrayList<>();
  private ArrayList<MenuItem> menuItems = new ArrayList<>();

  private int maxRats;

  private int xSize;
  private int ySize;
  private int currentTick = 0;
  private int timeLimit;

  private String saveFolder = "res\\maps\\save\\";

  private double startX;
  private double startY;
  private double startZoom;
  private double time;


  /**
   * Constructs a Level.
   *
   * @param x width of map
   * @param y height of map
   */
  public Level(int x, int y, int maxRats, String level, int timeLimit, int timer) {
    tiles = new Tile[x][y];
    elements = new ArrayList<>();
    this.xSize = x;
    this.ySize = y;
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        addTile(i, j, new Tile(TileType.Grass, i, j));
      }
    }
    this.level = level;
    this.maxRats = maxRats;
    this.timeLimit = timeLimit;
    this.timer = timer;
    time = System.nanoTime();
  }

  public void setStart(double zoom, double x, double y) {
      this.startY = y;
      this.startX = x;
      this.startZoom = zoom;
  }

    public void applyStartPosition() {
        Game.gameY = startY;
        Game.gameX = startX;
        Game.gameSize = startZoom;
    }

  public void setScore(int score) {
    Game.score = score;
  }

  public int bonusScore() {
	  return timeLimit - timer + Game.score;
  }

  public int getTimer() {
      return timer;
  }


  /**
   * Get Tile type.
   *
   * @param x horizontal map coordinate
   * @param y Vertical map coordinate
   * @return tile at parsed position
   */
  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  /**
   * Adds a tile of the select Type to the map at coordinates x,y.
   *
   * @param x 
   * @param y 
   * @param tile 
   */
  public void addTile(int x, int y, Tile tile) {
    tiles[x][y] = tile;
  }
    
  /**
   * Get length of map.
   *
   * @return the total amount of tiles on the map
   */
  public int getLength() {
    return tiles.length;
  }

  /**
   * Description.
   *
   * @param x 
   * @param y
   * @return an arraylist of elements that are contained in the tile that has coordinates x,y
   */
  public ArrayList<Element> getElements(int x, int y) {
    ArrayList<Element> stack = new ArrayList<>(); //returning elements at x,y
    for (Element element : elements) {
      if (element.getX() == x && element.getY() == y) {
        stack.add(element);
      }
    }
    return stack;
  }

  /**
   * Gives the bounds of the map.
   *
   * @return
   */
  public int[] getMapBounds() {
    int[] bounds = new int[2];
    bounds[0] = xSize;
    bounds[1] = ySize;
    return bounds;
  }


  /**
   * Description.
   *
   * @return
   */
  public int ratCount() {
    int rats = 0;
    for (Element element : elements) {
      if (element.getType().equals(ElementType.Rat)) {
        rats++;
      }
    }
    return rats;
  }

  private int maleRatCount() {
    int rats = 0;
    for (Element element : elements) {
      if (element.getType().equals(ElementType.Rat)) {
        Rat rat = (Rat) element;
        if (rat.getIsMale()) {
          rats++;
        }
      }
    }
    return rats;

  }


  private void checkGameCondition() {
    int rats = ratCount();
    if (rats > maxRats) {
      Game.endGame("You lost with a score of " + Game.score);
    } else if (rats == 0) {
        double winTime = System.nanoTime();
        double precision = 1000000000;

        Leaderboard.addScore(level,new Score(Game.currentProfile.getUsername(), bonusScore(), (winTime-time)/precision), 0);
        Game.endGame("You won with a score of: \n" + bonusScore() + "\nin:\n" + String.format("%.3f", (winTime-time)/precision) + " seconds");
        PlayerProfileManager.setMaxLevel(Game.currentProfile.getUsername(), level);


    }
    if (timer > timeLimit) {
      Game.endGame("You lost with a score of " + Game.score);
    }
  }

  /**
   * Description.
   *
   * @param element An object that interacts with others on the map
   */
  public void addElementLive(Element element) {
    nextElements.add(element);
  }

  public boolean canAddItem(int x, int y) {
      if (getTile(x,y).getType().equals(TileType.Path)) {

          return true;
      }
      return false;
  }




  /**
   * Description.
   *
   * @param element
   */
  public void addElement(Element element) {
    elements.add(element);
  }
  
  /**
   * Description.
   *
   * @param menuItem
   */
  public void addMenuItem(MenuItem menuItem) {
    menuItems.add(menuItem);
    }
    
  /**
   * Description.
   *
   * @param element
   */
  public void removeElement(Element element) {
    element.flagRemoval = true;
    removeFlagged();
    // this.elements.remove(element);
  }

  /**
   * Removes elements which are flagged for removal.
   */
  public void removeFlagged() {
    ArrayList<Element> oldElements = elements;
    elements = new ArrayList<>();
    for (Element element : oldElements) {
      if (!(element.flagRemoval)) {
        elements.add(element);
      }
    }
  }


  /**
   * Has elements run their tick() behaviours, and adds
   * buffered elements to the map.
   */
  public void tick() {
    currentTick++;
    if (currentTick >= Game.FPS) {
      currentTick = 0;
      timer++;
    }
    for (Element element : elements) {
      element.factor = Game.gameSize;
      element.size = Game.gameSize;
      element.tick();
    }
      elements.addAll(nextElements);
    nextElements = new ArrayList<>();
    tickMenuItems();
    checkGameCondition();
    }
  
  /**
   * Description.
   *
   * @param sideBar
   */
  public void loadSideBar(int[] sideBar) {
    for (int i = 0; i < sideBar[0]; i++) {
      Game.addItem(ElementType.Bomb);
    }
    for (int i = 0; i < sideBar[1]; i++) {
      Game.addItem(ElementType.DeathRat);
    }
    for (int i = 0; i < sideBar[2]; i++) {
      Game.addItem(ElementType.Gas);
    }
    for (int i = 0; i < sideBar[3]; i++) {
      Game.addItem(ElementType.StopSign);
    }

      for (int i = 0; i < sideBar[4]; i++) {
          Game.addItem(ElementType.Sterilise);
      }
      for (int i = 0; i < sideBar[5]; i++) {
          Game.addItem(ElementType.MaleGenderChange);
      }

      for (int i = 0; i < sideBar[6]; i++) {
          Game.addItem(ElementType.FemaleGenderChange);
      }

      for (int i = 0; i < sideBar[7]; i++) {
          Game.addItem(ElementType.Poison);
      }
  }


  private void tickMenuItems() {
    for (MenuItem menuItem : menuItems) {
      menuItem.tick();
    }
  }


  /**
   * Calls to draw elements and tiles on the map.
   *
   * @param g Graphics Context
   */
  public void render(GraphicsContext g) {
    renderTiles(g);

    for (Element element : elements) {
      element.render(g);
    }
    renderGrass(g);
    renderDetails(g);
  }

  /**
   * Draws tiles on the map.
   *
   * @param g
   */
  public void renderTiles(GraphicsContext g) {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
          if (tiles[i][j].getType().equals(TileType.Path)) {
              tiles[i][j].render(g);
          }
      }
    }
  }

  public void renderDetails(GraphicsContext g) {
      for (int i = 0; i < tiles.length; i++) {
          for (int j = 0; j < tiles[i].length; j++) {

                  tiles[i][j].renderDetails(g);

          }
      }
  }
  
  /**
   * Description.
   *
   * @param g
   */
  public void renderGrass(GraphicsContext g) {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
          TileType type = tiles[i][j].getType();
        if (type.equals(TileType.Tunnel) || type.equals(TileType.Grass)) {
          tiles[i][j].render(g);
        }
      }
    }
  }

  /**
   * Description.
   *
   * @param g
   */
  public void renderMiniMap(GraphicsContext g) {
      g.setFill(Color.color(0.3, 0.6, 0));
      g.fillRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
      double widthX = Game.currentLevel.getMapBounds()[0];
      double widthY = Game.currentLevel.getMapBounds()[1];
      double width = widthX;
      if (widthX < widthY) {
          width = widthY;
      }
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
        tiles[i][j].minirender(g, width);
      }
    }
    
    g.setFill(Color.color(0, 0, 0, 0.2));
    double mapFactorY = (Game.MAP_HEIGHT / g.getCanvas().getHeight())
             * (width * 1.0 / Game.VISIBLE_TILES);
    
    double mapFactorX = (Game.MAP_WIDTH / g.getCanvas().getWidth())
             * (width * 1.0 / Game.VISIBLE_TILES);
    
    g.fillRect(0, 0, g.getCanvas().getWidth(), - Game.gameY / mapFactorY);
    g.fillRect(0, 0, -Game.gameX / mapFactorX, g.getCanvas().getHeight());
    
    double tilewidth = g.getCanvas().getWidth() / width;
    double tileheight = g.getCanvas().getHeight() / width;

    g.fillRect(0, -Game.gameY / mapFactorY + (Game.VISIBLE_TILES) * tileheight,
               g.getCanvas().getWidth(), g.getCanvas().getHeight());
    g.fillRect(-Game.gameX / mapFactorX + (Game.VISIBLE_TILES) * tilewidth,
               0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
  }
  
  /**
   * Description.
   */
  public void deleteSave() {
    File f = new File(saveFolder +Game.currentProfile.getUsername() +level + ".txt");
    if (f.exists()) {
      try {
        Files.delete(Paths.get(saveFolder + Game.currentProfile.getUsername() +level + ".txt"));
        System.out.println("deleting save");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Description.
   *
   * @param g
   */
  public void renderRatLives(GraphicsContext g) {
    double sizeFactor = g.getCanvas().getHeight() / maxRats;
    g.setFill(Color.color(0, 0, 0));
    g.fillRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
    g.setFill(Color.color(0, 0.3, 1));
    g.fillRect(0, g.getCanvas().getHeight() - maleRatCount() * sizeFactor,
               g.getCanvas().getWidth(), maleRatCount() * sizeFactor);
    g.setFill(Color.color(1, 0.3, 1));
    g.fillRect(0, g.getCanvas().getHeight() - ratCount() * sizeFactor,
               g.getCanvas().getWidth(), (ratCount() - maleRatCount()) * sizeFactor);
  }

  /**
   * Description.
   */
  public void saveFile() {
    String lines = "/\n";
    String file = getMapBounds()[0] + "," + getMapBounds()[1];
    file = file + "\n" + lines;
    for (int i = 0; i < getMapBounds()[1]; i++) {
    	
      for (int j = 0; j < getMapBounds()[0]; j++) {
        file = file + tiles[j][i].asString();
      }


      file = file + ";\n";
    }
    file = file + "\n" + lines;

    file = file + maxRats;

    file = file + "\n" + lines;

    for (Element element : elements) {
      file = file + element.asString() + " ";
    }

    file = file + "\n" + lines;
    file = file + timeLimit;
    file = file + "\n" + lines;
    file = file + timer;
    file = file + "\n" + lines;
    for (MenuItem menuItem : menuItems) {
      file = file + menuItem.asString() + " ";
    }
    file = file + "\n" + lines;
    file = file + Game.score;
    file = file + "\n" + lines;
    file = file + Game.sidebarAsString();
    file = file + "\n" + lines;
    file = file +  Game.gameSize;
      file = file + "\n" + lines;
      file = file +  Game.gameX;
      file = file + "\n" + lines;
      file = file +  Game.gameY;
    try {
      File levelFile = new File(saveFolder + Game.currentProfile.getUsername() + level + ".txt");
      if (levelFile.createNewFile()) {
        System.out.println("save: " + levelFile.getName());
      } else {
        System.out.println("save already exists. now writing to it");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    try {
      FileWriter myWriter = new FileWriter(saveFolder + Game.currentProfile.getUsername() + level + ".txt");
      myWriter.write(file);
      myWriter.close();
      System.out.println("Successfully saved");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }


  private void saveScore() {

  }
}
