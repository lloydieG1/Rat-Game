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
 *
 * @author William Randle
 * @author Jack Lennard
 */
public class Game extends Application {

    private static final int WIDTH = 1350;
    private static final int HEIGHT = 900;
    public static final int FPS = 100;

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

    public static PlayerProfile currentProfile;

    public static Random random = new Random();

    public static double gameSize = 70;

    public static InGameController levelController;

    public static EndGameController endGameController;

    public static MainMenuController mainMenuController;

    public static LevelsMenuController levelMenuController;

    public static final int MAP_WIDTH = 900;
    public static final int MAP_HEIGHT = 900;
    public static double gameX = 0;
    public static double gameY = 0;

    public static int score;

    public static double visibleTiles = 14;

    private static final int ZOOM_MIN = 50;
    private static final int ZOOM_MAX = 200;
    private static int currentZoomMin = 50;

    public static boolean rightArrow;
    public static boolean leftArrow;
    public static boolean upArrow;
    public static boolean downArrow;


    /**
     * load the menus for navigation, and launch the user selection menu
     *
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {
        currentProfile = new PlayerProfile("anon", 0);

        Game.primaryStage = primaryStage;

        PlayerProfileManager.initializeProfileArray();

        //daily message refreshing
        loadDailyMessage();

        loadMenus();

        openUserSelection();
        primaryStage.setTitle("Rats - by team 19");
        primaryStage.getIcons().add(ImageLoader.icon);
        primaryStage.show();

        loadGameLoop();


    }

    /**
     * loads the game loop which handles all the logic and rendering of the game
     */
    private void loadGameLoop() {
        int fpstime = 1000 / FPS;
        gameLoop = new Timeline(new KeyFrame(Duration.millis(fpstime), (ActionEvent event) -> {
            tick();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * loads the daily message loop which allows scrolling of new daily messages
     */
    private void loadDailyMessage() {
        dailyMessageLoop = new Timeline(new KeyFrame(Duration.millis(30000), (ActionEvent event) -> {
            mainMenuController.refreshDailyMessage();
        }));
        dailyMessageLoop.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * load the menus in the menu system
     */
    private void loadMenus() {
        userSelection = loadScene("userSelection.fxml");
        mainMenu = loadScene("mainMenu.fxml");
        levelMenu = loadScene("levelMenu.fxml");
        levelLayout = loadScene("levelLayout.fxml");
        endGame = loadScene("EndGameScreen.fxml");

        levelLayout.addEventFilter(KeyEvent.KEY_PRESSED, event -> keyDown(event));
        levelLayout.addEventFilter(KeyEvent.KEY_RELEASED, event -> keyUp(event));
        levelLayout.addEventFilter(ScrollEvent.SCROLL, event -> scrollKeyEvent(event));
    }


    /**
     * navigates the map with arrow keys
     *
     * @param event KeyEvent key press which could be arrow keys
     */
    public void keyDown(KeyEvent event) {

        switch (event.getCode()) {
            case RIGHT:

                rightArrow = true;
                break;
            case LEFT:

                leftArrow = true;
                break;
            case UP:

                upArrow = true;
                break;
            case DOWN:

                downArrow = true;
                break;
            default:
                // Do nothing for all other keys.
                break;
        }

        event.consume();
    }


    /**
     * stops navigating when the arrow key is released
     *
     * @param event KeyEvent key press which could be arrow keys
     */
    public void keyUp(KeyEvent event) {

        switch (event.getCode()) {
            case RIGHT:

                rightArrow = false;
                break;
            case LEFT:

                leftArrow = false;
                break;
            case UP:

                upArrow = false;
                break;
            case DOWN:

                downArrow = false;
                break;
            default:
          
                break;
        }

        event.consume();
    }

    /**
     * navigates the map based on which arrow keys are currently pressed
     */
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
     * allows zooming in and out of the map
     *
     * @param event ScrollEvent the scroll the user did
     */
    public void scrollKeyEvent(ScrollEvent event) {

        double centerX = gameX + (visibleTiles / 2 * gameSize);
        double centerY = gameY + (visibleTiles / 2 * gameSize);

        if (event.getTextDeltaYUnits() == ScrollEvent.VerticalTextScrollUnits.LINES) {

            double scroll = event.getTextDeltaY() * (Math.log((gameSize)) / Math.log(10));

            gameSize = gameSize + scroll;

            //make the scroll happen from the center instead of the top corner
            if (minMax(gameSize, currentZoomMin, ZOOM_MAX) == gameSize) {
                gameX = centerX - (visibleTiles / 2 * gameSize);
                gameY = centerY - (visibleTiles / 2 * gameSize);
            }
        }


        event.consume();
    }

    /**
     * updates the score text in the level
     */
    public static void updateScore() {
        levelController.score.setText(Integer.toString(score));
    }

    /**
     * prevents the user navigating outside the map
     */
    private static void clampMap() {

        gameSize = minMax(gameSize, currentZoomMin, ZOOM_MAX);
        visibleTiles = (gameGraphics.getCanvas().getWidth() / gameSize);
        clampMapZoom();


        int mapWidth = currentLevel.getMapBounds()[0];
        int mapHeight = currentLevel.getMapBounds()[1];
        gameY = minMax(gameY, -gameSize * (mapHeight - visibleTiles)
                , 0);
        gameX = minMax(gameX, -gameSize * (mapWidth - visibleTiles)
                , 0);


    }

    /**
     * prevents the user from zooming far enough to see further than intented
     */
    private static void clampMapZoom() {
        double fractionVisible = 0.5;
        //prevent the map extending too far
        if (visibleTiles > currentLevel.getMapBounds()[0] * fractionVisible) {
            while (visibleTiles > currentLevel.getMapBounds()[0]
                    * fractionVisible) {
                gameSize++;
                gameSize = minMax(gameSize, currentZoomMin, ZOOM_MAX);
                visibleTiles = (gameGraphics.getCanvas().getWidth() / gameSize);
                currentZoomMin = (int) gameSize;
            }
            currentZoomMin = (int) gameSize;
        }
    }


    /**
     * gives the sidebar as a string so it can be saved for replay later
     * @return String the number of each item in the side bar
     */
    public static String sidebarAsString() {
        return levelController.sideBarAsString();
    }


    /**
     * runs the logic of the game and renders the game
     */
    private static void tick() {
        moveMap();
        clampMap();
        currentLevel.tick();
        gameGraphics.setFill(Color.color(0.3, 0.6, 0));
        gameGraphics.fillRect(0, 0, gameGraphics.getCanvas().getWidth()
                , gameGraphics.getCanvas().getHeight());
        currentLevel.render(gameGraphics);
        updateScore();
        drawButtons(gameGraphics);
        currentLevel.renderMiniMap(minimap);
        currentLevel.renderRatLives(ratLives);
        clampMap();
    }

    /**
     * draws the buttons which the user can use to navigate the map
     * @param g GraphicsContext the graphics of the game
     */
    private static void drawButtons(GraphicsContext g) {

        int buttonsize = levelController.buttonSize;
        drawButton(g, 0, 0, MAP_WIDTH, buttonsize);
        drawButton(g, 0, 0, buttonsize, MAP_HEIGHT);
        drawButton(g, MAP_WIDTH - buttonsize, 0, MAP_WIDTH, MAP_HEIGHT);
        drawButton(g, 0, MAP_HEIGHT - buttonsize, MAP_WIDTH, MAP_HEIGHT);

    }

    /**
     * draws a button the user could use to navigate the map
     * @param g GraphicsContext the graphics of the game
     * @param x int x position of hte upper left corner
     * @param y int y position of the upper left corner
     * @param width int width of the button
     * @param height int height of the button
     */
    private static void drawButton(GraphicsContext g, int x, int y, int width
            , int height) {
        if (intersect((int) levelController.mouseX, (int) levelController.mouseY
                , x, y, width, height)) {
            g.setFill(Color.color(0.2, 0.2, 0.2, 0.1));
        } else {
            g.setFill(Color.color(0.2, 0.2, 0.2, 0.7));
        }
        g.fillRect(x, y, width, height);
    }

    /**
     * sets the scene to the user menu
     */
    public static void openUserSelection() {

        primaryStage.setScene(userSelection);
        dailyMessageLoop.stop();
    }

    /**
     * changes the menu to the main Menu
     */
    public static void openMainMenu() {
        dailyMessageLoop.play();
        mainMenuController.setProfileText();
        primaryStage.setScene(mainMenu);

    }

    /**
     * returns true if a point (i, j) intersects the parsed dimensions
     * of a rectangle
     * @param x int x position of the point
     * @param y int y position of the point
     * @param x1 int x position of the rectangle
     * @param y1 int y position of the rectangle
     * @param width int width of the rectangle
     * @param height int width of the rectangle
     * @return boolean if the parsed point intersects with the rectangle
     */
    private static boolean intersect(int x, int y, int x1, int y1, int width
            , int height) {
        if (x > x1 && x < width) {
            if (y > y1 && y < height) {
                return true;
            }
        }
        return false;
    }

    /**
     * changes the menu to the level Menu
     */
    public static void openLevelMenu() {
        levelController.stopMusic();
        levelMenuController.updateCurrentUser();
        dailyMessageLoop.stop();
        levelMenuController.refreshButtons();
        primaryStage.setScene(levelMenu);

    }


    /**
     * changes the menu to the end game scene
     */
    public static void openEndGame() {
        levelController.stopMusic();
        endGameController.playAudio();
        endGameController.updateLeaderboard();
        primaryStage.setScene(endGame);
    }

    /**
     * stops all the arrow keys from being active
     */
    public static void resetArrowKeys() {
        rightArrow = false;
        leftArrow = false;
        upArrow = false;
        downArrow = false;

        levelController.resetArrowKeys();

    }

    /**
     * changes the menu to the level Menu
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
        levelController.playMusic();
        gameLoop.play();

    }

    /**
     * pauses the gameLoop
     */
    public static void pauseGame() {

        gameLoop.pause();
    }

    /**
     * loads the graphics
     *
     * @param graphics GraphicsContext game graphics
     */
    public static void loadCanvas(GraphicsContext graphics) {
        gameGraphics = graphics;

    }

    /**
     * loads the graphics for the ratlives indicator
     *
     * @param graphics GraphicsContext ratlives graphics
     */
    public static void loadRatLives(GraphicsContext graphics) {
        ratLives = graphics;

    }


    /**
     * loads an fxml file from parsed filename string
     *
     * @param fxmlFile string the name of the file to be loaded
     * @return a complete scene which can be shown
     */
    private Scene loadScene(String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            return new Scene(pane, WIDTH, HEIGHT);
        } catch (Exception e) {
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
        if (var >= max) {
            return max;
        } else if (var <= min) {
            return min;
        } else {
            return var;
        }

    }


    /**
     * adds an item to the sidebar in the level controller
     * @param itemType ElementType the type of item which is being added
     */
    public static void addItem(ElementType itemType) {
        levelController.addItem(itemType);
    }

    /**
     * laods the graphics for the minimap canvas
     * @param g GraphicsContext the graphics for the minimap
     */
    public static void setMiniMap(GraphicsContext g) {
        minimap = g;
    }


    /**
     * ends the game with a parsed message for the user.
     * @param message String message for the user
     */
    public static void endGame(String message) {
        levelController.wrongScreen = true;
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


    /**
     * launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


}
