package gameworld;

/**
 * This class represents a bomb challenge which must be
 * diffused by the player in order to access a portal.
 *
 * @author Latrell Whata 300417220
 */
public class Bomb extends ChallengeItem {

  /**
   * This constructor creates a Bomb challenge.
   *
   * @param row       the row value of the tile the Bomb is located
   * @param col       the column value of the tile the Bomb is located
   * @param direction the direction the Bomb is facing
   */
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


  @Override
  public String toString() {
    return "Bomb";
  }

}
