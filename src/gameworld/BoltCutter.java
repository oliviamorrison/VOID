package gameworld;

import javafx.scene.image.Image;

public class BoltCutter extends Item {

  public BoltCutter(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Red Bolt Cutter";
  }

  @Override
  public String getDescription() {
    return "It's a nice, red bolt cutter. It would be useful to break some chains.";
  }

}
