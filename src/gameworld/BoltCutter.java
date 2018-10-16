package gameworld;

/**
 * This class represents a BoltCutter which must be used to unlock
 * the vending machine.
 *
 * @author Latrell Whata 300417220
 */
public class BoltCutter extends Item {

  /**
   * This constructor creates a BoltCutter item.
   *
   * @param row       the row value of the tile the BoltCutter is located
   * @param col       the column value of the tile the BoltCutter is located
   * @param direction the direction the BoltCutter is facing
   */
  public BoltCutter(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Red Bolt Cutter";
  }

  @Override
  public String getDescription() {
    return "It's a red bolt cutter. It would be useful to break some chains.";
  }

  @Override
  public String toString() {
    return "BoltCutter";
  }

}
