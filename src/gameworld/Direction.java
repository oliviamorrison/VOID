package gameworld;

public enum Direction {

  Top, Bottom, Left, Right;

  public Direction getOppositeDirection() {

    switch (this) {

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
