import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 */
public class InGameController implements Initializable {


    @FXML
    Canvas gameCanvas; //canvas the game is shown on
    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openLevelMenu();

    }



    /**
     * code ran on initalization, allows graphics context to be referenced.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


