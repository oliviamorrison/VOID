package gameworld;

/**
 * This class represents a potion that a player must use
 * to befriend an alien.
 *
 * @author Latrell Whata 300417220
 */
public class Potion extends Item {

  /**
   * This constructor creates a Potion item.
   *
   * @param row       the row value of the tile the Potion is located
   * @param col       the column value of the tile the Potion is located
   * @param direction the direction the Item is Potion
   */
  public Potion(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Magic Potion";
  }

  @Override
  public String getDescription() {
    return "It's a magical potion. It could be useful to someone else.";
  }

  @Override
  public String toString() {
    return "Potion";
  }

}
