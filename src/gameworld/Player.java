package gameworld;

public class Player {

  private static final int HEALTH_BOOST = 20;
  private static final int MAX_HEALTH = 100;

  private AccessibleTile tile;
  private Item item = null;
  private Room room;
  private int health;
  private Direction direction;

  public Player(Room room, AccessibleTile tile, int health, Direction direction) {

    this.room = room;
    this.tile = tile;
    this.health = (health > 0) ? health : MAX_HEALTH;
    this.direction = direction;

  }

  public void boostHealth() {

    health += HEALTH_BOOST;

    if (health > MAX_HEALTH) {
      health = MAX_HEALTH;
    }

  }

  public void loseHealth() {

    if (health > 0) {
      health--;
    } else {
      health = 0;
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

  public Item getItem() {
    return item;
  }

  public void addItem(Item item) {
    this.item = item;
  }

  public boolean hasItem() {
    return item != null;
  }

  public Item dropItem() {

    Item item = this.item;
    this.item = null;
    return item;

  }

}
