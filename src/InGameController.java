import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 * @author William Randle
 * @version 1
 */
public class InGameController implements Initializable {


    @FXML
    Canvas gameCanvas; //canvas the game is shown on

    @FXML
    ImageView bomb;
    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openLevelMenu();
        Game.pauseGame();

    }


    public void showImages() {

        String filePath = "res\\images\\";
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(filePath + "bomb.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(inputstream);
        bomb.setImage(image);
        bomb.setCache(true);

    }

    /**
     * React when an object is dragged onto the canvas.
     * @param event The drag event itself which contains data about the drag that occurred.
     */
    public void canvasDragDroppedOccured(DragEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Print a string showing the location.



        placeItem((int)x, (int)y, ElementType.Bomb);
    }

    /**
     * place an item into the level
     * @param x
     * @param y
     */
    public static void placeItem(int x, int y, ElementType type) {
        if (type.equals(ElementType.Bomb)) {
         Game.currentLevel.addElement(new Bomb(ElementType.Bomb,  Game.currentLevel, x/Game.gameSize, y/Game.gameSize));
        } else {
            System.out.println("invalid item type");
        }
    }



    /**
     * code run on initalization of level, 
     * allows graphics context to be referenced.
     * 
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game.loadCanvas(gameCanvas.getGraphicsContext2D());
        showImages();


        bomb.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = bomb.startDragAndDrop(TransferMode.ANY);
                // db.setDragView(bomb.getImage());

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("bomb");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });


        // This code allows the canvas to receive a dragged object within its bounds.
        gameCanvas.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // Mark the drag as acceptable if the source was the draggable image.
                // (for example, we don't want to allow the user to drag things or files into our application)
                if (event.getGestureSource() == bomb) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }
            }
        });

        // This code allows the canvas to react to a dragged object when it is finally dropped.
        gameCanvas.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // We call this method which is where the bulk of the behaviour takes place.
                canvasDragDroppedOccured(event);
                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });
    }
}


