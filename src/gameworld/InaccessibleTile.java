package gameworld;

import javafx.scene.paint.Color;

public class InaccessibleTile extends Tile {
  private final static Color color = Color.rgb(237, 185, 177);
  private final static double height = 0.75;
  public InaccessibleTile(Room room, int x, int y) {
    super(room, x, y, color, height);
  }

  @Override
  public String toString() {
    return "X";
  }
}
