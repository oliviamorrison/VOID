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
      case EAST:
        return Direction.WEST;
      case WEST:
      default:
        return Direction.EAST;

    }

  }

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

  public static Direction directionFromString(String direction){

    switch(direction){

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

}
