import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author lloyd
 * @author William Randle
 */
public class UserSelectionController implements Initializable {
    @FXML
    TextField usernameInput;

    @FXML
    TilePane profileVbox;

    private void addProfileButtons() {

        for (int i = 0; i < PlayerProfileManager.getSize(); i++) {
            String buttonText = PlayerProfileManager.getProfileInt(i).getUsername();
            Button profileButton = new Button(buttonText);
            profileButton.setFont(Font.font("monospace", 25));
            profileButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    profileButtonClick(buttonText);//button text is the same as the username
                }
            });
            profileVbox.getChildren().add(profileButton);
        }


    }


    /**
     * sets the profile to parsed username
     * @param username String username of the profile for whom we want's profile
     */
    private void profileButtonClick(String username) {
        Game.currentProfile = PlayerProfileManager.getProfile(username);
        Game.openMainMenu();
    }


    /**
     * creates a user with username the text of the usernameInput
     * if it is a valid username.
     */
    @FXML
    private void createUserClick() {
        String userName = usernameInput.getText();

        //check the validity of the name
        ArrayList<Boolean> valid = new ArrayList<>();
        valid.add(!(userName.length() > 10));
        valid.add(!userName.equals(""));
        valid.add(!userName.contains("level"));
        valid.add(!illegalString(userName));
        valid.add(!playerExists(userName));

        if (valid.contains(false)) {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR);
            invalidDataAlert.setTitle("incorrect input:");
            invalidDataAlert.setHeaderText(getErrorDetail(userName));
            Optional<ButtonType> resume = invalidDataAlert.showAndWait();

        } else {
            PlayerProfileManager.addNewProfile(userName);
            removeProfiles();
            addProfileButtons();
        }
    }

    /**
     * checks if a player exists
     * @param username String for the profile we want to checks existance
     * @return boolean if the player exists
     */
    private boolean playerExists(String username) {
        return PlayerProfileManager.userExists(username);
    }


    /**
     *check if the username if a valid string for an account
     * @param username String the username we want to check
     * @return boolean if the parsed username is valid
     */
    private boolean illegalString(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (illegalChar(username.charAt(i))) {
                return true;
            }

        }
        return false;
    }


    /**
     * checks if a letter is illegal
     * @param letter char the letter we are checking
     * @return boolean if the character is illegal
     */
    private boolean illegalChar(char letter) {
        if ((int) letter <= 90 && (int) letter >= 65) {
            return false;
        } else if ((int) letter <= 122 && (int) letter >= 97) {
            return false;
        } else if (((int) letter <= 57 && (int) letter >= 48)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * removes the profiles buttons on display
     */
    private void removeProfiles() {
        while (profileVbox.getChildren().size() > 0) {
            profileVbox.getChildren().remove(0);
        }
    }

    /**
     * logs into the username in the usernameInput if it is valid
     */
    @FXML
    private void select() {
        String userName = usernameInput.getText();
        ArrayList<Boolean> valid = new ArrayList<>();
        valid.add(playerExists(userName));

        if (valid.contains(false)) {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR);
            invalidDataAlert.setHeaderText("this user doesn't exist");
            Optional<ButtonType> resume = invalidDataAlert.showAndWait();
        } else {
            profileButtonClick(userName);
        }
    }

    /**
     * removes a users profile and saves and highscores
     */
    @FXML
    private void removeUserClick() {

        String userName = usernameInput.getText();

        //check validity
        ArrayList<Boolean> valid = new ArrayList<>();
        valid.add(playerExists(userName));

        if (valid.contains(false)) {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR);
            invalidDataAlert.setHeaderText("this user doesn't exist");
            Optional<ButtonType> resume = invalidDataAlert.showAndWait();


        } else {
            Alert removePlayerConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            removePlayerConfirm.setTitle("Remove player profile");
            removePlayerConfirm.setHeaderText("are you sure you want to remove " + usernameInput.getText() + "?");
            removePlayerConfirm.setContentText("removing this profile deletes all the information for that user");
            Optional<ButtonType> resume = removePlayerConfirm.showAndWait();
            if (resume.get().equals(ButtonType.OK)) {
                PlayerProfileManager.removeProfile(usernameInput.getText());
                removeProfiles();
                addProfileButtons();
            }
        }

    }

    /**
     * gets the errors to be given to the user when the data they input is wrong
     * @param userName String username we want to find the errors for
     * @return String the errors formatted line by line
     */
    private String getErrorDetail(String userName) {
        String error = "";

        if ((userName.length() > 10)) {
            error = error + "Name is too long. \n";
        }
        if (userName.equals("")) {
            error = error + "Name is too short. \n";
        }
        if (userName.contains("level")) {
            error = error + "Name contains illegal String 'level'. \n";
        }
        if (illegalString(userName)) {
            error = error + "Name contains non letter/num character. \n";
        }
        if (playerExists(userName)) {
            error = error + "player already exists! \n";
        }
        return error;
    }


    /**
     * Quits the game.
     */
    @FXML
    private void quit() {
        Game.quit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProfileButtons();
    }
}
