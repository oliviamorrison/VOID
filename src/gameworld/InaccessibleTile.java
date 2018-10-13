package gameworld;

/**
 * This class represents a tile in a Room that cannot be accessed.
 */
public class InaccessibleTile extends Tile {

  public InaccessibleTile(int x, int y) {
    super(x, y);
  }

  @Override
  public String toString() {
    return "X";
  }

}
