package gameworld;

/**
 * This class helps to identify the orientation of game objects
 * relative to the room as well as the direction the player is facing.
 */
public enum Direction {

  NORTH, SOUTH, EAST, WEST;

  /**
   * This method finds the opposite direction.
   *
   * @return the opposite direction
   */
  public Direction getOppositeDirection() {

    switch (this) {

      case NORTH:
        return Direction.SOUTH;
      case SOUTH:
        return Direction.NORTH;
      case WEST:
        return Direction.EAST;
      case EAST:
      default:
        return Direction.WEST;

    }
  }

}
