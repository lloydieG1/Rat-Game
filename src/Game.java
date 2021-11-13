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



    public void start(Stage primaryStage) {

        BorderPane menuPane = null;
        Scene menu = null;
        try {
            menuPane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            menu = new Scene(menuPane,1350,900);
            primaryStage.setScene(menu);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(menu);
    }










    public static void main(String[] args) {
        launch(args);
    }



}