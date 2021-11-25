import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 * @version 1
 */
public class InGameController implements Initializable {


    @FXML
    Canvas gameCanvas; //canvas the game is shown on

    @FXML
    ImageView bomb;
    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openLevelMenu();
        Game.pauseGame();

    }


    public void showImages() {

        String filePath = "res\\images\\";
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(filePath + "bomb.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(inputstream);
        bomb.setImage(image);
        bomb.setCache(true);

    }



    /**
     * code run on initalization of level, 
     * allows graphics context to be referenced.
     * 
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game.loadCanvas(gameCanvas.getGraphicsContext2D());
        showImages();
    }
}


