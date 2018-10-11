package gameworld;

public abstract class Challenge {

  private String direction;
  private boolean navigable = false;

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public boolean isNavigable() {
    return navigable;
  }

  public void setNavigable(boolean navigable) {
    this.navigable = navigable;
  }

}
