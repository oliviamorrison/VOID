package gameworld;

public class DoorTile extends AccessibleTile {
  private Room toRoom;
  private Direction direction;

  public DoorTile(Room toRoom, Room room, Direction direction) {
    super(room);
    this.toRoom = toRoom;
    this.direction = direction;
  }

  public Room getToRoom() {
    return toRoom;
  }

  public Direction getDirection() {
    return direction;
  }

  public Direction getOppoositeDirection(Direction dir) {

    switch (dir) {
      case Top:
        return Direction.Bottom;
      case Bottom:
        return Direction.Top;
      case Left:
        return Direction.Right;
      case Right:
        return Direction.Left;
      default:
        return null;
    }

  }
}


