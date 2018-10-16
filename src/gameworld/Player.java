package gameworld;

import static gameworld.Direction.directionFromString;

/**
 * This class represents a player playing the game.
 *
 * @author Latrell Whata 300417220
 */
public class Player {

  private static final int HEALTH_BOOST = 20;
  private static final int MAX_HEALTH = 140;

  private AccessibleTile tile;
  private Item item = null;
  private Room room;
  private int oxygen;
  private Direction direction;

  /**
   * This constructor creates a Player.
   *
   * @param room      the room the player starts in
   * @param tile      the tile the player starts on
   * @param oxygen    the oxygen level of the player
   * @param direction the direction the player is facing
   */
  public Player(Room room, AccessibleTile tile, int oxygen, String direction) {

    this.room = room;
    this.tile = tile;
    this.oxygen = (oxygen > 0) ? oxygen : MAX_OXYGEN;
    this.direction = directionFromString(direction);

  }

  /**
   * This method increases the oxygen of the player.
   */
  public void boostOxygen() {

    oxygen += OXYGEN_BOOST;

    // limit the max value of oxygen
    if (oxygen > MAX_OXYGEN) {
      oxygen = MAX_OXYGEN;
    }

  }

  /**
   * This method decreases the oxygen of the player.
   */
  public void loseOxygen() {

    // limit the min value of oxygen
    if (oxygen > 0) {
      oxygen--;
    } else {
      oxygen = 0;
    }

  }

  /**
   * This method checks if the player changes direction.
   *
   * @param direction the next direction the player is to move
   * @return whether or not the players direction changes
   */
  public boolean changeDirection(Direction direction) {

    if (this.direction != direction) {
      this.direction = direction;
      return true;
    }

    return false;

  }

  /**
   * This method is a getter for the player direction.
   *
   * @return the player direction
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * This method is a setter for the player direction.
   *
   * @param direction the new value for the player direction
   */
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * This method is a getter for the players oxygen level.
   *
   * @return the player oxygen
   */
  public int getOxygen() {
    return oxygen;
  }

  /**
   * This method is a setter for the players oxygen level.
   *
   * @param oxygen the new value for the player oxygen
   */
  public void setOxygen(int oxygen) {
    this.oxygen = oxygen;
  }

  /**
   * This method is a getter for the room the player is located in.
   *
   * @return the room the player is located within
   */
  public Room getRoom() {
    return room;
  }

  /**
   * This method is a setter for the room the player is located in.
   *
   * @param room the new value of the room
   */
  public void setRoom(Room room) {
    this.room = room;
  }

  /**
   * This method is a getter for the tile the player is located on.
   *
   * @return the tile the player is located on
   */
  public AccessibleTile getTile() {
    return tile;
  }

  /**
   * This method is a setter for the tile the player is located on.
   *
   * @param tile the new value for the tile
   */
  public void setTile(AccessibleTile tile) {
    this.tile = tile;
  }

  /**
   * This method is a getter for the item the player has.
   *
   * @return the player's item
   */
  public Item getItem() {
    return item;
  }

  /**
   * This method is a setter for the item the player has.
   *
   * @param item the new value of the item
   */
  public void addItem(Item item) {
    this.item = item;
  }

  /**
   * This method checks if the player has an item.
   *
   * @return whether or not the player has an item
   */
  public boolean hasItem() {
    return item != null;
  }


  /**
   * This method checks if the item matches a specified String.
   *
   * @param itemName the itemName to be checked
   * @return whether or not the item matches the specified String
   */
  public boolean hasSpecificItem(String itemName) {
    if (!hasItem()) {
      return false;
    }
    return item.getName().replaceAll("\\s+", "").substring(3).equals(itemName);
  }

  /**
   * This method removes the player item.
   *
   * @return the item that was removed
   */
  public Item dropItem() {
    Item item = this.item;
    this.item = null;
    return item;

  }

}
