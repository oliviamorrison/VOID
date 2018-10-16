package gameworld;

/**
 * This class represents the end goal for the game, a player completes
 * the game if the spaceship is found.
 *
 * @author Latrell Whata 300417220
 */
public class SpaceShip extends Item {

  /**
   * This constructor creates a SpaceShip item.
   *
   * @param row       the row value of the tile the SpaceShip is located
   * @param col       the column value of the tile the SpaceShip is located
   * @param direction the direction the Item is SpaceShip
   */
  public SpaceShip(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The amazing spaceship";
  }

  @Override
  public String getDescription() {
    return "It's an amazing spaceship. I can go explore space!";
  }

  @Override
  public String toString() {
    return "SpaceShip";
  }

}
