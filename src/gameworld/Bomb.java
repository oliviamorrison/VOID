package gameworld;

//TODO: Had to change bomb to implement token just for now
public class Bomb implements Token{
  boolean isActive = false;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }
}
