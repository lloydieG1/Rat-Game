import javafx.scene.canvas.GraphicsContext;

/**
 * Elements are objects on the map that need to interact directly with
 * the level, such as rats or items.
 *
 * @author William Randle
 */
public abstract class Element {
  protected int x; //x position in the map
  protected int y; //y position in the map
  protected ElementType type; //the type of element
  protected int tickSpeed = Game.FPS; //Logic only happens once this many ticks have passed
  protected int currentTick = 0; //the current tick in the cycle (eg 3/(maxfps))
  protected Level level; //the level the element is in so it can use getElements
  protected int age = 0; //age of the element in ticks
  protected Direction dir;

  protected boolean flagRemoval = false; 
  //determines if level should remove this element at the end of the tick

  protected double size = Game.gameSize;
  protected double factor = Game.gameSize;

  protected int nextX;
  protected int nextY;
  protected Direction lastDir = Direction.North;
  protected int health;

  protected boolean hitStopSign = false;

  /**
   * Constructs an element with x,y, level it is in and type.
   *
   * @param type Element type
   * @param level Level number
   * @param x X position
   * @param y Y position
   */
  public Element(ElementType type, Level level, int x, int y, Direction dir, int health) {
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
     *
     * @return A String with all needed element properties
     */
    public String asString() {
        return typeAsString() + "," + health + "," + age + "," + nextX + ","
                + nextY + "," + dirAsString(dir) + extraInfo();
    }

    /**
     * gives the
     * @return
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
        } else {
            return "-";
        }
    }

    /**
     * sets the age of the element to parsed int
     * @param age int age of the element
     */
    public void setAge(int age) {
        this.age = age;
    }


  /**
   * Get type of an Element.
   *
   * @return type of Element
   */
  public ElementType getType() {
    return type;
  }

  /**
   * Get integer X position.
   *
   * @return X position in the map
   */
  public int getX() {
    return x;
  }

  /**
   * Get integer Y position.
   *
   * @return Y position in the map
   */
  public int getY() {
    return y;
  }


    /**
     * realigns the elements position and direction, making it still for 1 tick
     */
    protected void alignPosition() {
        nextY = y;
        nextX = x;
        lastDir =dir;
    }


    /**
   * Executes element logics to allow time-based interactions.
   */
  protected abstract void tick();
  
  /**
   * Draws the element on the canvas.
   *
   * @param g The Graphics
   */
  protected abstract void render(GraphicsContext g);


  /**
   * Handles the movement of the rat.
   */
  protected void movement() {
    hitStopSign = false;
    lastDir = dir;
    x = nextX;
    y = nextY;
    
    //update to the position decided last tick.
    int checkX = 0;
    int checkY = 0;

    int randomNum = Game.random.nextInt(4);

    // changes direction until nolonger stuck
    for (int i = 0; i < 4; i++)  {
      if (isStuck()) {
        dir = rightDir(dir);
      }
    }
    if (isStuck()) {
        checkX = x;
        checkY = y;
    } else {
        if (randomNum == 0) {
            checkX = x;
            checkY = y + 1;
        } else if (randomNum == 1) {
            checkX = x + 1;
            checkY = y;
        } else if (randomNum == 2) {
            checkX = x;
            checkY = y - 1;
        } else if (randomNum == 3) {
            checkX = x - 1;
            checkY = y;
        }
        while (!(isTile(checkX, checkY))) {
            randomNum = Game.random.nextInt(4);
            if (randomNum == 0) {
                checkX = x;
                checkY = y + 1;
            } else if (randomNum == 1) {
                checkX = x + 1;
                checkY = y;
            } else if (randomNum == 2) {
                checkX = x;
                checkY = y - 1;
            } else if (randomNum == 3) {
                checkX = x - 1;
                checkY = y;
            }
        }

        if (checkY == y + 1) {
            dir = Direction.North;
        } else if (checkY == y - 1) {
            dir = Direction.South;
        } else if (checkX == x + 1) {
            dir = Direction.East;
        } else if (checkX == x - 1) {
            dir = Direction.West;
        }
    }
          this.nextX = checkX;
          this.nextY = checkY;
  }

  /**
   * Checks if the tile at parsed position is a eligable tile to go onto.
   *
   * @return If tile is safe to walk on
   */
  protected boolean isTile(int x, int y) {
    int boardSize = level.getLength();
    if (x < 0 || y < 0) {
      return false;
    }
    if (x > boardSize - 1 || y > boardSize - 1) {
      return false;
    }

    if (dir.equals(Direction.North) && y == this.y - 1) {
      return false;
    } else if (dir.equals(Direction.East) && x == this.x - 1) {
      return false;
    } else if (dir.equals(Direction.South) && y == this.y + 1) {
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
   *
   * @return If rat is stuck between tiles
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

  //returns dir to the right
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

  
  protected double interpolate(double x1, double x2) {
    return x1 + ((x2 - x1) / tickSpeed) * currentTick;
  }


 
  protected double interpolateDir(double x1, double x2) {
    //makes sure the rat never rotates more than 180 for a turn:
    if (x2 - x1 > 180) {
      x2 = x2 - 360;
    } else if (x2 - x1 < -180) {
      x2 = x2 + 360;
    }
    double currentTick = Game.minMax(this.currentTick, 0, tickSpeed / 2.0);
    double tickSpeed = this.tickSpeed / 2.0;
    return cosineInterpolation(x1, x2, currentTick, tickSpeed);
  }

  protected double cosineInterpolation(double x1, double x2, double min, double max) {
    double m2 = (1 - Math.cos(min * Math.PI / max)) / 2;
    return x1 + (x2 - x1) * m2;
  }


  /**
   * Change to a number instead of a Direction value.
   *
   * @param dir Direction of element
   * @return A number equivalent to the direction
   */
  protected double dirAsNum(Direction dir) {
    if (dir == Direction.North) {
      return (180);
    } else if (dir == Direction.East) {
      return (90);
    } else if (dir == Direction.South) {
      return (0);
    } else if (dir == Direction.West) {
      return (270);
    }
    return 0;
  }


    /**
     * Gets the rendered version of x.
     *
     * @return The rendered version of X
     */
    protected double renderX() {
        double x = (int) (this.x * factor + Game.gameX);
        double nextX = (int) (this.nextX * factor + Game.gameX);
        return interpolate(x, nextX);
    }

   /**
   * Gets the rendered version of y.
   *
   * @return The rendered version of Y
   */
  protected double renderY() {
    double y = (int) (this.y * factor + Game.gameY);
    double nextY = (int) (this.nextY * factor + Game.gameY);
    return interpolate(y, nextY);

  }


 
  protected String extraInfo() {
    return "";
  }

  protected void killRat(Rat rat) {
      if (rat.getIsPregnant()) {
          level.removeElement(rat);
          Game.score = Game.score + 10 + 10*rat.getPregnantTime();
      } else {
          level.removeElement(rat);
          Game.score = Game.score + 10;
      }
  }
  
  
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

}
