package gameworld;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private static final int HEALTH_BOOST = 20;
  private static final int MAX_HEALTH = 100;

  private AccessibleTile tile;
  private List<Item> inventory;
  private Room room;
  private int health;
  private Direction direction;

  public Player(Room room, AccessibleTile tile, int health, String direction) {

    this.room = room;
    this.tile = tile;
    this.inventory = new ArrayList<>();
    this.health = (health > 0) ? health : MAX_HEALTH;
    this.direction = directionFromString(direction);

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

  public Direction directionFromString (String direction){
    switch(direction){
      case "North":
        return Direction.NORTH;
      case "South":
        return Direction.SOUTH;
      case "East":
        return Direction.EAST;
      default:
        return Direction.WEST;
    }
  }

  public boolean changeDirection(Direction direction) {

    if (this.direction != direction) {
      this.direction = direction;
      return true;
    }

    return false;

  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public Room getRoom() {
    return room;
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

}
