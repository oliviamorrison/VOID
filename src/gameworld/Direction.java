package gameworld;

/**
 * This class helps to identify the orientation of game objects
 * relative to the room as well as the direction the player is facing.
 */
public enum Direction {

  Top, Bottom, Left, Right;

  /**
   * This method finds the opposite direction.
   *
   * @return the opposite direction
   */
  public Direction getOppositeDirection() {

    switch (this) {

      case Top:
        return Direction.Bottom;
      case Bottom:
        return Direction.Top;
      case Left:
        return Direction.Right;
      case Right:
      default:
        return Direction.Left;

    }
  }

}
