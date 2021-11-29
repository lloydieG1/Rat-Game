import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Explosion extends Element  {

    private int health;
    private boolean damage;

    private final int RADIUS = 1;

    private Image blast;



    public Explosion(ElementType type, Level level, int x, int y) {
        super(type, level, x, y, Direction.North);
        health = 1;
        damage = false;


        blast = ImageLoader.getImage("blast.png", 64);
    }

    protected void tick() {

        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;

            logic();
        }

    }

    private void logic() {
        health--;
        if (health <= 0) {
            level.removeElement(this);
        }

        damage = false;
    }



    protected void render(GraphicsContext g) {


        double size = Game.gameSize;
        double halfSize = size/2;


        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame


        
        double blastOffset = size*((2+RADIUS))*(interpolate(health, health-1)+0.5);
        g.drawImage(blast, x- blastOffset/2.0, y -blastOffset/2.0, blastOffset, blastOffset);





    }


}
