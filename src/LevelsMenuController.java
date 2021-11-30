import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 * @version 2
 */
public class LevelsMenuController implements Initializable {


    @FXML
    TilePane levelPane;


    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openMainMenu();

    }


    /**
     * adds the buttons to the tilepane
     */
    private void addLevelButtons() {
        File directory=new File("res\\maps");
        File[] levels = directory.listFiles();
        int fileCount= directory.list().length;

        for (int i = 0; i < fileCount; i++) {
            if ( !(levels[i].getName().replace(".txt", "").equals(levels[i].getName()))) {
                String buttonText = levels[i].getName().replace(".txt", "");
                Button levelButton = new Button(buttonText);
                levelButton.setFont(new Font(25));

                levelButton.setOnAction(new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        File savesDirectory=new File("res\\maps\\save");
                        File[] saves = directory.listFiles();
                        int fileCount= directory.list().length;
                        for (int i = 0; i < fileCount; i++) {

                            String saveText = saves[i].getName().replace(".txt", "");
                            if (saveText.equals(buttonText)) {
                                Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                saveConfirmation.setTitle("resume / overwrite");
                                saveConfirmation.setHeaderText("there is a save file for this level.");
                                saveConfirmation.setContentText("clicking ok resumes this save, cancel overwrites it.");
                                Optional<ButtonType> resume = saveConfirmation.showAndWait();

                                if (resume.get().equals(ButtonType.OK)) {
                                    Game.openGameScene("save\\" + saveText);

                                } else if (resume.get().equals(ButtonType.CANCEL)) {
                                    Game.openGameScene(buttonText);

                                }
                            }
                        }
                     //   Game.openGameScene(buttonText);
                    }
                });

                levelPane.getChildren().add(levelButton);
            }
        }
        levelPane.setPrefColumns(3);
    }






    /**
     * code ran on initalization, adds level buttons to the tilepane
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addLevelButtons();


    }

}


