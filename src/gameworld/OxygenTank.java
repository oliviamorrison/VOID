package gameworld;

/**
 * This class represents an oxygen tank which can boost the
 * oxygen level of a player.
 *
 * @author Latrell Whata 300417220
 */
public class OxygenTank extends Item {

  /**
   * This constructor creates an OxygenTank item.
   *
   * @param row       the row value of the tile the OxygenTank is located
   * @param col       the column value of the tile the OxygenTank is located
   * @param direction the direction the Item is OxygenTank
   */
  public OxygenTank(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The full oxygen tank";
  }

  @Override
  public String getDescription() {
    return "It's a full oxygen tank. It'll help you breathe.";
  }

  @Override
  public String toString() {
    return "OxygenTank";
  }

}
