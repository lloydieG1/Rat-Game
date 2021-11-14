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


    private static Stage primaryStage;

    private static Scene mainMenu;

    /**
     * open the main menu
     * @param primaryStage stage javafx shows things on
     */
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;



        loadMainMenu();
        openMainMenu();
        primaryStage.show();
    }

    public void loadMainMenu () {
        BorderPane menuPane;

        try {
            menuPane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            mainMenu = new Scene(menuPane,1350,900);
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









    public static void main(String[] args) {
        launch(args);
    }



}