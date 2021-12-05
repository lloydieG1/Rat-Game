import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class allows access to images.
 *
 * @author William Randle
 */
public class ImageLoader {

    public static Image grass = getImage("grass.png", 64);
    public static Image tunnel = getImage("tunnel.png", 64);

    public static Image ratChild = getImage("ratChild.png", 64);
    public static Image ratMale = getImage("ratMale.png", 64);
    public static Image ratFemale = getImage("ratFemale.png", 64);

    public static Image gas = getImage("gas.png", 64);
    public static Image bomb = getImage("bomb.png", 64);
    public static Image sterilize = getImage("sterilise.png", 64);

    public static Image sterilizeAura = getImage("steriliseAura.png", 64);


    public static Image stopSign = getImage("stopSign.png", 64);
    public static Image stopSign1 = getImage("stopSign1.png", 64);
    public static Image stopSign2 = getImage("stopSign2.png", 64);
    public static Image stopSign3 = getImage("stopSign3.png", 64);
    public static Image stopSign4 = getImage("stopSign4.png", 64);

    public static Image icon = getImage("icon.png", 64);

    /**
     * Description.
     *
     * @param fileName
     * @param size
     * @return
     */
    public static Image getImage(String fileName, int size) {
        Image image;
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("res\\images\\" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(inputstream, size, size, true, false);
        return image;
    }
}
