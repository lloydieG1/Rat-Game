import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

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
        Game.currentLevel.saveFile();
        Game.openLevelMenu();
        Game.pauseGame();

    }

    @FXML
    private void mapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (x < buttonSize) {
            Game.gameX = Game.gameX + Game.gameSize/2;
        }

        if (y < buttonSize) {
            Game.gameY = Game.gameY + Game.gameSize/2;
        }

        if (x > Game.MAP_WIDTH-buttonSize) {
            Game.gameX = Game.gameX - Game.gameSize/2;
        }

        if (y > Game.MAP_HEIGHT-buttonSize) {
            Game.gameY = Game.gameY - Game.gameSize/2;
        }

    }



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

    private Image  getImage(String fileName) {
        Image image = ImageLoader.getImage(fileName, 64);
        return image;
    }

    @FXML
    private void canvasMouseLeave() {
        mouseX = gameCanvas.getWidth() / 2;
        mouseY = gameCanvas.getHeight() / 2;
    }

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
        double clickX = event.getX( )/ tilewidth;
        double clickY = event.getY() / tileheight;

        //adjust so centered:
        clickX = clickX-Game.VISIBLE_TILES / 2.0;
        clickY = clickY-Game.VISIBLE_TILES / 2.0;
        Game.gameX =- (int)(clickX * Game.gameSize);
        Game.gameY =- (int)(clickY * Game.gameSize);

    }

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
                // db.setDragView(bomb.getImage());

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

        while (bombPane.getChildren().size() >0) {
            bombPane.getChildren().remove(0);
        }
        while (deathRatPane.getChildren().size() >0) {
            deathRatPane.getChildren().remove(0);
        }

        while (gasPane.getChildren().size() >0) {
            gasPane.getChildren().remove(0);
        }

        while (stopSignPane.getChildren().size() >0) {
            stopSignPane.getChildren().remove(0);
        }
        
        while (poisonPane.getChildren().size() >0) {
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
     * Description.
     *
     * @param itemType
     */
    public void addItem(ElementType itemType) {
        int maxItems = 4;
        if (itemType == null) {
            System.out.println("null item type");
        } else {
            if (itemType.equals(ElementType.Bomb)) {
                if(bombPane.getChildren().size() < maxItems) {
                    bombPane.getChildren().add(getItem(ElementType.Bomb));
                }
            } else if (itemType.equals(ElementType.DeathRat)) {
                if(deathRatPane.getChildren().size() < maxItems) {
                    deathRatPane.getChildren().add(getItem(ElementType.DeathRat));
                }
            } else if (itemType.equals(ElementType.Gas)) {
                if(gasPane.getChildren().size() <maxItems) {
                    gasPane.getChildren().add(getItem(ElementType.Gas));
                }
            } else if (itemType.equals(ElementType.StopSign)) {
                if(stopSignPane.getChildren().size() <maxItems) {
                    stopSignPane.getChildren().add(getItem(ElementType.StopSign));
                }
            } else if (itemType.equals(ElementType.Poison)) {
            	if(poisonPane.getChildren().size() <maxItems) {
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
     * @param event The drag event itself which contains data about the drag that occurred.
     */
    public void canvasDragDroppedOccured(DragEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Print a string showing the location.

        x = x - Game.gameX;
        y= y - Game.gameY;
        x = (x / Game.gameSize);
        y = (y / Game.gameSize);

        if (Game.currentLevel.canAddItem((int)x, (int)y)) {
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
     * @param x
     * @param y
     */
    public static void placeItem(double x, double y, ElementType type) {

        if (type.equals(ElementType.Bomb)) {
         Game.currentLevel.addElement(new Bomb(ElementType.Bomb,  Game.currentLevel,
        		 (int) x, (int) y, 4));

        } else if (type.equals(ElementType.DeathRat)) {
         Game.currentLevel.addElement(new DeathRat(ElementType.DeathRat, Game.currentLevel,
        		 (int) x, (int) y, 1, Direction.North));

        } else if (type.equals(ElementType.Gas)) {
            Game.currentLevel.addElement(new Gas(ElementType.Gas, Game.currentLevel,
            		(int) x, (int) y, 3));

        } else if (type.equals(ElementType.StopSign)) {
            Game.currentLevel.addElement(new StopSign(ElementType.StopSign, Game.currentLevel,
            		(int) x, (int) y, 3));

        } else if(type.equals(ElementType.Poison)) {
        	Game.currentLevel.addElement(new Poison(ElementType.Poison, Game.currentLevel,
        			(int) x, (int) y, 1));

    	} else if(type.equals(ElementType.Sterilise)) {
    		Game.currentLevel.addElement(new Sterilise(ElementType.Sterilise, Game.currentLevel,
    				(int) x, (int) y, 2));

    	} else if(type.equals(ElementType.MaleGenderChange)) {
    		Game.currentLevel.addElement(new MaleChanger(ElementType.MaleGenderChange,
    				Game.currentLevel, (int) x, (int) y, 1));

    	} else if(type.equals(ElementType.FemaleGenderChange)) {
    		Game.currentLevel.addElement(new FemaleChanger(ElementType.FemaleGenderChange,
    				Game.currentLevel, (int) x, (int) y, 1));

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


