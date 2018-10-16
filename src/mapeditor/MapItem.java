package mapeditor;

import javafx.scene.image.Image;

public class MapItem {
  private Image image;
  private String imageName;

  public MapItem(String n, Image i) {
    imageName = n;
    image = i;
  }

  public Image getImage() {
    return image;
  }

  public String getImageName() {
    return imageName;
  }
}
