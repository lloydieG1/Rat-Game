import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.image.Image;
        import javafx.scene.paint.Color;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.util.ArrayList;

/**
 * scene manager contains and manages all of the tiles and elements in a level
 * @author william randle
 */
public class Level {
 //   private int[] gridSize;
  //  private Tile[][] tileGrid = null;
  //  private ArrayList<Element> elements = new ArrayList<Element>();
 //   private int maxRats;
 //   private int expectedTime;
 //   private ArrayList<Element> levelItemData = new ArrayList<Element>();


  //  /**
   //  * @param gridSize
  //   * @param tileGrid
 //    * @param elements
 //    * @param maxRats
 //    * @param expectedTime
  //   * @param levelItemData
 //    */
  //  public Level(int[] gridSize, Tile[][] tileGrid, ArrayList<Element> elements, int maxRats, int expectedTime,
  //               ArrayList<Element> levelItemData) {
 //       super();
  //      this.gridSize = gridSize;
  //      this.tileGrid = tileGrid;
  //      this.elements = elements;
  //      this.maxRats = maxRats;
  //      this.expectedTime = expectedTime;
  //      this.levelItemData = levelItemData;
  //  }
    private Tile[][] tiles; // 2d array of all the tiles and what they contain

    private ArrayList<Element> elements;
    private ArrayList<Element> nextElements = new ArrayList<>();


    /**
     * constructs a SceneManager
     *
     * @param x width of map
     * @param y height of map
     */
    public Level(int x, int y) {
        tiles = new Tile[x][y];
        elements = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                addTile(i, j ,new Tile(TileType.Grass,i,j));
            }

        }
        System.out.println(x + " " + y);



    }

    /**
     *
     * @param x east/west in map
     * @param y north/south in map
     * @return tile at parsed position
     */
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void addTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    public int getLength() {
        return tiles.length;
    }


    public ArrayList<Element> getElements(int x, int y) {
        ArrayList<Element> stack = new ArrayList<>(); //returning elements at x,y
        for (Element element : elements) {
            if (element.getX() == x && element.getY() == y) {
                stack.add(element);
            }
        }
        return stack;

    }

    public void addElementLive(Element element) {
        nextElements.add(element);

    }

    public void addElement(Element element) {
        elements.add(element);

    }

    public void removeElement(Element element) {
        element.flagRemoval = true;
        removeFlagged();
        // this.elements.remove(element);

    }

    /**
     * removes elements which are flagged for removal
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
     * removes all elements
     */
    public void removeAll() {

        elements = new ArrayList<>();

    }


    public void tick() {

        for (Element element : elements) {

            element.tick();



        }


        for(Element element : nextElements) {
            elements.add(element);

        }
        nextElements = new ArrayList<>();
    }

    public void render(GraphicsContext g) {
        renderTiles(g);

        for (Element element : elements) {
            element.render(g);
        }

    }

    public void renderTiles(GraphicsContext g) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {

                    tiles[i][j].render(g);

            }
        }


    }





}
