package gameworld;

/**
 * This class represents a bomb challenge which must be
 * diffused by the player in order to access a portal.
 */
public class Bomb extends ChallengeItem {

  public Bomb(int x, int y) {
    super(x, y);
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String toString() {
    return "Bomb";
  }

}
