import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageLoader {

    public static Image getImage(String fileName, int size) {
        Image image;
        FileInputStream inputstream = null;
        try {


        inputstream = new FileInputStream("res\\images\\" + fileName);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(inputstream, 64, 64, true, false);

        return image;
    }



}
