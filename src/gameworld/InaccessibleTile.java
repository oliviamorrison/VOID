package gameworld;

public class InaccessibleTile extends Tile {

  public InaccessibleTile(Room room, int x, int y) {
    super(room, x, y);
  }

  @Override
  public String toString() {
    return "X";
  }
}
