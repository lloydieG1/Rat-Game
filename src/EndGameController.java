import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;
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

    private final double AUDIO_VOLUME = 0.2;
    private final String AUDIO_FILE_PATH = "res/audio/FOOLISHLITTLERATS.mp3";
    private Media audio = new Media(new File(AUDIO_FILE_PATH).toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(audio);// initialise music player

    @FXML
    private Button sortByButton;


    @FXML
    Text endgameMessage;

    @FXML
    private VBox leaderboard;

    /**
     * Switches back to the menu.
     */
    @FXML
    private void backClick() {
        Game.openLevelMenu();
        Game.currentLevel.deleteSave();
    }

    /*
     * Play audio at stored at AUDIO_FILE_PATH
     */
    public void playAudio() {
        mediaPlayer.play();
        mediaPlayer.seek(Duration.ZERO); //restarts sound for next time it is played
    }

    /**
     * Update the leader board.
     */
    public void updateLeaderboard() {

        ArrayList<Score> scores = Leaderboard.getScores(Game.currentLevel.level, sortBy);
        removeScores();

        int i = 0;
        for (Score score : scores) {
            i++;
            Text scoreT = new Text("#" + i + " " + score.toString());
            scoreT.setFont(Font.font("monospace", 35));

            leaderboard.getChildren().add(scoreT);
        }
    }

    private void removeScores() {
        while (leaderboard.getChildren().size() > 0) {
            leaderboard.getChildren().remove(0);
        }
    }

    @FXML
    private void sortBy() {
        sortBy++;
        if (sortBy > 1) {
            sortBy = 0;
        }
        updateLeaderboard();

        if (sortBy == 0) {
            sortByButton.setText("order by time");
        }
        if (sortBy == 1) {
            sortByButton.setText("order by score");
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

