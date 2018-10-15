package gameworld;

import static gameworld.Direction.directionFromString;

/**
 * This class represents items within the game.
 *
 * @author Latrell Whata 300417220
 */
public abstract class Item {

  private int row;
  private int col;
  private Direction direction;

  /**
   * This constructor creates an item.
   *
   * @param row       the row value of the tile the Item is located
   * @param col       the column value of the tile the Item is located
   * @param direction the direction the Item is facing
   */
  public Item(int row, int col, String direction) {
    this.row = row;
    this.col = col;
    this.direction = directionFromString(direction);
  }

  /**
   * This method is a getter for the row value.
   *
   * @return the row value of the tile which has the item
   */
  public int getRow() {
    return row;
  }

  /**
   * This method is a setter for the row value.
   *
   * @param row the new row value of the tile which has the item
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * This method is a getter for the column value.
   *
   * @return the column value of the tile which has the item
   */
  public int getCol() {
    return col;
  }

  /**
   * This method is a setter for the column value.
   *
   * @param col the new column value of the tile which has the item
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * This method is a getter for the direction the item is facing.
   *
   * @return the direction the item is currently facing.
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * This method is a setter for the direction of the item.
   *
   * @param direction the new direction the item is facing.
   */
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * This method is a getter for the unique internal name of the item.
   *
   * @return the unique internal name of the item
   */
  public abstract String getName();

  /**
   * This method is a getter for the description of the item.
   *
   * @return the description of the item
   */
  public abstract String getDescription();

}
