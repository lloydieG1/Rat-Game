import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the main menu.
 *
 * @author William Randle, Jack Lennard
 */
public class MainMenuController implements Initializable {
			

    String oldMotd = "";
	String motd = ""; //text on screen for message of the day

    private int fontSize = 30;

    private final int MAX_WRAP = 75;
    private final int TEXT_WRAP = 65;

    private final int GRID_OFFSET = 8;

    private final double LINE_FACTOR = 1.6666;


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

    private boolean differentEnough(String s1, String s2) {
        if (s1.equals(s2)) {
            return false;
        }

        return true;
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
        System.out.println(motd);
        text1Pos = 0;
        text2Pos = motdCanvas.getHeight();
        oldMotd = motd;
        try {
            String newMessage = " " + DailyMessage.getMessage(null);
            if (differentEnough(newMessage, motd)) {
                motd = newMessage;
                swap.play();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }





    }

    private void renderMessage(GraphicsContext g) {


        double moveHeight = motdCanvas.getHeight()/Game.FPS;
        text1Pos-= moveHeight;
        text2Pos-= moveHeight;

        g.setFill(Color.color(0.6,0.6,0.6));
        g.fillRect(0,0, motdCanvas.getWidth(), motdCanvas.getHeight());

        g.setStroke(Color.color(0,0,0, 0.1));
        for (int i = 0; i < MAX_WRAP; i++) {
            g.strokeRect(0,0, i*fontSize/LINE_FACTOR, motdCanvas.getHeight());
        }
        for (int i = 0; i < MAX_WRAP; i++) {
            g.strokeRect(0,0, motdCanvas.getWidth(), i*fontSize+GRID_OFFSET-text1Pos - motdCanvas.getHeight());
        }


        g.setFill(Color.color(1,0.8,0));
        g.setFont(Font.font("monospace", FontWeight.NORMAL,fontSize));
        renderMotd(motdLines(oldMotd), text1Pos, g);
        renderMotd(motdLines(motd), text2Pos, g);
    }

    private void renderGrid(GraphicsContext g) {

        //

    }

    private void renderMotd(ArrayList<String> lines, double position, GraphicsContext g) {
        for (int i = 0; i < lines.size(); i++) {
            g.fillText(lines.get(i), 0,fontSize+i*fontSize - position);
        }
    }

    private ArrayList<String> motdLines(String motd) {
        motd = motd + " ";
        ArrayList<String> lines = new ArrayList<>();
        while(motd.length()> TEXT_WRAP) {
            int pos = TEXT_WRAP;
            while (!(motd.charAt(pos) == ' ' || motd.charAt(pos) == '(' || pos >= MAX_WRAP)) {
                pos++;
            }
            lines.add(motd.substring(0,pos));
            motd = motd.substring(pos);
        }
        lines.add(motd);
        return lines;
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
        refreshDailyMessage();

	}
}


