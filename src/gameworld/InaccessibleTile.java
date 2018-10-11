package gameworld;

import javafx.scene.paint.Color;

public class InaccessibleTile extends Tile {

  public InaccessibleTile(int x, int y) {
    super(x, y);
  }

  @Override
  public String toString() {
    return "X";
  }
}
