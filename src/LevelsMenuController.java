import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the level menu.
 *
 * @author William Randle
 */
public class LevelsMenuController implements Initializable {

    private static int DISPLAY_COUNT = 10;

  @FXML
  TilePane levelPane;

  @FXML
  Text currentUser;

  public void updateCurrentUser() {
      currentUser.setText("current user: " + Game.currentProfile.getUsername());
  }


  /**
   * Switches back to the menu.
   */
  @FXML
  private void backClick() {
    Game.openMainMenu();
  }


  /**
   * Adds the buttons to the tilepane.
   */
  private void addLevelButtons() {
    File directory = new File("res\\maps");
    File[] levels = directory.listFiles();
    int fileCount = directory.list().length;


    for (int i = 0; i < fileCount; i++) {
      if (!(levels[i].getName().replace(".txt", "").equals(levels[i].getName()))) {
        String buttonText = levels[i].getName().replace(".txt", "");
        String highScore = "highscore: " + Leaderboard.getHighScore(Game.currentProfile.getUsername(), buttonText, 0).getScore();
        String fastest =  "fastest: " + Leaderboard.getHighScore(Game.currentProfile.getUsername(), buttonText, 1).getTime();
        Button levelButton = new Button("level: " + buttonText + "\n" +highScore + "\n" + fastest);
        levelButton.setFont(Font.font("monospace", 35));

        if (PlayerProfileManager.getMaxLevel(Game.currentProfile.getUsername()) >= Integer.parseInt(buttonText)) {
            System.out.println(PlayerProfileManager.getMaxLevel(Game.currentProfile.getUsername()));

            levelButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    File savesDirectory = new File("res\\maps\\save");
                    File[] saves = savesDirectory.listFiles();
                    int fileCount = savesDirectory.list().length;
                    String saveFileName = "";
                    boolean found = false;

                    for (int i = 0; i < fileCount; i++) {
                        String saveText = saves[i].getName().replace(".txt", "");
                        saveText = saveText.replace(Game.currentProfile.getUsername(), "");
                        if (saveText.equals(buttonText)) {
                            saveFileName = saveText;
                            found = true;
                            System.out.println(saveText);
                        }
                    }

                    if (found) {
                        Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        saveConfirmation.setTitle("resume / overwrite");
                        saveConfirmation.setHeaderText("there is a save file for this level.");
                        saveConfirmation.setContentText("clicking ok resumes this save, cancel overwrites it.");
                        Optional<ButtonType> resume = saveConfirmation.showAndWait();
                        if (resume.get().equals(ButtonType.OK)) {
                            Game.openGameScene("save\\" + Game.currentProfile.getUsername() + saveFileName);
                        } else if (resume.get().equals(ButtonType.CANCEL)) {
                            Game.openGameScene(buttonText);
                        }
                    } else {
                        Game.openGameScene(buttonText);
                    }
                }
            });
        } else {
            levelButton.setStyle("-fx-text-fill: grey; -fx-background-color: #555555;");
        }

          ArrayList<Score> scores = Leaderboard.getScores(buttonText, 0);
          String text = "leaderboard: \n";

          int j = 0;
          for (Score score : scores) {
              j++;
              if (j < DISPLAY_COUNT) {
                  text = text + "#" + j + " " +score + "\n";
              }
          }
        Tooltip leaderboard = new Tooltip();
          leaderboard.setText(text);
          leaderboard.setFont(Font.font("monospace", 20));

        levelButton.setTooltip(leaderboard);
        levelPane.getChildren().add(levelButton);
      }
    }
    levelPane.setPrefColumns(3);
  }

  public void refreshButtons() {
      removebuttons();
      addLevelButtons();
  }


    private void removebuttons() {
        while(levelPane.getChildren().size() > 0) {
            levelPane.getChildren().remove(0);
        }
    }


  /**
   * Code ran on initalization, adds level buttons to the tilepane.
   * @param url
   * @param resourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
      Game.levelMenuController = this;
  //  addLevelButtons();
  }
}


