import javafx.fxml.FXML;

/**
 * this class handles interactions with the main menu
 * @author William Randle
 */
public class MainMenuController {


    /**
     * switches to level menu when level button is clicked
     */
    @FXML
        private void levelsClick() {
            Game.openLevelMenu();

        }

}


