import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

    private int sortBy = 0;


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

      ArrayList<Score> scores = Leaderboard.getScores(Game.currentLevel.level, sortBy);
      removeScores();

      Text header = new Text("leaderboard: ");
      header.setFont(new Font(25));

      leaderboard.getChildren().add(header);
      for (Score score : scores) {
          Text scoreT = new Text(score.toString());
          scoreT.setFont(new Font(20));

          leaderboard.getChildren().add(scoreT);
      }

  }

    private void removeScores() {
        while(leaderboard.getChildren().size() > 0) {
            leaderboard.getChildren().remove(0);
        }
    }

    @FXML
    private void sortBy() {
      sortBy++;
      if (sortBy > 1) {
          sortBy=0;
      }
      updateLeaderboard();

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

