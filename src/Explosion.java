import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Explosion extends Element  {

    private int health;
    private Image blast;



    public Explosion(ElementType type, Level level, int x, int y) {
        super(type, level, x, y, Direction.North);
        health = 2;


        blast = ImageLoader.getImage("blast.png", 64);
    }

    protected void tick() {
        for (Element element : level.getElements(x, y)) {
            if (element.getType().equals(ElementType.Rat)) {
                level.removeElement(element);
                Game.score = Game.score+1;

            }
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
            level.removeElement(this);
        }
    }



    protected void render(GraphicsContext g) {
        System.out.println("£");

        double size = Game.gameSize;


        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame



        g.drawImage(blast,x, y, size, size);



    }


}
