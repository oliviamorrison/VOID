package gameworld;

public class DoorTile extends AccessibleTile {

  private Room toRoom;
  private Direction direction;

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

  @Override
  public String toString() {
    return "0";
  }

}


