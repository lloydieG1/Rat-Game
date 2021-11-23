import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the main menu
 * @author William Randle, Jack Lennard
 */
public class MainMenuController implements Initializable {

    @FXML
    Text motd; //text on screen for message of the day
    /**
     * switches to level menu when level button is clicked
     */
    @FXML
        private void levelsClick() {
            Game.openLevelMenu();

        }



    /**
     * code run on initalization, displays updated message of the day
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
			motd.setText(DailyMessage.getMessage(null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


