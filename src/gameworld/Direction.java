package gameworld;

/**
 * This class helps to identify the orientation of game objects
 * relative to the room as well as the direction the player is facing.
 *
 * @author Latrell Whata 300417220
 */
public enum Direction {

  NORTH, SOUTH, EAST, WEST;

  /**
   * This method finds the direction from a string value.
   *
   * @param direction the string value of a direction
   * @return the Direction value of the direction
   */
  public static Direction directionFromString(String direction) {

    switch (direction) {

      case "NORTH":
        return Direction.NORTH;
      case "SOUTH":
        return Direction.SOUTH;
      case "EAST":
        return Direction.EAST;
      case "WEST":
      default:
        return Direction.WEST;

    }
  }

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
      case EAST:
        return Direction.WEST;
      case WEST:
      default:
        return Direction.EAST;

    }

  }

  /**
   * This method finds the clockwise direction.
   *
   * @return the clockwise direction
   */
  public Direction getClockwiseDirection() {

    switch (this) {

      case NORTH:
        return Direction.EAST;
      case SOUTH:
        return Direction.WEST;
      case EAST:
        return Direction.SOUTH;
      case WEST:
      default:
        return Direction.NORTH;

    }

  }

  /**
   * This method finds the anticlockwise direction.
   *
   * @return the anticlockwise direction
   */
  public Direction getAnticlockwiseDirection() {

    switch (this) {

      case NORTH:
        return Direction.WEST;
      case SOUTH:
        return Direction.EAST;
      case EAST:
        return Direction.NORTH;
      case WEST:
      default:
        return Direction.SOUTH;

    }

  }

  /**
   * This method finds the next direction based on the
   * current direction and change in the row and column values.
   *
   * @param dx the change in the row value
   * @param dy the change in the column value
   * @return the next direction
   */
  public Direction nextDirection(int dx, int dy) {

    Direction direction = null;

    if (dx < 0) {
      direction = Direction.NORTH;
    } else if (dx > 0) {
      direction = Direction.SOUTH;
    } else if (dy < 0) {
      direction = Direction.WEST;
    } else if (dy > 0) {
      direction = Direction.EAST;
    }

    return direction;

  }

}
