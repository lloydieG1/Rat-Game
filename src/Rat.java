import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.image.Image;
        import javafx.scene.paint.Color;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;


/**
 * Class used for construction and behaviours of Rat elements.
 *
 * @author William Randle, Mosaed
 *
 */      
public class Rat extends Element {
	
  private static final int MATING_TIME = 8;
  private static final int ADULT_AGE = 8;
  private boolean isMale;
  private boolean isSterile;
  private int timeLeftInMating;  
  private Image image;

  /**
   * Rat.
   * @param type
   * @param level
   * @param x
   * @param y
   * @param isMale
   * @param dir
   * @param health
   */
  public Rat(ElementType type, Level level, int x, int y, boolean isMale,
             Direction dir, int health, int age, boolean sterile) {
    super(type, level, x, y, dir, health);
    this.age = age;
    this.isMale = isMale;   
    this.isSterile = sterile;

      if (getIsChild()) {
          tickSpeed = Game.FPS/2;
          image = ImageLoader.getImage("ratChild.png", 64);
          System.out.println("*****");
      } else {
          develop();
      }

  }

  protected void tick() {  
    if (health < 1) {
      Game.score = Game.score + 1;
      level.removeElement(this);
    }
      
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
      System.out.println("deleting deathRat");
    }
        
    currentTick++;
    if (currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    } 
  }

  /**
   * The logic of the rat, only ran at the rats slower speed.
   */
  private void logic() {  
    age++;
    if (age == ADULT_AGE) {
      develop();
    }
        
    //Can move only if mating time is finished
    if (isFinishedMating()) {	
      if (level.getTile(x, y).getType().equals(TileType.Grass)) {  
        level.removeElement(this);
      }
      movement();  
      if (!getIsChild()) {
        if (isMale == false) {
          breed();
        }
      }   
    }
        
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Gas)) {
        health--;
      }
    }  
  }
    
  /**
   * Keep track how much time elapsed on mating.
   * 
   * @return Whether mating time is finished
   */
  public boolean isFinishedMating() {
    if (timeLeftInMating < 0) {
      timeLeftInMating = 0;
    }
    
    if (timeLeftInMating == 0) {
      return true;
    } else {
      timeLeftInMating--;
      return false;
    }
  }

    public boolean getIsChild() {
        return age < ADULT_AGE;
    }

  private void develop() {
      tickSpeed = Game.FPS;
    if (isMale) {
      image = ImageLoader.getImage("ratMale.png", 64);
    } else {
      image = ImageLoader.getImage("ratFemale.png", 64);
    }
  }

  public boolean getIsMale() {
      return isMale;
  }
    
  public void setIsMale(boolean isMale) {
    this.isMale = isMale;
  }

  public Direction getDirection() {
    return dir;
  }

  public void makeSterile() {
    this.isSterile = true;
  }

  private void breed() {
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Rat)) {
        Rat rat = (Rat) element;
        if (rat.isSterile == false) {
          if (rat.isMale == true) {
            System.out.println("should now mate");
            level.addElementLive(new Rat(ElementType.Rat, level, x, y,
                                 Game.random.nextBoolean(), Direction.North, 3, 0, false));
            timeLeftInMating = MATING_TIME;
            nextY = y;
            nextX = x;
          }
        }
      }
    }
  }

  /**
   * renders a rat.
   * 
   * @param g graphics context
   */
  protected void render(GraphicsContext g) {
    double x = renderX();
    double y = renderY();
    //calculating the position the rat should be in this frame

    g.save();
    g.translate(x + Game.gameSize / 2.0, y + Game.gameSize / 2.0);
    g.rotate(interpolateDir(dirAsNum(lastDir), dirAsNum(dir)));
    g.drawImage(image, - (size / 2), - (size / 2), size, size);
    g.restore();

  }

  @Override
  protected String extraInfo() {
    return "," +  isMale + "," + age + "," + isSterile;
  }
  
}
