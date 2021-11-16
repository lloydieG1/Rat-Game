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
     * switches to level 1
     */
    @FXML
    private void level1() {
        Game.openGameScene(1);

    }


    /**
     * code ran on initalization, allows graphics context to be referenced.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File directory=new File("res\\maps");
        File[] levels = directory.listFiles();
        int fileCount= directory.list().length;
        for (int i = 0; i < fileCount; i++) {
            Button levelButton = new Button(levels[i].getName().replace(".txt", ""));
            levelButton.setFont(new Font(25));

            levelPane.getChildren().add(levelButton);
        }

    }

}


