import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class handles interactions with the level menu
 *
 * @author William Randle
 * @author Adrian Zabica
 * @version 1
 */
public class InGameController implements Initializable {

    public double mouseX;
    public double mouseY;

    public boolean rightArrow = false;
    public boolean leftArrow = false;
    public boolean upArrow = false;
    public boolean downArrow = false;

    private final double MUSIC_VOLUME = 0.2;
    private final String MUSIC_FILE_PATH = "res/audio/LevelMusic.mp3";
    private Media music = new Media(new File(MUSIC_FILE_PATH).toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(music);// initialise music player


    @FXML
    private Canvas gameCanvas; //canvas the game is shown on

    @FXML
    private Text score;

    @FXML
    private TilePane bombPane;

    @FXML
    private TilePane deathRatPane;

    @FXML
    private TilePane gasPane;

    @FXML
    private TilePane stopSignPane;

    @FXML
    private TilePane poisonPane;

    @FXML
    private TilePane sterilisePane;

    @FXML
    private TilePane maleGenderChangePane;

    @FXML
    private TilePane femaleGenderChangePane;

    @FXML
    private Canvas minimap;

    @FXML
    private Canvas ratLives;

    public static final int BUTTON_SIZE = 70;

    @FXML
    private Text userName;

    @FXML
    private Text levelName;

    public boolean wrongScreen = false;

    private static ElementType lastItem;


    /**
     * sets the score text
     * @param score String new score to be displayed
     */
    public void setScore(String score) {
        this.score.setText(score);
    }

    /**
     * Stops all the arrow keys from being active.
     */
    public void resetArrowKeys() {
        rightArrow = false;
        leftArrow = false;
        upArrow = false;
        downArrow = false;
    }

    /**
     * updates the mouse position in the game when it moves
     * @param event
     */
    @FXML
    private void updateMousePosition(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();

    }

    /**
     * sets the username on display
     * @param name String the name of the user
     */
    public void setUserNameText(String name) {
        userName.setText("username: " + name);

    }

    /**
     *
     * sets the level name on display
     * @param name String name of the level
     */
    public void setLevelNameText(String name) {
        levelName.setText("level: " + name);

    }



    /**
     * Switches back to the menu
     */
    @FXML
    private void backClick() {
        wrongScreen = true;
        Game.currentLevel.saveFile();
        Game.openLevelMenu();
        Game.pauseGame();

    }

    /**
     * if mouse is over one of the nav buttons when click move in that direciton
     * @param event
     */
    @FXML
    private void mapClick(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();

        if (mouseX < BUTTON_SIZE) {
            leftArrow = true;

        }

        if (mouseY < BUTTON_SIZE) {
            upArrow = true;
        }

        if (mouseX > Game.MAP_WIDTH - BUTTON_SIZE) {
            rightArrow = true;
        }

        if (mouseY > Game.MAP_HEIGHT - BUTTON_SIZE) {
            downArrow = true;
        }


    }


    /**
     * when a button is released, you dont move in any directions
     * @param event
     */
    @FXML
    private void mapRelease(MouseEvent event) {

        upArrow = false;
        downArrow = false;
        rightArrow = false;
        leftArrow = false;

    }

    /**
     * Produce in text form.
     * @return Side Bar in a parsable form.
     */
    public String sideBarAsString() {
        String sideBar = bombPane.getChildren().size() + ",";
        sideBar = sideBar + deathRatPane.getChildren().size() + ",";
        sideBar = sideBar + gasPane.getChildren().size() + ",";
        sideBar = sideBar + stopSignPane.getChildren().size() + ",";
        sideBar = sideBar + sterilisePane.getChildren().size() + ",";
        sideBar = sideBar + maleGenderChangePane.getChildren().size() + ",";
        sideBar = sideBar + femaleGenderChangePane.getChildren().size() + ",";
        sideBar = sideBar + poisonPane.getChildren().size() + ",";
        return sideBar;
    }

    /**
     * @param fileName String name of the file of the image
     * @return Image image for the item
     */
    private Image getImage(String fileName) {
        Image image = ImageLoader.getImage(fileName, 64);
        return image;
    }

    /**
     * when the mouse leaves the canvas, default to moving in no directions so
     * a button doesnt stick
     */
    @FXML
    private void canvasMouseLeave() {
        mouseX = gameCanvas.getWidth() / 2;
        mouseY = gameCanvas.getHeight() / 2;
        upArrow = false;
        downArrow = false;
        rightArrow = false;
        leftArrow = false;

    }


    /**
     * move the map position to the position of the cursor in the minimap
     * @param event
     */
    @FXML
    private void minimapClick(MouseEvent event) {
        double widthX = Game.currentLevel.getMapBounds()[0];
        double widthY = Game.currentLevel.getMapBounds()[1];
        double width;
        width = widthX;
        if (widthX < widthY) {
            width = widthY;
        }

        double tilewidth = minimap.getWidth() / width;
        double tileheight = minimap.getHeight() / width;
        double clickX = event.getX() / tilewidth;
        double clickY = event.getY() / tileheight;

        //Adjust so centered:
        clickX = clickX - Game.visibleTiles / 2.0;
        clickY = clickY - Game.visibleTiles / 2.0;
        Game.gameX = -(int) (clickX * Game.gameSize);
        Game.gameY = -(int) (clickY * Game.gameSize);

    }

    /**
     * 
     * @param type
     * @return
     */
    private String typeToString(ElementType type) {
        if (type.equals(ElementType.Bomb)) {
            return "bomb";
        } else if (type.equals(ElementType.DeathRat)) {
            return "deathRat";
        } else if (type.equals(ElementType.Gas)) {
            return "gas";
        } else if (type.equals(ElementType.StopSign)) {
            return "stopSign";
        } else if (type.equals(ElementType.Poison)) {
            return "poison";
        } else if (type.equals(ElementType.Sterilise)) {
            return "sterilise";
        } else if (type.equals(ElementType.MaleGenderChange)) {
            return "maleGenderChange";
        } else if (type.equals(ElementType.FemaleGenderChange)) {
            return "femaleGenderChange";
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
                db.setDragView(item.getImage());

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString(typeToString(type));
                db.setContent(content);

                lastItem = type;
                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        return item;
    }

    public void resetItems() {

        while (bombPane.getChildren().size() > 0) {
            bombPane.getChildren().remove(0);
        }
        while (deathRatPane.getChildren().size() > 0) {
            deathRatPane.getChildren().remove(0);
        }

        while (gasPane.getChildren().size() > 0) {
            gasPane.getChildren().remove(0);
        }

        while (stopSignPane.getChildren().size() > 0) {
            stopSignPane.getChildren().remove(0);
        }

        while (poisonPane.getChildren().size() > 0) {
            poisonPane.getChildren().remove(0);
        }

        while (sterilisePane.getChildren().size() > 0) {
            sterilisePane.getChildren().remove(0);
        }

        while (maleGenderChangePane.getChildren().size() > 0) {
            maleGenderChangePane.getChildren().remove(0);
        }

        while (femaleGenderChangePane.getChildren().size() > 0) {
            femaleGenderChangePane.getChildren().remove(0);
        }
    }


    /**
     * Adds item to the side bar.
     * @param itemType
     */
    public void addItem(ElementType itemType) {
        int maxItems = 4;
        if (itemType == null) {
            System.out.println("null item type");
        } else {
            if (itemType.equals(ElementType.Bomb)) {
                if (bombPane.getChildren().size() < maxItems) {
                    bombPane.getChildren().add(getItem(ElementType.Bomb));
                }
            } else if (itemType.equals(ElementType.DeathRat)) {
                if (deathRatPane.getChildren().size() < maxItems) {
                    deathRatPane.getChildren().add(getItem(ElementType.DeathRat));
                }
            } else if (itemType.equals(ElementType.Gas)) {
                if (gasPane.getChildren().size() < maxItems) {
                    gasPane.getChildren().add(getItem(ElementType.Gas));
                }
            } else if (itemType.equals(ElementType.StopSign)) {
                if (stopSignPane.getChildren().size() < maxItems) {
                    stopSignPane.getChildren().add(getItem(ElementType.StopSign));
                }
            } else if (itemType.equals(ElementType.Poison)) {
                if (poisonPane.getChildren().size() < maxItems) {
                    poisonPane.getChildren().add(getItem(ElementType.Poison));
                }
            } else if (itemType.equals(ElementType.Sterilise)) {
                if (sterilisePane.getChildren().size() < maxItems) {
                    sterilisePane.getChildren().add(getItem(ElementType.Sterilise));
                }
            } else if (itemType.equals(ElementType.MaleGenderChange)) {
                if (maleGenderChangePane.getChildren().size() < maxItems) {
                    maleGenderChangePane.getChildren().add(getItem(ElementType.MaleGenderChange));
                }
            } else if (itemType.equals(ElementType.FemaleGenderChange)) {
                if (femaleGenderChangePane.getChildren().size() < maxItems) {
                    femaleGenderChangePane.getChildren().add(getItem(ElementType.FemaleGenderChange));
                }
            } else {
                System.out.println("invalid item type");

            }
        }
    }

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that occurred.
     */
    public void canvasDragDroppedOccured(DragEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Print a string showing the location.

        x = x - Game.gameX;
        y = y - Game.gameY;
        x = (x / Game.gameSize);
        y = (y / Game.gameSize);

        if (Game.currentLevel.canAddItem((int) x, (int) y)) {
            placeItem((int) x, (int) y, lastItem);
            if (lastItem.equals(ElementType.Bomb)) {
                bombPane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.DeathRat)) {
                deathRatPane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.Gas)) {
                gasPane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.StopSign)) {
                stopSignPane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.Poison)) {
                poisonPane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.Sterilise)) {
                sterilisePane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.MaleGenderChange)) {
                maleGenderChangePane.getChildren().remove(0);
            } else if (lastItem.equals(ElementType.FemaleGenderChange)) {
                femaleGenderChangePane.getChildren().remove(0);
            }
        }
    }

    /**
     * place an item into the level
     *
     * @param x
     * @param y
     */
    public static void placeItem(double x, double y, ElementType type) {

        if (type.equals(ElementType.Bomb)) {
            Game.currentLevel.addElement(new Bomb(ElementType.Bomb, Game.currentLevel,
                    (int) x, (int) y, Bomb.BLAST_TIME));

        } else if (type.equals(ElementType.DeathRat)) {
            Game.currentLevel.addElement(new DeathRat(ElementType.DeathRat, Game.currentLevel,
                    (int) x, (int) y, DeathRat.MAX_HEALTH, Direction.North));

        } else if (type.equals(ElementType.Gas)) {
            Game.currentLevel.addElement(new Gas(ElementType.Gas, Game.currentLevel,
                    (int) x, (int) y, Gas.MAX_HEALTH));

        } else if (type.equals(ElementType.StopSign)) {
            Game.currentLevel.addElement(new StopSign(ElementType.StopSign, Game.currentLevel,
                    (int) x, (int) y, 0));

        } else if (type.equals(ElementType.Poison)) {
            Game.currentLevel.addElement(new Poison(ElementType.Poison, Game.currentLevel,
                    (int) x, (int) y, Poison.MAX_HEALTH));

        } else if (type.equals(ElementType.Sterilise)) {
            Game.currentLevel.addElement(new Sterilise(ElementType.Sterilise, Game.currentLevel,
                    (int) x, (int) y, Sterilise.MAX_HEALTH));

        } else if (type.equals(ElementType.MaleGenderChange)) {
            Game.currentLevel.addElement(new MaleChanger(ElementType.MaleGenderChange,
                    Game.currentLevel, (int) x, (int) y, 1));

        } else if (type.equals(ElementType.FemaleGenderChange)) {
            Game.currentLevel.addElement(new FemaleChanger(ElementType.FemaleGenderChange,
                    Game.currentLevel, (int) x, (int) y, 1));

        } else {
            System.out.println("invalid item type");
        }
    }

    /*
     * Play music at stored at MUSIC_FILE_PATH
     *
     */
    public void playMusic() {
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /*
     * stops level music
     */
    public void stopMusic() {
        mediaPlayer.stop();
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
        mediaPlayer.setVolume(MUSIC_VOLUME); // set volume of music

        Game.loadRatLives(ratLives.getGraphicsContext2D());
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


