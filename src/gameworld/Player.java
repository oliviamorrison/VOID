package gameworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private static final int HEALTH_BOOST = 20;
  private static final int MAX_HEALTH = 100;

  private AccessibleTile tile;
  private List<Item> inventory;
  private Room room;
  private int health;
  private Direction playerDir;
  private Ellipse ellipse;

  public Player(Room room, AccessibleTile tile, int health, Direction direction) {

    this.room = room;
    this.tile = tile;
    this.inventory = new ArrayList<>();
    this.health = (health > 0) ? health : MAX_HEALTH;
    this.playerDir = direction;

    this.ellipse = new Ellipse();
    this.ellipse.setFill(Color.ORANGE);
    this.ellipse.setRadiusX(8);
    this.ellipse.setRadiusY(15);

  }

  public Direction getPlayerDir() {
    return playerDir;
  }

  public void setPlayerDir(Direction playerDir) {
    this.playerDir = playerDir;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void boostHealth() {

    health += HEALTH_BOOST;

    if (health > MAX_HEALTH)
      health = MAX_HEALTH;

  }

  public void loseHealth() {

    if (health > 0)
      health--;
    else
      health = 0;

  }

  public Room getRoom() {
    return room;
  }

  public void moveTile(int dx, int dy) {
    Direction direction = null;
    if(dx < 0) {
      direction = Direction.Top;
    } else if(dx > 0){
      direction = Direction.Bottom;
    } else if(dy < 0){
      direction = Direction.Left;
    } else if(dy > 0){
      direction = Direction.Right;
    }

    if(playerDir != direction){
      playerDir = direction;
      return;
    }

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
