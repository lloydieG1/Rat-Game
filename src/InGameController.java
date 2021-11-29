import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

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

    public double mouseX;
    public double mouseY;

    @FXML
    Canvas gameCanvas; //canvas the game is shown on

    @FXML
    Text score;

    @FXML
    private TilePane bombPane;

    @FXML
    private TilePane deathRatPane;

    @FXML
    private TilePane gasPane;

    @FXML
    private TilePane stopSignPane;

    @FXML
    private Canvas minimap;

    public int buttonSize = 70;


    @FXML
    private void buttonsVisible(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();

    }

    
    private static ElementType lastItem;
    /**
     * switches back to the menu
     */
    @FXML
    private void backClick() {
        Game.openLevelMenu();
        Game.pauseGame();

    }

    @FXML
    private void mapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (x < buttonSize) {
            Game.gameX = Game.gameX+buttonSize;
        }

        if (y < buttonSize) {
            Game.gameY = Game.gameY+buttonSize;
        }

        if (x > Game.MAP_WIDTH-buttonSize) {
            Game.gameX = Game.gameX-buttonSize;
        }

        if (y > Game.MAP_HEIGHT-buttonSize) {
            Game.gameY = Game.gameY-buttonSize;
        }

    }

    private Image  getImage(String fileName) {
        Image image = ImageLoader.getImage(fileName, 64);
        return image;
    }

    private String typeToString(ElementType type) {
        if (type.equals(ElementType.Bomb)) {
            return "bomb";
        } else if (type.equals(ElementType.DeathRat)) {
            return "deathRat";
        } else if (type.equals(ElementType.Gas)) {
            return "gas";
        } else if (type.equals(ElementType.NoEntry)) {
            return "noEntry";
        }
        return "invalid type";
    }

    private ImageView getItem(ElementType type) {
        ImageView item = new ImageView();
        item.setImage(getImage(typeToString(type) + ".png"));
        item.setCache(true);
        item.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = item.startDragAndDrop(TransferMode.ANY);
                // db.setDragView(bomb.getImage());

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString(typeToString(type) );
                db.setContent(content);

                lastItem = type;
                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        return item;
    }

    public void resetItems() {

        while(bombPane.getChildren().size() >0) {
            bombPane.getChildren().remove(0);
        }
        while(deathRatPane.getChildren().size() >0) {
            deathRatPane.getChildren().remove(0);
        }

        while(gasPane.getChildren().size() >0) {
            gasPane.getChildren().remove(0);
        }

        while(stopSignPane.getChildren().size() >0) {
            stopSignPane.getChildren().remove(0);
        }
    }


    public void addItem(ElementType itemType) {
        if (itemType ==null) {
            System.out.println("null item type");
        } else {
            if (itemType.equals(ElementType.Bomb)) {
                bombPane.getChildren().add(getItem(ElementType.Bomb));
            } else if (itemType.equals(ElementType.DeathRat)) {
                deathRatPane.getChildren().add(getItem(ElementType.DeathRat));
            } else if (itemType.equals(ElementType.Gas)) {
                gasPane.getChildren().add(getItem(ElementType.Gas));
            } else if (itemType.equals(ElementType.NoEntry)) {
                stopSignPane.getChildren().add(getItem(ElementType.NoEntry));
            } else {
                System.out.println("invalid item type");

            }
        }
    }

    /**
     * React when an object is dragged onto the canvas.
     * @param event The drag event itself which contains data about the drag that occurred.
     */
    public void canvasDragDroppedOccured(DragEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Print a string showing the location.



        placeItem((int)x, (int)y, lastItem);
        if (lastItem.equals(ElementType.Bomb)) {
            bombPane.getChildren().remove(0);
        } else if (lastItem.equals(ElementType.DeathRat)) {
            deathRatPane.getChildren().remove(0);
        }
    }

    /**
     * place an item into the level
     * @param x
     * @param y
     */
    public static void placeItem(int x, int y, ElementType type) {
        x = x-Game.gameX;
        y=y-Game.gameY;
        if (type.equals(ElementType.Bomb)) {
         Game.currentLevel.addElement(new Bomb(ElementType.Bomb,  Game.currentLevel, x/Game.gameSize, y/Game.gameSize));
        } else if (type.equals(ElementType.DeathRat)) {
         Game.currentLevel.addElement(new DeathRat(ElementType.DeathRat, Game.currentLevel, x/Game.gameSize, y/Game.gameSize));
        } else if (type.equals(ElementType.Gas)) {
            Game.currentLevel.addElement(new Gas(ElementType.Gas, Game.currentLevel, x/Game.gameSize, y/Game.gameSize, 4));
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
        Game.levelController = this;
        Game.setMiniMap(minimap.getGraphicsContext2D());

        Game.loadCanvas(gameCanvas.getGraphicsContext2D());

        // This code allows the canvas to receive a dragged object within its bounds.
        gameCanvas.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // Mark the drag as acceptable if the source was the draggable image.
                // (for example, we don't want to allow the user to drag things or files into our application)
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
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


