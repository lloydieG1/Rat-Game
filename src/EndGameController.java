import javafx.fxml.FXML;

/**
 * this class handles interactions with the end game screen
 * @author William Randle
 * @version 2
 */
public class EndGameController {

    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openMainMenu();

    }



}

