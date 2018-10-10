package gameworld;

public class DoorTile extends AccessibleTile {
  private Room toRoom;
  private Direction direction;

  public DoorTile(Room room, int x, int y) {
    super(room, x, y);
  }

  public DoorTile(Room room, Room toRoom, int x, int y, Direction direction) {
    super(room, x, y);
    this.toRoom = toRoom;
    this.direction = direction;
  }

  public Room getToRoom() {
    return toRoom;
  }

  public Direction getDirection() {
    return direction;
  }

  public Direction getOppositeDirection(Direction dir) {

    switch (dir) {
      case top:
        return Direction.bottom;
      case bottom:
        return Direction.top;
      case left:
        return Direction.right;
      case right:
        return Direction.left;
      default:
        return null;
    }

  }

  @Override
  public String toString() {
    return "0";
  }

}


