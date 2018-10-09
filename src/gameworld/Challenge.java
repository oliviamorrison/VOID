package gameworld;

public abstract class Challenge {

  private boolean accessible;

  abstract void interact();

  public boolean isAccessible() { return accessible; }

  public void setAccessible(boolean accessible) { this.accessible = accessible; }

}
