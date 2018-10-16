package gameworld;

/**
 * This class represents a Diffuser which is needed to defuse a bomb.
 *
 * @author Latrell Whata 300417220
 */
public class Diffuser extends Item {

  /**
   * This constructor creates a Diffuser item.
   *
   * @param row       the row value of the tile the Diffuser is located
   * @param col       the column value of the tile the Diffuser is located
   * @param direction the direction the Diffuser is facing
   */
  public Diffuser(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Bomb Diffuser";
  }

  @Override
  public String getDescription() {
    return "It's a funny-looking suitcase. Which button do I press to work it?";
  }

  @Override
  public String toString() {
    return "Diffuser";
  }

}
