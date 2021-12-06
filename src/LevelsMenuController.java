import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    private static final int DISPLAY_COUNT = 10;

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
                Button levelButton = new Button("level: " + buttonText);
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

                                ButtonType resumeButton = new ButtonType("resume", ButtonBar.ButtonData.OK_DONE);
                                ButtonType overwriteButton = new ButtonType("overwrite", ButtonBar.ButtonData.CANCEL_CLOSE);

                                Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "", resumeButton, overwriteButton);
                                saveConfirmation.setTitle("resume / overwrite");
                                saveConfirmation.setHeaderText("there is a save file for this level.");

                                Optional<ButtonType> resume = saveConfirmation.showAndWait();
                                if (resume.get().equals(resumeButton)) {
                                    Game.openGameScene("save\\" + Game.currentProfile.getUsername() + saveFileName);
                                } else {
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


                Tooltip leaderboard = getLevelLeaderboardHover(buttonText);
                levelButton.setTooltip(leaderboard);
                levelPane.getChildren().add(levelButton);
            }
        }
        levelPane.setPrefColumns(3);
    }

    /**
     * gives the tooltip for each level buttons leaderboard
     * @param level String name of the level
     * @return ToolTip leaderboard on hoever over the button
     */
    private Tooltip getLevelLeaderboardHover(String level) {
        ArrayList<Score> scores = Leaderboard.getScores(level, 0);
        String text = "leaderboard: \n";

        //use for each instead of for because otherwise you might get out of bounds
        int j = 0;
        for (Score score : scores) {
            j++;
            if (j <= DISPLAY_COUNT) {
                text = text + "#" + j + " " + score + "\n";
            }

        }
        Tooltip leaderboard = new Tooltip();
        leaderboard.setText(text);
        leaderboard.setFont(Font.font("monospace", 20));

        return leaderboard;
    }

    /**
     * updates the buttons for hte levels to reflect current information
     */
    public void refreshButtons() {
        removebuttons();
        addLevelButtons();
    }


    /**
     * removes all the level buttons
     */
    private void removebuttons() {
        while (levelPane.getChildren().size() > 0) {
            levelPane.getChildren().remove(0);
        }
    }


    /**
     * Code ran on initalization, adds level buttons to the tilepane.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game.levelMenuController = this;

    }
}


