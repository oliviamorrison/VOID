package gameworld;

/**
 * This class represents a vending machine challenge which must
 * be unlocked and passed a coin by the player in order to dispense a beer.
 */
public class VendingMachine extends ChallengeItem {

  private boolean unlocked = false;

  public VendingMachine(int x, int y) {
    super(x, y);
  }

  public boolean isUnlocked() {
    return unlocked;
  }

  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String toString() {
    return "VendingMachine";
  }

}
