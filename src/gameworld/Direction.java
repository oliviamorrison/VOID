package gameworld;

public enum Direction {

  Top, Bottom, Left, Right;

  public String toString() {

    switch (this) {
      case Left:
        return "left";
      case Right:
        return "right";
      case Top:
        return "top";
      case Bottom:
        return "bottom";
      default:
        return null;
    }
  }
  
}
