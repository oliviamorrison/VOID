package mapeditor;

import javafx.scene.image.Image;

/**
 * This class is a MapItem which is represented by an image
 * and an image name.
 */
public class MapItem {
  private Image image;
  private String imageName;

  /**
   * Map item constructor which sets the two fields.
   *
   * @param n given image name
   * @param i given image
   */
  public MapItem(String n, Image i) {
    imageName = n;
    image = i;
  }

  /**
   * A method to get the image field.
   * @return image field
   */
  public Image getImage() {
    return image;
  }

  /**
   * A method to get the image name.
   * @return image name
   */
  public String getImageName() {
    return imageName;
  }
}
