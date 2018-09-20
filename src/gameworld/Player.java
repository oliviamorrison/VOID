package gameworld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

  private Point location;
  private AccessibleTile tile;
  private List<Token> inventory = new ArrayList<>();
  private Room room;

  public Player(Room room, AccessibleTile tile) {
    this.room = room;
    this.tile = tile;
  }

  /**
   * Updates the player's current position on the board.
   *
   * @param dx amount of steps moved in the x direction
   * @param dy amount of steps moved in the y direction
   */
  public void move(int dx, int dy) {
    location.translate(dx, dy);
    System.out.println(location.toString());
  }

  public void moveTile(int dx, int dy) {
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

  public Tile getTile() {
    return tile;
  }

  public Point getLocation() {
    return location;
  }

  public List<Token> getInventory() {
    return inventory;
  }

  public void pickUp(Token item) {
    this.inventory.add(item);
  }
}
