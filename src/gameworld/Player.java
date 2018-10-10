package gameworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private AccessibleTile tile;
  private List<Item> inventory;
  private Room room;
  private Ellipse ellipse;

  public Player(Room room, AccessibleTile tile) {
    this.room = room;
    this.tile = tile;
    this.inventory = new ArrayList<>();

    this.ellipse = new Ellipse();
    this.ellipse.setFill(Color.ORANGE);
    this.ellipse.setRadiusX(8);
    this.ellipse.setRadiusY(15);
  }

  public Room getRoom() {
    return room;
  }

  public void moveTile(int dx, int dy) {

    if (room.moveTile(tile, dx, dy) == null) {
      return;
    }
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

  public AccessibleTile getTile() {
    return tile;
  }

  public void setTile(AccessibleTile tile) {
    this.tile = tile;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public List<Item> getInventory() {
    return inventory;
  }

  public void removeItem(Item item) {
    inventory.remove(item);
  }

  public void addItem(Item item) {
    inventory.add(item);
  }

  public void pickUp(Item item) {
    this.inventory.add(item);
  }

  public Ellipse getEllipse() {
    return this.ellipse;
  }

  public void setEllipse(Ellipse e) {
    this.ellipse = e;
  }

}
