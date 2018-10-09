package gameworld;

public class Bomb extends Challenge {

  boolean isActive = false;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  @Override
  public String toString(){
    return "bomb";
  }

  @Override
  public void interact() {

  }
}
