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


    public Bomb(ElementType type, Level level, int x, int y) {
        super(type, level, x, y, Direction.North);
        health = 6;
        damage = false;

        FileInputStream inputstream = null;
        try {

            inputstream = new FileInputStream("res\\images\\bomb.png");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(inputstream, 64, 64, true, false);

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
            explode();

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

                    }
                }
            }
        }
    }

    protected void render(GraphicsContext g) {


        double size = Game.gameSize;
        double halfSize = size/2;
        double x = renderX();
        double y = renderY();

        //get the current interpolated frame positions of rat.

        if (health >1) {
            g.setFill(Color.color(0, 0, 0));
            g.drawImage(image, x - size / 4, y - size / 4, halfSize, halfSize);
        } else {
            g.setFill(Color.color(1, 1, 0, 0.5));
            double blastOffset = size*((2+RADIUS));
            g.fillRect(x- blastOffset/2.0, y -blastOffset/2.0, blastOffset, blastOffset);

            g.setFill(Color.color(1, 0, 0));
            g.fillRect(x- size / 2, y -size / 2, size, size);
        }



    }


}
