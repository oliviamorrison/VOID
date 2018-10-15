package gameworld;

/**
 * This class represents a alien challenge which must be bribed
 * with a potion by the player in order to access a portal.
 */
public class Alien extends ChallengeItem {

  public Alien(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The strange alien";
  }

  @Override
  public String getDescription() {
    return "It's a strange alien. I'm sure he could do with a beverage.";
  }

}
