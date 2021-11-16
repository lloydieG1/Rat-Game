import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * launching application which runs the game
 * @author William Randle, Jack Lennard
 *
 */
public class Game extends Application {

    private static int WIDTH = 1350; //width of the window
    private static int HEIGHT = 900; //height of the window

    private static Stage primaryStage; //the stage everything is shown on

    private static Scene mainMenu; //the main menu
    private static Scene levelMenu; //the levels menu
    private static Scene ingameScene; //the levels menu

    /**
     * open the main menu and loads the other menus
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;


        mainMenu = loadScene("mainMenu.fxml");
        levelMenu = loadScene("levelMenu.fxml");
        ingameScene = loadScene("ingameScene.fxml");

        openMainMenu();
        primaryStage.show();
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
     *changes the menu to the level Menu
     */
    public static void openGameScene(int level) {
        primaryStage.setScene(ingameScene);

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


    public static void main(String[] args) {
        launch(args);
    }



}