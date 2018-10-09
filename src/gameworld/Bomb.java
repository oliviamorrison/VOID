package gameworld;

public class Bomb implements Challenge {
  private String direction;
  boolean isActive = false;

  public Bomb(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

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

}
