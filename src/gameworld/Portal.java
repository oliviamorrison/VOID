package gameworld;

public class Portal extends AccessibleTile {

  private Room neighbour;
  private Direction direction;

  public Portal(int row, int col, Room neighbour, Direction direction) {

    super(row, col);
    this.neighbour = neighbour;
    this.direction = direction;

  }

  public Room getNeighbour() {
    return neighbour;
  }

  public Direction getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return direction.toString();
  }

}


