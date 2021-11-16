import javafx.fxml.FXML;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 */
public class LevelsMenuController {


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

}


