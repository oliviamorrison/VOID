package gameworld;

import static gameworld.Direction.directionFromString;

public abstract class Item {

  private int row;
  private int col;
  private Direction direction = Direction.EAST;

  public Item(int row, int col) {
    this.row = row;
    this.col = col;
//    this.direction = directionFromString(direction);
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public abstract String getName();

  public abstract String getDescription();

}
