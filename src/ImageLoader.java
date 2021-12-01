import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class allows access to images.
 *
 * @author William Randle
 */
public class ImageLoader {
  
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
