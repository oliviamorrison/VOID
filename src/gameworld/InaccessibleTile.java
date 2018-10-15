package gameworld;

/**
 * This class represents a tile in a Room that cannot be accessed.
 */
public class InaccessibleTile extends Tile {

  public InaccessibleTile(int row, int col) {
    super(row, col);
  }

  @Override
  public String toString() {
    return "Inaccessible tile: Row: " + getRow() + " Col: " + getCol();
  }

}
