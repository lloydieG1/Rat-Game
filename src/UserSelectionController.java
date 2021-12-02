import java.io.File;
import java.io.IOException;
import java.net.URL;
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
 *
 * @author lloyd, William Randle
 */
public class UserSelectionController implements Initializable {
	@FXML
	TextField usernameInput;
	
	@FXML
	TilePane profileVbox;
	
	private void addProfileButtons() {
		File directory = new File("res\\profiles");
		File[] profiles = directory.listFiles();
		int fileCount = directory.list().length;

		for (int i = 0; i < fileCount; i++) {
			String buttonText = profiles[i].getName().replace(".txt", "");
		    Button profileButton = new Button(buttonText);
		    profileButton.setFont(new Font(25));
		    profileButton.setOnAction(new EventHandler() {
		    	@Override
		    	public void handle(Event event){
		    		profileButtonClick(buttonText);//button text is the same as the username
		    	}
		    });

		    profileVbox.getChildren().add(profileButton); 
		  }
	}

	
	private void profileButtonClick(String username) {
		Game.currentProfile = PlayerProfileManager.getProfile(username);
		Game.openMainMenu();
	}
	
	@FXML
	private void createUserClick() {
        if (!(usernameInput.getText() == null) || !(usernameInput.getText().equals(" "))) {
            PlayerProfileManager.addNewProfile(usernameInput.getText());
            removeProfiles();
            addProfileButtons();
        }
	}


    private void removeProfiles() {
        while(profileVbox.getChildren().size() > 0) {
            profileVbox.getChildren().remove(0);
        }
    }
	
	@FXML
	private void removeUserClick() {

        if (!(usernameInput.getText() == null) || !(usernameInput.getText().equals(" "))) {
            PlayerProfileManager.removeProfile(usernameInput.getText());
            removeProfiles();
              addProfileButtons();
        }
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
