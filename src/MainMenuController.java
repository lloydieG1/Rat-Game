import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the main menu.
 *
 * @author William Randle, Jack Lennard
 */
public class MainMenuController implements Initializable {
			

    String oldMotd = "";
	String motd = ""; //text on screen for message of the day

    @FXML
    Canvas motdCanvas;
	
	@FXML
	Text currentUser; //user that is logged in

    private Timeline swap;

    private double text1Pos; //daily new message position on the screen
    private double text2Pos; //daily old message position on the screen

	public void setProfileText() {
		currentUser.setText("Current User: " + Game.currentProfile.getUsername());
	}
	
	/**	
	 * Switches to level menu when level button is clicked.
	 */
	@FXML
	private void levelsClick() {
		Game.openLevelMenu();
	}
	
	/**
	 * Goes back to user select.
	 */
	@FXML
	private void backClick() {
		Game.openUserSelection();
	}
	
	/**
	 * Quits the game.
	 */
	@FXML
	private void quit() {
		Game.quit();
	}

    public void refreshDailyMessage() {
        text1Pos = 0;
        text2Pos = (int)motdCanvas.getWidth();
        oldMotd = motd;
        try {
            String newMessage = DailyMessage.getMessage(null);

            if (!(newMessage.equals(motd))) {
                motd = newMessage;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!oldMotd.equals(motd)) {
            swap.play();
        }




    }

    private void renderMessage(GraphicsContext g) {
        double moveWidth = motdCanvas.getWidth()/Game.FPS;


        g.setFill(Color.color(0.6,0.6,0.6));
        g.fillRect(0,0, motdCanvas.getWidth(), motdCanvas.getHeight());
        text1Pos-= moveWidth;
        text2Pos-= moveWidth;
        g.setFill(Color.color(1,0.8,0));
        g.setFont(new Font(20));
        g.fillText(oldMotd, text1Pos,20);
        g.fillText(motd, text2Pos,20);
    }


	/**
	 * Code run on initalization, displays updated message of the day.
	 *
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
        int fpstime = 1000/Game.FPS;
        swap = new Timeline(new KeyFrame(Duration.millis(fpstime), (ActionEvent event) -> {
            renderMessage(motdCanvas.getGraphicsContext2D());
        }));
        swap.setCycleCount(Game.FPS);

        Game.mainMenuController = this;

	}
}


