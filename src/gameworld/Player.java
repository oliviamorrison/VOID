package gameworld;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private AccessibleTile tile;
  private List<Token> inventory = new ArrayList<>();
  private Room room;

  public Player(Room room, AccessibleTile tile) {
    this.room = room;
    this.tile = tile;
  }

  public void moveTile(int dx, int dy) {
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

  public Tile getTile() {
    return tile;
  }

  public List<Token> getInventory() {
    return inventory;
  }

  public void pickUp(Token item) {
    this.inventory.add(item);
  }
}
