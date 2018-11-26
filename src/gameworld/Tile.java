package gameworld;

import renderer.PolygonBlock;

/**
 * This class represents a tile. Each room consists of a grid of tiles.
 *
 * @author Latrell Whata 300417220
 */
public abstract class Tile {

  private int row;
  private int col;
  private PolygonBlock polygon;

  /**
   * This constructor creates a Tile.
   *
   * @param row the row value of the Tile within the room
   * @param col the column value of the Tile within the room
   */
  public Tile(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * This method is a getter for the tile row.
   *
   * @return the row value of the tile within the room.
   */
  public int getRow() {
    return row;
  }

  /**
   * This method is a getter for the tile column.
   *
   * @return the column value of the tile within the room.
   */
  public int getCol() {
    return col;
  }

  /**
   * This method is a getter for the PolygonBlock of the tile.
   * It is used to render the image.
   *
   * @return the polygon block of the tile
   */
  public PolygonBlock getTilePolygon() {
    return this.polygon;
  }

  /**
   * This method is a setter for the PolygonBlock of the tile.
   *
   * @param poly the new value of the tile PolygonBlock
   */
  public void setTilePolygon(PolygonBlock poly) {
    this.polygon = poly;
  }

}
