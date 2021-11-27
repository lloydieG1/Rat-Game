import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
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
    public static final int FPS = 24; //the fps of the game
    
    private static Stage primaryStage; //the stage everything is shown on

    private static Scene mainMenu; //the main menu
    private static Scene levelMenu; //the levels menu
    private static Scene endGame; //the levels menu
    private static Scene levelLayout; //the layout of the levels 

    private static GraphicsContext gameGraphics; //canvas to show the graphics on

    private static Timeline gameLoop; //the loop in which the game runs


    public static Level currentLevel;

    public static Random random = new Random();

    public static int gameSize = 70;

    public static double factor = 70;

    public static InGameController levelController;

    public static int gameX =0;
    public static int gameY = 0;
    public static int score;

    /**
     * open the main menu and loads the other menus
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;


        mainMenu = loadScene("mainMenu.fxml");
        levelMenu = loadScene("levelMenu.fxml");
        levelLayout = loadScene("levelLayout.fxml");
        endGame = loadScene("EndGameScreen.fxml");

        openMainMenu();
        primaryStage.show();

        int fpstime = 1000/FPS;

        //game loop:
        gameLoop = new Timeline(new KeyFrame(Duration.millis(fpstime), (ActionEvent event) -> {
            tick();
        }));


        gameLoop.setCycleCount(Timeline.INDEFINITE);

        levelController.addBomb();
        levelController.addBomb();
        levelController.addDeathRat();

        levelLayout.addEventFilter(KeyEvent.KEY_PRESSED, event -> processKeyEvent(event));

    }


    /**
     * scrolls the map with arrow keys
     * @param event
     */
    public void processKeyEvent(KeyEvent event) {
        // We change the behaviour depending on the actual key that was pressed.

        int scroll = 50;
        switch (event.getCode()) {
            case RIGHT:
                // Right key was pressed. So move the player right by one cell.
                Game.gameX-=scroll;
              //  clampMap();
                break;
            case LEFT:
                // Right key was pressed. So move the player right by one cell.
                Game.gameX+=scroll;
               // clampMap();
                break;
            case UP:
                // Right key was pressed. So move the player right by one cell.
                Game.gameY+=scroll;
             //   clampMap();
                break;
            case DOWN:
                // Right key was pressed. So move the player right by one cell.
                Game.gameY-=scroll;
              //  clampMap();
                break;
            default:
                // Do nothing for all other keys.
                break;
        }


        // Consume the event. This means we mark it as dealt with. This stops other GUI nodes (buttons etc) responding to it.
        event.consume();
    }

    public static void updateScore() {
        levelController.score.setText(Integer.toString(score));
    }





    /**
     * runs the logic of the game
     */
    private static void tick() {
        currentLevel.tick();
        gameGraphics.setFill(Color.color(0.3,0.6,0));
        gameGraphics.fillRect(0,0,gameGraphics.getCanvas().getWidth(), gameGraphics.getCanvas().getHeight());
        currentLevel.render(gameGraphics);
        updateScore();
    }

    /**
     *changes the menu to the main Menu
     */
    public static void openMainMenu() {
        primaryStage.setScene(mainMenu);

    }

    /**
     *changes the menu to the level Menu
     */
    public static void openLevelMenu() {
        primaryStage.setScene(levelMenu);

    }


    /**
     *changes the menu to the end game scene
     */
    public static void openEndGame() {
        primaryStage.setScene(endGame);

    }

    /**
     *changes the menu to the level Menu
     */
    public static void openGameScene(String levelName) {

        currentLevel = LevelLoader.getLevel(levelName);
        primaryStage.setScene(levelLayout);
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



    public static void endGame() {
        pauseGame();
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
