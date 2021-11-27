import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the end game screen
 * @author William Randle
 * @version 2
 */
public class EndGameController implements Initializable {

    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openMainMenu();

    }
    @FXML
    Text text;


    @FXML
    public void setText(String input) {
        text.setText(input);
    }


    /**
     * give game access to this class
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game.endGameController = this;


    }
}

