package gameworld;

public abstract class ChallengeItem extends Item {

  private boolean navigable = false;

  public ChallengeItem(int x, int y) {
    super(x, y);
  }

  public boolean isNavigable() {
    return navigable;
  }

  public void setNavigable(boolean navigable) {
    this.navigable = navigable;
  }

}
