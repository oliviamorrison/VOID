package gameworld;

public class VendingMachine extends Challenge {

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
  public String toString() {
    return "VendingMachine";
  }

}
