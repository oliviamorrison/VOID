package gameworld;

public class AccessibleTile extends Tile {
  private Token token;
  private boolean player = false;

  public AccessibleTile(Room room) {
    super(room);
  }

  public void setToken(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return this.token;
  }

  public boolean hasToken() {
    return this.token != null;
  }

  public void setPlayer(boolean player) {
    this.player = player;
  }

  public boolean hasPlayer() {
    return player;
  }
}
