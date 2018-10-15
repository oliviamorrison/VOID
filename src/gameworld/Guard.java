package gameworld;

/**
 * This class represents a guard challenge which must be bribed
 * with a beer by the player in order to access a portal.
 */
public class Guard extends ChallengeItem {

  public Guard(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The guard";
  }

  @Override
  public String getDescription() {
    return "He's a hardworking chap. I'm sure he could do with a beverage.";
  }

}
