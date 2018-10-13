package gameworld;

public class DoorTile extends AccessibleTile {

  private Room neighbour;
  private Direction direction;

  public DoorTile(Room neighbour, int x, int y, Direction direction) {

    super(x, y);
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


