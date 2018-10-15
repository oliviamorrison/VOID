package gameworld;

import static gameworld.Direction.directionFromString;

public class Player {

  private static final int OXYGEN_BOOST = 20;
  private static final int MAX_OXYGEN = 100;

  private AccessibleTile tile;
  private Item item = null;
  private Room room;
  private int oxygen;
  private Direction direction;

  public Player(Room room, AccessibleTile tile, int oxygen, String direction) {

    this.room = room;
    this.tile = tile;
    this.oxygen = (oxygen > 0) ? oxygen : MAX_OXYGEN;
    this.direction = directionFromString(direction);

  }

  public void boostOxygen() {

    oxygen += OXYGEN_BOOST;

    if (oxygen > MAX_OXYGEN)
      oxygen = MAX_OXYGEN;

  }

  public void loseOxygen() {

    if (oxygen > 0)
      oxygen--;
    else
      oxygen = 0;

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

  public int getOxygen() {
    return oxygen;
  }

  public void setOxygen(int oxygen) {
    this.oxygen = oxygen;
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

  public Item getItem() {
    return item;
  }

  public void addItem(Item item) {
    this.item = item;
  }

  public boolean hasItem() {
    return item != null;
  }

  public boolean hasSpecificItem(String itemName) {
    if (!hasItem()) return false;
    return item.getName().replaceAll("\\s+","").substring(3).equals(itemName);
  }

  public Item dropItem() {
    Item item = this.item;
    this.item = null;
    return item;

  }

}
