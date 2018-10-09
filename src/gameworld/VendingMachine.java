package gameworld;

public class VendingMachine extends Challenge {

  private boolean unlocked = false;

  public boolean isUnlocked() {
    return unlocked;
  }

  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
  }

  @Override
  public String toString(){
    return "vending machine";
  }
}
