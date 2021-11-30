import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;



public class Explosion extends Element  {



    int maxHealth = 5;

    private Image blast;
    private boolean isCenter;



    public Explosion(ElementType type, Level level, int x, int y, int health,Direction dir, boolean isCenter) {
        super(type, level, x, y, dir, health);

        if (isCenter) {
            blast = ImageLoader.getImage("blastCenter.png", 32);
        } else {
            blast = ImageLoader.getImage("blast.png", 32);
        }

        tickSpeed=Game.FPS/10;

        lastDir=dir;
        this.isCenter = isCenter;

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
        double size = Game.gameSize;


        //get the current interpolated frame positions of rat.
        double x = renderX();
        double y = renderY();
        //calculating the position the rat should be in this frame



        g.setFill(Color.color(1-(health*0.5/maxHealth),(health*0.9/maxHealth),0));
        g.fillRect(x, y, size, size);


    }

    @Override
    protected String extraInfo() {

        return "," +isCenter;
    }



}
