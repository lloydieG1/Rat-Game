import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Description.
 * 
 * @author 
 *
 */
public class DeathRat extends Element {

  private int ratsKilled;
  private Image image;
  public static final int MAX_HEALTH = 5;

  private final int KILLING_AGE = 5;
  
  /**
   * Description.
   *
   * @param type
   * @param level
   * @param x
   * @param y
   * @param health
   * @param dir
   */
  public DeathRat(ElementType type, Level level, int x, int y, int health, Direction dir) {
    super(type, level, x, y, Direction.North, health);
    this.dir = dir;
    // TODO Auto-generated constructor stub
    ratsKilled = 0;
    image = ImageLoader.getImage("deathRat.png", 64);
    tickSpeed = Game.FPS / 2;
  }

  private void logic() {
    age++;
    if (age > KILLING_AGE ) {
      movement();
      checkRat();
    }      
  }
  
  /**
   * Description.
   */
  public void checkRat() {
    for (Element element : level.getElements(x, y)) {
      if (element.getType().equals(ElementType.Rat)) {
    	  if (element.getType().equals(ElementType.Rat)) {
        	  Rat rat = (Rat) element;
        	  killRat(rat);
          }
        if (health <= 0) {
          level.removeElement(this);
        }
      }
    }
  }



  @Override
  protected void killRat(Rat rat) {
      if(rat.getIsPregnant()) {
          level.removeElement(rat);
          health = health - 1 - rat.getPregnantTime();;
          Game.score = Game.score + 10 + 10*rat.getPregnantTime();
      } else {
          level.removeElement(rat);
          health--;
          Game.score = Game.score + 10;
      }
  }

  @Override
  protected void tick() {
    if (level.getTile(x, y).getType().equals(TileType.Grass)) {
      level.removeElement(this);
    }
    currentTick++;
    if (currentTick > tickSpeed) {
      currentTick = 0;
      logic();
    }
    if (age > KILLING_AGE ) {
      checkRat();
    }
  }

  public Direction getDirection() {
    return dir;
  }

  @Override
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
        return "," + age;
    }
}
