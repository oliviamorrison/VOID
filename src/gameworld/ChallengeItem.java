package gameworld;

public abstract class ChallengeItem extends Item {

  private boolean navigable = false;

  public ChallengeItem(int row, int col) {
    super(row, col);
  }

  public boolean isNavigable() {
    return navigable;
  }

  public void setNavigable(boolean navigable) {
    this.navigable = navigable;
  }

}
