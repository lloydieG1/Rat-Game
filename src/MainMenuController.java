import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the main menu.
 *
 * @author William Randle, Jack Lennard
 */
public class MainMenuController implements Initializable {

  @FXML
  Text motd; //text on screen for message of the day
  /**
   * Switches to level menu when level button is clicked.
   */
  
  @FXML
  private void levelsClick() {
    Game.openLevelMenu();
  }

  @FXML
  private void changeUser() {
      Game.openUserSelection();
  }
  
  /**
   * Quits the game.
   */
  @FXML
  private void quit() {
    Game.quit();
  }

  /**
   * Code run on initalization, displays updated message of the day.
   *
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


