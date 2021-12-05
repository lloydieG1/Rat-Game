import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

/**
 * launching application which runs the game
 * @author William Randle, Jack Lennard
 */
public class Game extends Application {

    private static final int WIDTH = 1350; //width of the window
    private static final int HEIGHT = 900; //height of the window
    public static final int FPS = 100; //the fps of the game
    
    private static Stage primaryStage; //the stage everything is shown on

    private static Scene userSelection; //the main menu
    private static Scene mainMenu; //the main menu
    private static Scene levelMenu; //the levels menu
    private static Scene endGame; //the levels menu
    private static Scene levelLayout; //the layout of the levels 

    private static GraphicsContext gameGraphics; //canvas to show the graphics on

    private static GraphicsContext minimap;

    private static GraphicsContext ratLives;

    private static Timeline gameLoop; //the loop in which the game runs

    private static Timeline dailyMessageLoop; //the loop for the daily message updating

    public static Level currentLevel;
    
    public static PlayerProfile currentProfile = new PlayerProfile("anon", 0);

    public static Random random = new Random();

    public static double gameSize = 70;

    public static InGameController levelController;

    public static EndGameController endGameController;

    public static MainMenuController mainMenuController;

    public static LevelsMenuController levelMenuController;

    public static final int MAP_WIDTH = 900;
    public static final int MAP_HEIGHT = 900;
    public static double gameX =0;
    public static double gameY = 0;

    public static double scrollX;


    public static int score;

    public static double VISIBLE_TILES = 14;

    private static final int ZOOM_MIN = 50;
    private static final int ZOOM_MAX = 200;
    private static int currentZoomMin = 50;

    public static boolean rightArrow;
    public static boolean leftArrow;
    public static boolean upArrow;
    public static boolean downArrow;




    /**
     * open the main menu and loads the other menus.
     *
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;

        PlayerProfileManager.initializeProfileArray();

        //daily message refreshing
        dailyMessageLoop = new Timeline(new KeyFrame(Duration.millis(30000), (ActionEvent event) -> {
            mainMenuController.refreshDailyMessage();
        }));
        dailyMessageLoop.setCycleCount(Timeline.INDEFINITE);
        
        userSelection = loadScene("userSelection.fxml");
        mainMenu = loadScene("mainMenu.fxml");
        levelMenu = loadScene("levelMenu.fxml");
        levelLayout = loadScene("levelLayout.fxml");
        endGame = loadScene("EndGameScreen.fxml");

      //  primaryStage.setResizable(false);

        openUserSelection();
        primaryStage.setTitle("Rats - by team 19");
        primaryStage.getIcons().add(ImageLoader.icon);
        primaryStage.show();

        int fpstime = 1000/FPS;

        //game loop:
        gameLoop = new Timeline(new KeyFrame(Duration.millis(fpstime), (ActionEvent event) -> {
            tick();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        levelLayout.addEventFilter(KeyEvent.KEY_PRESSED, event -> keyDown(event));
        levelLayout.addEventFilter(KeyEvent.KEY_RELEASED, event -> keyUp(event));
        levelLayout.addEventFilter(ScrollEvent.SCROLL, event -> scrollKeyEvent(event));

    }



    /**
     * scrolls the map with arrow keys
     * @param event
     */
    public void keyDown(KeyEvent event) {

        // We change the behaviour depending on the actual key that was pressed.

        switch (event.getCode()) {
            case RIGHT:
                // Right key was pressed. So move the player right by one cell.
                rightArrow = true;
                break;
            case LEFT:
                // Right key was pressed. So move the player right by one cell.
                leftArrow = true;
                break;
            case UP:
                // Right key was pressed. So move the player right by one cell.
                upArrow = true;
                break;
            case DOWN:
                // Right key was pressed. So move the player right by one cell.
                downArrow = true;
                break;
            default:
                // Do nothing for all other keys.
                break;
        }
                // Consume the event. This means we mark it as dealt with. This stops other GUI nodes (buttons etc) responding to it.
        event.consume();
    }


    /**
     * scrolls the map with arrow keys
     * @param event
     */
    public void keyUp(KeyEvent event) {

        // We change the behaviour depending on the actual key that was pressed.


        switch (event.getCode()) {
            case RIGHT:
                // Right key was pressed. So move the player right by one cell.
                rightArrow = false;
                break;
            case LEFT:
                // Right key was pressed. So move the player right by one cell.
                leftArrow = false;
                break;
            case UP:
                // Right key was pressed. So move the player right by one cell.
                upArrow = false;
                break;
            case DOWN:
                // Right key was pressed. So move the player right by one cell.
                downArrow = false;
                break;
            default:
                // Do nothing for all other keys.
                break;
        }
        // Consume the event. This means we mark it as dealt with. This stops other GUI nodes (buttons etc) responding to it.
        event.consume();
    }

    private static void moveMap() {
        double scroll = (Game.gameSize * 5) / FPS;
        if (rightArrow || levelController.rightArrow) {
            Game.gameX -= scroll;
        }
        if (leftArrow || levelController.leftArrow) {
            Game.gameX += scroll;
        }
        if (upArrow || levelController.upArrow) {
            Game.gameY += scroll;
        }
        if (downArrow || levelController.downArrow) {
            Game.gameY -= scroll;
        }
    }

    /**
     * scrolls the size of the map on the screen
     * @param event
     */
    public void scrollKeyEvent(ScrollEvent event) {
        // We change the behaviour depending on the actual key that was pressed.
        double centerX = gameX + (VISIBLE_TILES / 2 * gameSize);

        double centerY = gameY + (VISIBLE_TILES / 2 * gameSize);

        switch(event.getTextDeltaYUnits()) {
            case LINES:
                // scroll about event.getTextDeltaY() lines

                double scroll = event.getTextDeltaY() * (Math.log((gameSize)) / Math.log(10));


                gameSize = gameSize + scroll;


                //make the scroll happen from the center instead of the top corner
                if (minMax(gameSize, currentZoomMin, ZOOM_MAX) == gameSize) {
                    gameX = centerX - (VISIBLE_TILES / 2 * gameSize);
                    gameY = centerY - (VISIBLE_TILES / 2 * gameSize);
                }

                break;
            case PAGES:
                // scroll about event.getTextDeltaY() pages
                break;
            case NONE:
                // scroll about event.getDeltaY() pixels
                break;
            default:
            	//Do nothing
            	break;
        }

        // Consume the event. This means we mark it as dealt with. This stops other GUI nodes (buttons etc) responding to it.
        event.consume();
    }
    public static void updateScore() {
        levelController.score.setText(Integer.toString(score));
    }

    private static void clampMap() {
        int zoomBefore = (int)gameSize;

        gameSize = minMax(gameSize, currentZoomMin, ZOOM_MAX);
        VISIBLE_TILES = (gameGraphics.getCanvas().getWidth() / gameSize);
        clampMapZoom();


            int mapWidth = currentLevel.getMapBounds()[0];
            int mapHeight = currentLevel.getMapBounds()[1];
            gameY = minMax(gameY, -gameSize * (mapHeight - VISIBLE_TILES), 0);
            gameX = minMax(gameX, -gameSize * (mapWidth - VISIBLE_TILES), 0);


    }

    private static void clampMapZoom() {
        double fractionVisible = 0.5;
        //prevent the map extending too far
        if (VISIBLE_TILES > currentLevel.getMapBounds()[0] * fractionVisible) {
            while (VISIBLE_TILES > currentLevel.getMapBounds()[0] * fractionVisible) {
                gameSize++;
                gameSize = minMax(gameSize, currentZoomMin, ZOOM_MAX);
                VISIBLE_TILES = (gameGraphics.getCanvas().getWidth() / gameSize);
                currentZoomMin = (int) gameSize;
            }
            currentZoomMin = (int) gameSize;
        }
    }


    public static String sidebarAsString() {
        return levelController.sideBarAsString();
    }


    /**
     * runs the logic of the game
     */
    private static void tick() {
        moveMap();
        clampMap();
        currentLevel.tick();
        gameGraphics.setFill(Color.color(0.3, 0.6, 0));
        gameGraphics.fillRect(0, 0, gameGraphics.getCanvas().getWidth(), gameGraphics.getCanvas().getHeight());
        currentLevel.render(gameGraphics);
        updateScore();
        drawButtons(gameGraphics);
        currentLevel.renderMiniMap(minimap);
        currentLevel.renderRatLives(ratLives);
        clampMap();
    }

    private static void drawButtons(GraphicsContext g) {

        int buttonsize = levelController.buttonSize;
        drawButton(g, 0,  0,  MAP_WIDTH, buttonsize);
        drawButton(g, 0,  0,  buttonsize, MAP_HEIGHT);
        drawButton(g, MAP_WIDTH-buttonsize,  0,  MAP_WIDTH, MAP_HEIGHT);
        drawButton(g, 0,  MAP_HEIGHT-buttonsize,  MAP_WIDTH, MAP_HEIGHT);

    }

    private static void drawButton(GraphicsContext g, int x, int y, int width, int height) {
        if(intersect((int)levelController.mouseX, (int)levelController.mouseY, x, y, width, height)) {
            g.setFill(Color.color(0.2, 0.2, 0.2, 0.1));
        } else {
            g.setFill(Color.color(0.2, 0.2, 0.2, 0.7));
        }
        g.fillRect(x, y, width, height);
    }

    /**
     *sets the scene to the user menu
     */
    public static void openUserSelection() {

        primaryStage.setScene(userSelection);
        dailyMessageLoop.stop();
    }
    
    /**
     *changes the menu to the main Menu
     */
    public static void openMainMenu() {
        dailyMessageLoop.play();
        mainMenuController.setProfileText();
        primaryStage.setScene(mainMenu);

    }

    private static boolean intersect(int i, int j, int x, int y, int x2, int y2) {
        if (i > x && i < x2 ) {
            if (j > y && j < y2) {
                return true;
            }
        }
        return false;
    }

    /**
     *changes the menu to the level Menu
     */
    public static void openLevelMenu() {
        levelMenuController.updateCurrentUser();
        dailyMessageLoop.stop();
        levelMenuController.refreshButtons();
        primaryStage.setScene(levelMenu);

    }


    /**
     *changes the menu to the end game scene
     */
    public static void openEndGame() {
        endGameController.updateLeaderboard();
        primaryStage.setScene(endGame);

    }

    public static void resetArrowKeys() {
        rightArrow = false;
        leftArrow = false;
        upArrow = false;
        downArrow = false;
        levelController.rightArrow = false;
        levelController.leftArrow = false;
        levelController.upArrow = false;
        levelController.downArrow = false;

        levelController.wrongScreen = false;
    }

    /**
     *changes the menu to the level Menu
     */
    public static void openGameScene(String levelName) {

        resetArrowKeys();

        currentZoomMin = ZOOM_MIN;
        levelController.resetItems();
        currentLevel = LevelLoader.getLevel(levelName);
        currentLevel.applyStartPosition();
        primaryStage.setScene(levelLayout);
        levelController.setUserNameText(currentProfile.getUsername());
        levelController.setLevelNameText(currentLevel.level);
        gameLoop.play();

    }

    /**
     * pauses the gameLoop
     */
    public static void pauseGame() {
        gameLoop.pause();
    }

    /**
     * loads the canvas to be drawn to in levels
     * @param graphics game graphics
     */
    public static void loadCanvas(GraphicsContext graphics) {
        gameGraphics = graphics;

    }

    /**
     * loads the canvas to be drawn to in levels
     * @param graphics game graphics
     */
    public static void loadRatLives(GraphicsContext graphics) {
        ratLives = graphics;

    }


    /**
     * loads an fxml file from parsed filename string
     * @param fxmlFile string the name of the file to be loaded
     * @return a complete scene which can be shown
     */
    private Scene loadScene (String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            return new Scene(pane,WIDTH,HEIGHT);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Prevents a value being above or below parsed doubles.
     *
     * @param var Value to check
     * @param min Allowed minimum
     * @param max Allowed maximum
     * @return The allowed number within range
     */
    public static double minMax(double var, double min, double max) {
        if(var >= max) {
        	return max;
        } else if (var <= min) {
        	return min;
        } else {
        	return var;
        }

    }


    public static void addItem(ElementType itemType) {
        levelController.addItem(itemType);
    }

    public static void setMiniMap(GraphicsContext g) {
        minimap = g;
    }



    public static void endGame(String message) {
        levelController.wrongScreen =true;
        pauseGame();
        endGameController.setText(message);
        openEndGame();
    }


    /**
     * closes the javafx window.
     */
    public static void quit() {
        primaryStage.close();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
