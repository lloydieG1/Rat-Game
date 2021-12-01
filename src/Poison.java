import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Poison extends Element {
	
    private Image image;
    private int health;
    private boolean damage;


	public Poison(ElementType type, Level level, int x, int y, Direction dir) {
		super(type, level, x, y, dir);
		health = 6;
        damage = false;


        image = ImageLoader.getImage("Poison.png", 64);

		}

	  private void logic() {
	        age++;
	        movement();
	    }

	@Override
	protected void tick() {
		   currentTick++;
	        if(currentTick > tickSpeed) {
	            currentTick = 0;
	            logic();
	        }
		
	}

	@Override
	  protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();

        g.setFill(Color.color(0.2,0.2,0.3));
        g.fillRect(x, y, size/2, size/2);


    }
}
