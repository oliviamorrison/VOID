package gameworld;

/**
 * This class represents a Coin item which a player needs to
 * purchase a potion from the vending machine.
 *
 * @author Latrell Whata 300417220
 */
public class Coin extends Item {

  /**
   * This constructor creates a Coin item.
   *
   * @param row       the row value of the tile the Coin is located
   * @param col       the column value of the tile the Coin is located
   * @param direction the direction the Coin is facing
   */
  public Coin(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Golden Coin";
  }

  @Override
  public String getDescription() {
    return "It's a shiny, gold coin. It could probably buy something nice.";
  }

  @Override
  public String toString() {
    return "Coin";
  }

}
