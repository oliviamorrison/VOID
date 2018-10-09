package gameworld;

public abstract class Challenge {

  private boolean navigable = false;

  public boolean isNavigable() {
    return navigable;
  }

  public void setNavigable(boolean navigable) {
    this.navigable = navigable;
  }

}
