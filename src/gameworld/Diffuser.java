package gameworld;

import javafx.scene.image.Image;

public class Diffuser extends Item {

  public Diffuser(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String toString() { return "Diffuser"; }
}
