package gameworld;

/**
 * This class represents a vending machine challenge which must
 * be unlocked and passed a coin by the player in order to dispense a potion.
 *
 * @author Latrell Whata 300417220
 */
public class VendingMachine extends ChallengeItem {

  private boolean unlocked = false;

  /**
   * This constructor creates a VendingMachine challenge.
   *
   * @param row       the row value of the tile the VendingMachine is located
   * @param col       the column value of the tile the VendingMachine is located
   * @param direction the direction the Bomb is VendingMachine
   */
  public VendingMachine(int row, int col, String direction) {
    super(row, col, direction);
  }

  /**
   * This method is a getter to determine if the vending machine is unlocked.
   *
   * @return whether or not it is unlocked
   */
  public boolean isUnlocked() {
    return unlocked;
  }

  /**
   * This method is a setter to set whether the vending machine is unlocked.
   *
   * @param unlocked the new value for whether the vending machine is unlocked
   */
  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
  }

  @Override
  public String getName() {
    return "The vending machine";
  }

  @Override
  public String getDescription() {
    return "It's a vending machine. What goodies does it contain?";
  }

  @Override
  public String toString() {
    return "VendingMachine";
  }

}
