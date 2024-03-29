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

        image = ImageLoader.getImage("deathRat.png", 64);
	}
	
	private void logic() {
		age++;
        
        if (age > 12) {
        	movement();
        	killRat();
        }
        
        if(ratsKilled > 0) {
        	level.removeElement(this);
        }
        
        
    }
	
	public void killRat() {
		for (Element element : level.getElements(x, y)) {
			if (element.getType().equals(ElementType.Rat)) {
				level.removeElement(element);
				ratsKilled++;
                Game.score = Game.score+1;
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
        if (age > 12) {
            killRat();
        }
	}
	
	public Direction getDirection () {
        return dir;
    }


	
	@Override
	protected void render(GraphicsContext g) {
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame

        g.save();
        g.translate(x+Game.gameSize/2.0, y+Game.gameSize/2.0);
        g.rotate(interpolateDir(dirAsNum(lastDir),dirAsNum(dir)));
        g.drawImage(image,-(size/2), -(size/2), size, size);
        g.restore();


    }
	
}
