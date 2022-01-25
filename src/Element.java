import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Elements are objects on the map that need to interact directly with
 * the level, such as rats or items.
 * @author William Randle
 * @version 1.0
 */
public abstract class Element {
    protected int x;
    protected int y;
    protected ElementType type;
    protected int tickSpeed = Game.FPS;
    protected int currentTick = 0;
    protected Level level;
    protected int age = 0;
    protected Direction dir;

    protected boolean flagRemoval = false; //if the element should be removed

    protected double size = Game.gameSize; // the size when rendered

    protected int nextX; //the next x position of the element
    protected int nextY; //the next y position of the element
    protected Direction lastDir = Direction.North; //the last direction
    protected int health; //the health of the element. if 0, it dies

    protected boolean hitStopSign = false; //if it hit a stopsign this tick

    /**
     * Constructs an element with x,y, level it is in and type.
     * @param type  Element type.
     * @param level Level number.
     * @param x     The x coordinate position.
     * @param y     The y coordinate position.
     */
    public Element(ElementType type, Level level, int x, int y, Direction dir
            , int health) {
        this.y = y;
        this.x = x;
        this.type = type;
        this.level = level;
        this.dir = dir;

        this.health = health;
        alignPosition();

    }

    /**
     * Organise element properties into 1 string.
     * @return A String with all needed element properties.
     */
    public String asString() {
        return typeAsString() + "," + health + "," + age + "," + nextX + ","
                + nextY + "," + dirAsString(dir) + extraInfo();
    }

    /**
     * Gives the element type as a string.
     * @return Element type in string form.
     */
    public String typeAsString() {
        if (type == null) {
            return "-";
        }
        if (type.equals(ElementType.Rat)) {
            return "rat";
        }
        if (type.equals(ElementType.Bomb)) {
            return "bomb";
        } else if (type.equals(ElementType.Gas)) {
            return "gas";
        } else if (type.equals(ElementType.Sterilise)) {
            return "sterilise";
        } else if (type.equals(ElementType.Poison)) {
            return "poison";
        } else if (type.equals(ElementType.FemaleGenderChange)) {
            return "femaleGenderChange";
        } else if (type.equals(ElementType.MaleGenderChange)) {
            return "maleGenderChange";
        } else if (type.equals(ElementType.StopSign)) {
            return "stopSign";
        } else if (type.equals(ElementType.DeathRat)) {
            return "deathRat";
        }
        return "-";
    }

    /**
     * Sets the age of the element to parsed int.
     * @param age int age of the element.
     */
    public void setAge(int age) {
        this.age = age;
    }


    /**
     * Get type of an Element.
     * @return type of Element
     */
    public ElementType getType() {
        return type;
    }

    /**
     * Get integer X position.
     * @return X position in the map
     */
    public int getX() {
        return x;
    }

    /**
     * Get integer Y position.
     * @return Y position in the map
     */
    public int getY() {
        return y;
    }


    /**
     * Realigns the elements position and direction, making it still for 1 tick.
     */
    protected void alignPosition() {
        nextY = y;
        nextX = x;
        lastDir = dir;
    }


    /**
     * Executes element logics to allow time-based interactions.
     */
    protected abstract void tick();

    /**
     * Draws the element on the canvas.
     * @param g The Graphics.
     */
    protected abstract void render(GraphicsContext g);


    /**
     * Handles the movement of the element.
     */
    protected void movement() {
        hitStopSign = false;
        lastDir = dir;
        //update to the position decided last tick.
        x = nextX;
        y = nextY;

        changeDirectionIfStuck();

        moveRandom();
    }

    /**
     * Moves in a random available direction, if there is one. Otherwise it
     * stays still.
     */
    private void moveRandom() {

        if (isStuck()) {
            this.nextX = x;
            this.nextY = y;
        } else {
            Direction randomDirection = getValidDirection();
            move(randomDirection);

        }

    }

    /**
     * Moves an element in the parsed direction.
     * @param randomDirection The direction which the element moves in.
     */
    private void move(Direction randomDirection) {
        if (randomDirection.equals(Direction.South)) {
            nextX = x;
            nextY = y + 1;
            dir = Direction.South;
        } else if (randomDirection.equals(Direction.East)) {
            nextX = x + 1;
            nextY = y;
            dir = Direction.East;
        } else if (randomDirection.equals(Direction.North)) {
            nextX = x;
            nextY = y - 1;
            dir = Direction.North;
        } else if (randomDirection.equals(Direction.West)) {
            nextX = x - 1;
            nextY = y;
            dir = Direction.West;
        }
    }


    /**
     * Checks if the tile at parsed position is a eligable tile to go onto.
     * @return If tile is safe to walk on.
     */
    protected boolean isTile(int x, int y) {
        int boardSize = level.getLength();
        if (x < 0 || y < 0) {
            return false;
        }
        if (x > boardSize - 1 || y > boardSize - 1) {
            return false;
        }

        if (dir.equals(Direction.South) && y == this.y - 1) {
            return false;
        } else if (dir.equals(Direction.East) && x == this.x - 1) {
            return false;
        } else if (dir.equals(Direction.North) && y == this.y + 1) {
            return false;
        } else if (dir.equals(Direction.West) && x == this.x + 1) {
            return false;
        }

        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            return false;
        }

        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.StopSign)) {
                StopSign stopsign = (StopSign) element;
                if (!hitStopSign) {
                    stopsign.blocksUp();
                    hitStopSign = true;
                }
                return false;

            }
        }


        return true;
    }

    /**
     * Check if the rat cannot move to a nearby tile.
     * @return If rat is stuck between tiles.
     */
    protected boolean isStuck() {
        int checkX = x;
        int checkY = y;
        int totalPaths = 0; //if paths accesable is < 2 then stuck

        if (isTile(checkX, checkY + 1)) {
            totalPaths++;
        }

        if (isTile(checkX + 1, checkY)) {
            totalPaths++;
        }

        if (isTile(checkX, checkY - 1)) {
            totalPaths++;
        }

        if (isTile(checkX - 1, checkY)) {
            totalPaths++;
        }

        return totalPaths < 1;
    }

    /**
     * Gives a direction to the right of the parsed direction.
     * @param dir Direction we want the right of.
     * @return Direction to the right of the parsed one.
     */
    protected Direction rightDir(Direction dir) {
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
     * Gives a direction to the left of the parsed direction.
     * @param dir Direction we want the left of.
     * @return Direction to the left of the parsed one.
     */
    protected Direction leftDir(Direction dir) {
        if (dir.equals(Direction.North)) {
            return Direction.West;
        } else if (dir.equals(Direction.East)) {
            return Direction.North;
        } else if (dir.equals(Direction.South)) {
            return Direction.East;
        } else {
            return Direction.South;
        }
    }


    /**
     * Interpolate between two values.
     * @param x1 Double floor value.
     * @param x2 Double ceiling value.
     * @return Double a value between the two parsed values.
     */
    protected double interpolate(double x1, double x2) {
        return x1 + ((x2 - x1) / tickSpeed) * currentTick;
    }


    /**
     * Gives a interpolated value for the direction of an element for smooth
     * animation.
     * @param x1 Double the first direction.
     * @param x2 Double the second direction.
     * @return Double a direction between the two parsed directions.
     */
    protected double interpolateDir(double x1, double x2) {
        //makes sure the rat never rotates more than 180 for a turn:
        if (x2 - x1 > 180) {
            x2 = x2 - 360;
        } else if (x2 - x1 < -180) {
            x2 = x2 + 360;
        }
        double currentTick = Game.minMax(this.currentTick, 0, tickSpeed
                / 2.0);
        double tickSpeed = this.tickSpeed / 2.0;
        return cosineInterpolation(x1, x2, currentTick, tickSpeed);
    }

    /**
     * Gives a cosine interpolated interpolation between two values for
     * smoother animation.
     * @param x1  int the current sample.
     * @param x2  int the max sample.
     * @param min Double the minimum interpolated value.
     * @param max Double the max interpolated value.
     * @return Double a direction between the two parsed directions.
     */
    protected double cosineInterpolation(double x1, double x2, double min
            , double max) {
        double m2 = (1 - Math.cos(min * Math.PI / max)) / 2;
        return x1 + (x2 - x1) * m2;
    }


    /**
     * Gives a number of degrees from parsed Direction.
     * @param dir Direction of element.
     * @return A number of degrees equivalent to the direction.
     */
    protected double dirAsNum(Direction dir) {
        if (dir == Direction.North) {
            return (0);
        } else if (dir == Direction.East) {
            return (90);
        } else if (dir == Direction.South) {
            return (180);
        } else if (dir == Direction.West) {
            return (270);
        }
        return 0;
    }

    /**
     * Gives a Direction from parsed number of degrees.
     * @param degrees int form of direction.
     * @return A Direction equivalent to the parsed int degrees.
     */
    protected Direction numAsDir(int degrees) {
        if (degrees == 0) {
            return (Direction.North);
        } else if (degrees == 90) {
            return Direction.East;
        } else if (degrees == 180) {
            return (Direction.South);
        } else if (degrees == 270) {
            return Direction.West;
        }
        return Direction.South;
    }


    /**
     * Gives the x position for where the element should be rendered on screen.
     * @return The rendered version of X.
     */
    protected double renderX() {
        double x = (int) (this.x * Game.gameSize + Game.gameX);
        double nextX = (int) (this.nextX * Game.gameSize + Game.gameX);
        return interpolate(x, nextX);
    }

    /**
     * Gives the y position for where the element should be rendered on screen.
     * @return The rendered version of Y.
     */
    protected double renderY() {
        double y = (int) (this.y * Game.gameSize + Game.gameY);
        double nextY = (int) (this.nextY * Game.gameSize + Game.gameY);
        return interpolate(y, nextY);

    }


    /**
     * Returns any extra information for an element. by default gives nothing,
     * and should be overridden when an element requires extra info when saving.
     * @return extra information for a given element.
     */
    protected String extraInfo() {
        return "";
    }

    /**
     * Kills a parsed rat, increasing the score by the correct amount.
     * @param rat 
     */
    protected void killRat(Rat rat) {
        if (rat.getIsPregnant()) {
            level.removeElement(rat);
            Game.score = Game.score + 10 + 10 * rat.getPregnantTime();
        } else {
            level.removeElement(rat);
            Game.score = Game.score + 10;
        }
    }


    /**
     * Gives the String version of a Direction type.
     * @param dir Direction to get the string of.
     * @return Direction as a string.
     */
    protected String dirAsString(Direction dir) {
        if (dir == Direction.North) {
            return ("north");
        } else if (dir == Direction.East) {
            return ("east");
        } else if (dir == Direction.South) {
            return ("south");
        } else if (dir == Direction.West) {
            return ("west");
        }
        return "0";
    }

    /**
     * Changes direction until not stuck.
     * If there is no direction to move, it should stay in the same Direction.
     */
    private void changeDirectionIfStuck() {

        int directionChange = Game.random.nextInt(2);

        if (directionChange == 0) {
            for (int i = 0; i < 4; i++) {
                if (isStuck()) {
                    dir = rightDir(dir);
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (isStuck()) {
                    dir = leftDir(dir);
                }
            }
        }
    }

    /**
     * Gives a valid direction to head in.
     * should not be run if there are no valid directions, as will cause
     * infinite loop.
     * @return Direction a direction which is accessible.
     */
    private Direction getValidDirection() {
        int randomDirection = Game.random.nextInt(4);
        int checkX = 0;
        int checkY = 0;
        while (!(isTile(checkX, checkY))) {
            randomDirection = Game.random.nextInt(4);
            if (randomDirection == 0) {
                checkX = x;
                checkY = y - 1;
            } else if (randomDirection == 1) {
                checkX = x + 1;
                checkY = y;
            } else if (randomDirection == 2) {
                checkX = x;
                checkY = y + 1;
            } else if (randomDirection == 3) {
                checkX = x - 1;
                checkY = y;
            }
        }
        return numAsDir(randomDirection * 90);
    }

}
