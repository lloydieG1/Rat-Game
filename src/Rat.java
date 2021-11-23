import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 *a rat will move to tiles, etc
 * @author william randle
 */
public class Rat extends Element {

    private boolean isMale;

    public Rat(ElementType type, Level level, int x, int y, boolean isMale, Direction dir) {
        super(type, level, x, y, dir);

        this.isMale = isMale;


    }

    protected void tick() {
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
            }





    }

    /**
     * the logic of the rat, only ran at the rats slower speed
     */
    private void logic() {
        age++;
        movement();
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

    /**
     * renders a rat
     * @param g graphics context
     */
    protected void render(GraphicsContext g) {
        double x = (this.x-1)*factor;
        double y = this.y*factor;
        //calculating the position the rat should be in this frame

        g.setFill(Color.color(0.2,0.2,0.3));
        g.fillRect(x, y, size/2, size/2);


    }

}
