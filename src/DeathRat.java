import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DeathRat extends Element {
	
	private int ratsKilled;
	private Image image;

	public DeathRat(ElementType type, Level level, int x, int y) {
		super(type, level, x, y, Direction.North);
		// TODO Auto-generated constructor stub
		ratsKilled = 0;
		
		FileInputStream inputstream = null;
        try {

            inputstream = new FileInputStream("res\\images\\deathRat.png");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(inputstream, 64, 64, true, false);
	}
	
	private void logic() {
		age++;
        
        if (age > 12) {
        	movement();
        	killRat();
        }
        
        if(ratsKilled > 5) {
        	level.removeElement(this);
        }
        
        
    }
	
	public void killRat() {
		for (Element element : level.getElements(x, y)) {
			if (element.getType().equals(ElementType.Rat)) {
				level.removeElement(element);
				ratsKilled++;
            }
		}
	}
	
	
	

	@Override
	protected void tick() {
		currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
	}
	
	public Direction getDirection () {
        return dir;
    }

    /**
     * handles the movement of the rat
     */
	private void movement() {
        //update to the position decided last tick.

        int checkX = 0;
        int checkY = 0;

        int randomNum = Game.random.nextInt(4);

        // changes direction until nolonger stuck
        while (isStuck()) {
            dir = rightDir(dir);
        }

        if (randomNum == 0) {
            checkX = x;
            checkY = y +1;
        } else if (randomNum == 1) {
            checkX = x+1;
            checkY = y;
        }else if (randomNum == 2) {
            checkX = x;
            checkY = y-1;
        }else if (randomNum == 3) {
            checkX = x-1;
            checkY = y;
        }

        while (!(isTile(checkX, checkY))) {
            randomNum = Game.random.nextInt(4);

            if (randomNum == 0) {
                checkX = x;
                checkY = y +1;
            } else if (randomNum == 1) {
                checkX = x+1;
                checkY = y;
            }else if (randomNum == 2) {
                checkX = x;
                checkY = y-1;
            }else if (randomNum == 3) {
                checkX = x-1;
                checkY = y;
            }
        }

        if (checkY == y+1) {
            dir = Direction.North;
        } else if (checkY == y-1) {
            dir = Direction.South;
        }else if (checkX == x+1) {
            dir = Direction.East;
        }else if (checkX == x-1) {
            dir = Direction.West;
        }

        this.x = checkX;
        this.y = checkY;


    }




    /**
     *  checks if the tile at parsed position is a eligable tile to go onto
     *
     * @return boolean if tile is safe
     */
    private boolean isTile(int x, int y) {
        int boardSize = level.getLength();

        if (x < 0 || y < 0) {
            return false;
        }

        if (x > boardSize-1 || y > boardSize-1) {
            return false;
        }

        if (dir.equals(Direction.North) && y == this.y-1 ) {
            return false;
        } else if (dir.equals(Direction.East) && x == this.x-1 ) {
            return false;
        }else if (dir.equals(Direction.South) && y == this.y+1 ) {
            return false;
        }else if (dir.equals(Direction.West) && x == this.x+1 ) {
            return false;
        }

        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            return false;
        }


        return true;
    }





    /**
     * returns true if the rat cannot move to a nearby tile
     * @return
     */
    private boolean isStuck() {
        int checkX= x;
        int checkY = y;
        int totalPaths = 0; //if paths accesable is < 2 then stuck

        if(isTile(checkX, checkY+1)) {
            totalPaths++;
        }

        if(isTile(checkX+1, checkY)) {
            totalPaths++;
        }

        if(isTile(checkX, checkY-1)) {
            totalPaths++;
        }

        if(isTile(checkX-1, checkY)) {
            totalPaths++;
        }

        return totalPaths<1;




    }

    //returns dir to the right
    private Direction rightDir(Direction dir) {
        if (dir.equals(Direction.North)) {
            return Direction.East;
        } else if (dir.equals(Direction.East)) {
            return Direction.South;
        } else if (dir.equals(Direction.South)) {
            return Direction.West;
        } else {
            return Direction.North;
        }
    }
	
	
	@Override
	protected void render(GraphicsContext g) {
		double size = Game.gameSize;
        double halfSize = size/2;
        double x = ((this.x-1)*factor)*-1.0 + 700;
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.setFill(Color.color(0.5,0.5,0.5));
        g.drawImage(image, x, y, halfSize, halfSize);


    }
	
}
