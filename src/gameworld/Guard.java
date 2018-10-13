package gameworld;

/**
 * This class represents a guard challenge which must be bribed
 * with a beer by the player in order to access a portal.
 */
public class Guard extends Challenge {

  public Guard(int x, int y) {
    super(x, y);
  }

  @Override
  public String toString() {
    return "Guard";
  }

}
