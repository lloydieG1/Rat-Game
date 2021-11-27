import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * scene manager contains and manages all of the tiles and elements in a level
 * @author william randle, lloyd, adrian, yazan
 * @version 1
 */
/**
 * @author A B
 *
 */
public class Level {
    private Tile[][] tiles; // 2d array of all the tiles and what they contain

    private ArrayList<Element> elements;
    private ArrayList<Element> nextElements = new ArrayList<>();
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    private int maxRats;

    /**
     * constructs a Level
     *
     * @param x width of map
     * @param y height of map
     */
    public Level(int x, int y, int maxRats) {
        tiles = new Tile[x][y];
        elements = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                addTile(i, j ,new Tile(TileType.Grass,i,j));
            }

        }


        this.maxRats = maxRats;
        System.out.println(maxRats);
    }

    /**
     *
     * @param x horizontal map coordinate
     * @param y Vertical map coordinate
     * @return tile at parsed position
     */
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     * @param x 
     * @param y 
     * @param tile 
     * adds a tile of the selected Type to the map at
     * coordinates x,y
     */
    public void addTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }
    
    /**
     * @return the total amount of tiles on the map
     */
    public int getLength() {
        return tiles.length;
    }

    /**
     * @param x 
     * @param
     * @return an arraylist of elements that are contained in the tile
     * that has coordinates x,y
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



    public int ratCount() {
        int rats = 0;
        for (Element element : elements) {
            if (element.getType().equals(ElementType.Rat)) {
                rats++;
            }
        }
        return rats;

    }


    private void checkGameCondition() {
        int rats = ratCount();
        if (rats > maxRats) {
            Game.endGame();
            System.out.println("you lost: too many rats");
        } else if (rats == 0) {
            Game.endGame();
            System.out.println("you won: no more rats");
        }


    }

    /**
     * @param element An object that interacts with others on the map
     */
    public void addElementLive(Element element) {
        nextElements.add(element);

    }

    /**
     * @param element
     */
    public void addElement(Element element) {
        elements.add(element);

    }

    /**
     * @param menuItem
     */
    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);

    }
    
    /**
     * @param element
     */
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
     * removes all elements from the map
     */
    public void removeAll() {

        elements = new ArrayList<>();

    }

    /**
     * has elements run their tick() behaviours, and adds
     * buffered elements to the map
     */
    public void tick() {
        for (Element element : elements) {
            element.tick();
        }


        for(Element element : nextElements) {
            elements.add(element);
        }
        nextElements = new ArrayList<>();

        checkGameCondition();
    }

    /**
     * calls to draw elements and tiles on the map.
     * @param g Graphics Context
     */
    public void render(GraphicsContext g) {
        renderTiles(g);

        for (Element element : elements) {
            element.render(g);
        }

        renderTunnels(g);
    }

    /**
     * draws tiles on the map.
     * @param g
     */
    public void renderTiles(GraphicsContext g) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].render(g);
            }
        }
    }

    public void renderTunnels(GraphicsContext g) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if( tiles[i][j].getType().equals(TileType.Tunnel)) {
                    tiles[i][j].render(g);
                }
            }
        }
    }
}
