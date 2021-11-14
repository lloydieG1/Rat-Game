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


    private static Stage primaryStage; //the stage everything is shown on

    private static Scene mainMenu; //the main menu

    private static Scene levelMenu; //the levels menu

    /**
     * open the main menu
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;


        loadLevelMenu();
        loadMainMenu();

        openMainMenu();
        primaryStage.show();
    }


    /**
     * loads the menu fxml file into the mainmenu scene.
     */
    private void loadMainMenu () {
        BorderPane menuPane;

        try {
            menuPane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            mainMenu = new Scene(menuPane,1350,900);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * loads the level fxml file into the levelMenu scene.
     */
    private void loadLevelMenu () {
        BorderPane levelPane;

        try {
            levelPane = FXMLLoader.load(getClass().getResource("levelMenu.fxml"));
            levelMenu = new Scene(levelPane,1350,900);
        } catch(Exception e) {
            e.printStackTrace();
        }

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







    public static void main(String[] args) {
        launch(args);
    }



}