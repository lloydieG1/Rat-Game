import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class UserSelectionController implements Initializable {
	  /**
	   * Quits the game.
	   */
	  @FXML
	  private void quit() {
	    Game.quit();
	  }
	  
	  @Override
	  public void initialize(URL url, ResourceBundle resourceBundle) {
	  }
	}
