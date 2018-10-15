package gameworld;

/**
 * This class represents a portal which enables a player
 * to teleport between rooms.
 *
 * @author Latrell Whata 300417220
 */
public class Portal extends AccessibleTile {

  private Room neighbour;
  private Direction direction;

  /**
   * This constructor creates a new Portal.
   *
   * @param row       tile row within the room
   * @param col       tile column within the room
   * @param neighbour adjacent room that neighbours this portal
   * @param direction direction of a portal within a room
   */
  public Portal(int row, int col, Room neighbour, Direction direction) {

    super(row, col);
    this.neighbour = neighbour;
    this.direction = direction;

  }

  /**
   * This method is a getter for the neighbouring room.
   *
   * @return adjacent room of the portal
   */
  public Room getNeighbour() {
    return neighbour;
  }

  /**
   * This method is a getter for the direction of a portal.
   *
   * @return direction of a portal within a room
   */
  public Direction getDirection() {
    return direction;
  }

}


