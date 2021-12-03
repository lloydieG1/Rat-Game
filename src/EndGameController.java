import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the end game screen.
 *
 * @author William Randle
 */
public class EndGameController implements Initializable {
  @FXML
  Text endgameMessage;

  @FXML
    private VBox leaderboard;

  /**
   * Switches back to the menu.
   */
  @FXML
  private void backClick() {
    Game.openMainMenu();
    Game.currentLevel.deleteSave();
  }


  public void updateLeaderboard() {

      ArrayList<Score> scores = Leaderboard.getScores(Game.currentLevel.level);
      removeScores();

      for (Score score : scores) {
          leaderboard.getChildren().add(new Text(score.toString()));
      }

  }

    private void removeScores() {
        while(leaderboard.getChildren().size() > 0) {
            leaderboard.getChildren().remove(0);
        }
    }

  @FXML
  public void setText(String input) {
    endgameMessage.setText(input);
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

