package gameworld;

/**
 * This class represents a tile in a Room that cannot be accessed.
 *
 * @author Latrell Whata 300417220
 */
public class InaccessibleTile extends Tile {

  /**
   * This constructor creates a InaccessibleTile.
   *
   * @param row the row value of the tile within a room
   * @param col the column value of the tile within a room
   */
  public InaccessibleTile(int row, int col) {
    super(row, col);
  }

}
