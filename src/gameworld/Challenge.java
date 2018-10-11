package gameworld;

public abstract class Challenge {

  private String direction;
  private boolean navigable = false;
  private int x;
  private int y;

  public Challenge(int x, int y) {

    this.x = x;
    this.y = y;

  }

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

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

}
