import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 */
public class LevelsMenuController implements Initializable {

    @FXML
    TilePane levelPane;

    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openMainMenu();

    }


    /**
     * adds the buttons to the tilepane
     */
    private void addLevelButtons() {
        File directory=new File("res\\maps");
        File[] levels = directory.listFiles();
        int fileCount= directory.list().length;

        for (int i = 0; i < fileCount; i++) {
            String buttonText = levels[i].getName().replace(".txt", "");
            Button levelButton = new Button(buttonText);
            levelButton.setFont(new Font(25));

            levelButton.setOnAction(new EventHandler() {

                @Override
                public void handle(Event event) {
                    Game.openGameScene(buttonText);
                }
            });

            levelPane.getChildren().add(levelButton);
        }
    }


    /**
     * code ran on initalization, adds level buttons to the tilepane
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       addLevelButtons();

    }

}


