import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Bomb extends Element  {

    private int health;
    private boolean damage;
    private Image image;
    private final int RADIUS = 1;

    private Image blast;



    public Bomb(ElementType type, Level level, int x, int y) {
        super(type, level, x, y, Direction.North);
        health = 5;
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
            level.addElementLive(new Explosion(ElementType.Explosion,level, x, y));
            level.removeElement(this);
        }

        damage = false;
    }

    private void explode() {

        for (int x = this.x-RADIUS; x <= this.x +RADIUS; x++) {
            for (int y = this.y-RADIUS; y <= this.y+RADIUS; y++) {
                for (Element element : level.getElements(x, y)) {
                    if (element.getType().equals(ElementType.Rat)) {
                        level.removeElement(element);
                        Game.score = Game.score+1;

                    }
                }
            }
        }
    }

    protected void render(GraphicsContext g) {


        double size = Game.gameSize;
        double halfSize = size/2;


        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame



        g.drawImage(image,x, y, size, size);




    }


}
