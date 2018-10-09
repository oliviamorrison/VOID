package gameworld;

public abstract class Challenge {

  private boolean accessible = false;

  public boolean isAccessible() { return accessible; }

  public void setAccessible(boolean accessible) { this.accessible = accessible; }

}
