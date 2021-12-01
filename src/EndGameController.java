import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the end game screen.
 *
 * @author William Randle
 */
public class EndGameController implements Initializable {
  @FXML
  Text text;

  /**
   * Switches back to the menu.
   */
  @FXML
  private void backClick() {
    Game.openMainMenu();
    Game.currentLevel.deleteSave();
  }

  @FXML
  public void setText(String input) {
    text.setText(input);
  }

  /**
   * Give game access to this class.
   *
   * @param url
   * @param resourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Game.endGameController = this;
  }
}

