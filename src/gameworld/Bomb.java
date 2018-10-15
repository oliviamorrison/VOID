package gameworld;

/**
 * This class represents a bomb challenge which must be
 * diffused by the player in order to access a portal.
 */
public class Bomb extends ChallengeItem {

  public Bomb(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The bomb";
  }

  @Override
  public String getDescription() {
    return "It looks dangerous, probably best to keep your distance.";
  }

}
