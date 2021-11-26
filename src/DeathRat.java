import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DeathRat extends Element {
	
	private int ratsKilled;
	private Image image;

	public DeathRat(ElementType type, Level level, int x, int y) {
		super(type, level, x, y, Direction.North);
		// TODO Auto-generated constructor stub
		ratsKilled = 0;
		
		FileInputStream inputstream = null;
        try {

            inputstream = new FileInputStream("res\\images\\deathRat.png");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(inputstream, 64, 64, true, false);
	}
	
	private void logic() {
		age++;
        
        if (age > 12) {
        	movement();
        	killRat();
        }
        
        if(ratsKilled > 5) {
        	level.removeElement(this);
        }
        
        
    }
	
	public void killRat() {
		for (Element element : level.getElements(x, y)) {
			if (element.getType().equals(ElementType.Rat)) {
				level.removeElement(element);
				ratsKilled++;
            }
		}
	}
	
	
	

	@Override
	protected void tick() {
		currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
	}
	
	public Direction getDirection () {
        return dir;
    }


	
	@Override
	protected void render(GraphicsContext g) {
		double size = Game.gameSize;
        double halfSize = size/2;
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.setFill(Color.color(0.5,0.5,0.5));
        g.drawImage(image, x, y, halfSize, halfSize);


    }
	
}
