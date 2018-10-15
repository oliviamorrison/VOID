package gameworld;

/**
 * This class represents a vending machine challenge which must
 * be unlocked and passed a coin by the player in order to dispense a beer.
 */
public class VendingMachine extends ChallengeItem {

  private boolean unlocked = false;

  public VendingMachine(int row, int col, String direction) {
    super(row, col, direction);
  }

  public boolean isUnlocked() {
    return unlocked;
  }

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
