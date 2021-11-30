import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Text;


public class Bomb extends Element  {


    private boolean damage;
    private Image image;
    private final int RADIUS = 1;

    private Image blast;




    public Bomb(ElementType type, Level level, int x, int y, int health) {
        super(type, level, x, y, Direction.North, health);

        damage = false;

        image = ImageLoader.getImage("bomb.png", 64);
    }

    protected void tick() {
        if (level.getTile(x,y).getType().equals(TileType.Grass)) {
            level.removeElement(this);
            System.out.println("deleting deathRat");
        }
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;

            logic();
        }

    }

    private void logic() {
        health--;
        if (health <= 0) {
            explode();
            level.removeElement(this);
        }

        damage = false;
    }

    private void explode() {
        level.addElementLive(new Explosion(ElementType.Explosion, level, (x), y, 1));

        int tempX = x;
        int tempY = y;
        tempX++;
        while (isSpreadable(tempX, tempY)) {
            level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX),tempY, 1));
            tempX++;
        }
        tempX = x;
        tempY = y;
        tempX--;
        while (isSpreadable(tempX, tempY)) {

            level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX),tempY, 1));
            tempX--;
        }
        tempX = x;
        tempY = y;
        tempY++;
        while (isSpreadable(tempX, tempY)) {

            level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX),tempY, 1));
            tempY++;
        }
        tempX = x;
        tempY = y;
        tempY--;
        while (isSpreadable(tempX, tempY)) {

            level.addElementLive(new Explosion(ElementType.Explosion, level, (tempX),tempY, 1));
            tempY--;
        }
    }

    /**
     *  checks if the tile at parsed position is a eligable tile to go onto
     *
     * @return boolean if tile is safe
     */
    protected boolean isSpreadable(int x, int y) {
        int boardSize = level.getLength();

        if (x < 0 || y < 0) {
            return false;
        }

        if (x > boardSize-1 || y > boardSize-1) {
            return false;
        }

        if (level.getTile(x, y).getType().equals(TileType.Grass)) {
            return false;
        }

        return true;
    }


    protected void render(GraphicsContext g) {


        double size = Game.gameSize;
        double halfSize = size/2;


        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame



        g.drawImage(image,x, y, size, size);
        g.fillText(Integer.toString(health),x+halfSize, y+halfSize, size);




    }




}
