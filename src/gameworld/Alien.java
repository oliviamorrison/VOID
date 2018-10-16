package gameworld;

/**
 * This class represents a Alien challenge which must be befriended
 * with a potion by the player in order to access a portal.
 *
 * @author Latrell Whata 300417220
 */
public class Alien extends ChallengeItem {

  /**
   * This constructor creates an Alien challenge.
   *
   * @param row       the row value of the tile the Alien is located
   * @param col       the column value of the tile the Alien is located
   * @param direction the direction the Alien is facing
   */
  public Alien(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The strange alien";
  }

  @Override
  public String getDescription() {
    return "It's a strange alien. I wonder if it wants to be my friend.";
  }

  @Override
  public String toString() {
    return "Alien";
  }

}
