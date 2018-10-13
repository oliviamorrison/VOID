package gameworld;

/**
 * This class represents a bomb challenge which must be
 * diffused by the player in order to access a portal.
 */
public class Bomb extends Challenge {

  public Bomb(int x, int y) {
    super(x, y);
  }

  @Override
  public String toString() {
    return "Bomb";
  }

}
