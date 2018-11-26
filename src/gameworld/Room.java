package gameworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a room. The game consists of a series of connected rooms.
 *
 * @author Latrell Whata 300417220
 */
public class Room {

  /**
   * Width/Height of the room.
   */
  public static final int ROOMSIZE = 10;

  private int row;
  private int col;
  private Tile[][] tiles;
  private List<String> doors;
  private List<Portal> portals;

  /**
   * This constructor creates a room.
   *
   * @param row   the row value of the room within the game board
   * @param col   the column value of the room within the game board
   * @param tiles the tiles within the room
   * @param doors a list of doors that are within the room
   */
  public Room(int row, int col, Tile[][] tiles, List<String> doors) {

    this.row = row;
    this.col = col;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.doors = doors;
    this.portals = new ArrayList<>();
  }

  /**
   * This constructor creates a room for the test class.
   *
   * @param row the row value of the room within the game board
   * @param col the column value of the room within the game board
   */
  public Room(int row, int col) {
    this.row = row;
    this.col = col;
    this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
    this.doors = new ArrayList<>();
    this.portals = new ArrayList<>();

    setupTestRoom();

  }

  /**
   * This method is used to setup the default values of the tiles of a room.
   */
  private void setupTestRoom() {

    // default all border tiles to inaccessible tiles, all others to accessible tiles
    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        if (row == 0 || col == 0 || col == ROOMSIZE - 1 || row == ROOMSIZE - 1) {
          tiles[row][col] = new InaccessibleTile(row, col);
        } else {
          tiles[row][col] = new AccessibleTile(row, col);
        }
      }
    }
  }

  /**
   * This method finds the resulting tile from translating by dx, dy from
   * the current tile.
   *
   * @param currentTile the starting tile
   * @param dx          the change in the row value
   * @param dy          the change in the column value
   * @return the next tile
   */
  public AccessibleTile findNextTile(Tile currentTile, int dx, int dy) {

    int[] coordinates = getTileCoordinates(currentTile);

    assert coordinates != null;

    int x = coordinates[0];
    int y = coordinates[1];

    int newX = x + dx;
    int newY = y + dy;

    // stay within bounds
    if (newX < 0 || newY < 0 || newX >= 10 || newY >= 10) {
      return null;
    }

    Tile tile = tiles[newX][newY];

    if (tile instanceof AccessibleTile) {

      AccessibleTile nextTile = (AccessibleTile) tile;

      // cannot move onto a challenge which is not yet navigable
      if (nextTile.checkNavigable()) {
        return nextTile;
      }

    }

    return null;

  }

  /**
   * This method iterates through the tiles array to find the tile.
   *
   * @param tile the tile being searched for
   * @return the row, col value for the tile
   */
  public int[] getTileCoordinates(Tile tile) {

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {

        if (tiles[row][col].equals(tile)) {
          return new int[]{row, col};
        }

      }
    }

    return null;

  }

  /**
   * This method finds destination tile from a starting tile and
   * travelling in a particular direction.
   *
   * @param tile      the starting tile
   * @param direction the direction to travel from the start tile
   * @return the destination tile
   */
  public Tile findTile(AccessibleTile tile, Direction direction) {

    int[] coordinates = getTileCoordinates(tile);

    assert coordinates != null;

    int row = coordinates[0];
    int col = coordinates[1];

    switch (direction) {

      case NORTH:
        row -= 1;
        break;
      case SOUTH:
        row += 1;
        break;
      case EAST:
        col += 1;
        break;
      case WEST:
      default:
        col -= 1;
        break;

    }

    // stay within bounds
    if (row < 0 || col < 0 || row >= 10 || col >= 10) {
      return null;
    }

    return tiles[row][col];

  }

  /**
   * This method finds the adjacent challenge to the tile.
   *
   * @param currentTile the start tile
   * @param direction   the direction to search from the start tile
   * @return the challenge on the adjacent tile
   */
  public ChallengeItem getAdjacentChallenge(AccessibleTile currentTile, Direction direction) {

    Tile adjacentTile = findTile(currentTile, direction);

    if (adjacentTile instanceof AccessibleTile) {

      AccessibleTile tile = (AccessibleTile) adjacentTile;

      if (tile.hasChallenge()) {
        return tile.getChallenge();
      }

    }

    return null;

  }

  /**
   * This method finds the portal of a room.
   *
   * @param direction the direction of the portal within the room
   * @return the portal in the direction
   */
  public Portal getDestinationPortal(Direction direction) {

    for (Portal portal : portals) {

      if (portal.getDirection() == direction) {
        return portal;
      }

    }

    return null;

  }

  /**
   * This method rotates the room clockwise.
   */
  public void rotateRoomClockwise() {

    int x = ROOMSIZE / 2;
    int y = ROOMSIZE - 1;

    for (int i = 0; i < x; i++) {
      for (int j = i; j < y - i; j++) {

        final Tile value = this.tiles[i][j];
        this.tiles[i][j] = this.tiles[y - j][i];
        this.tiles[y - j][i] = this.tiles[y - i][y - j];
        this.tiles[y - i][y - j] = this.tiles[j][y - i];
        this.tiles[j][y - i] = value;

      }
    }
  }

  /**
   * This method rotates the room anticlockwise.
   */
  public void rotateRoomAnticlockwise() {

    Tile[][] tempArray = new Tile[ROOMSIZE][ROOMSIZE];

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        tempArray[ROOMSIZE - col - 1][row] = this.tiles[row][col];
      }
    }

    this.tiles = tempArray;
  }

  /**
   * This method rotates the direction of each object.
   *
   * @param clockwise whether items should be rotated clockwise or anticlockwise
   */
  public void rotateObjects(boolean clockwise) {

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        if (getTile(row, col) instanceof AccessibleTile) {

          AccessibleTile tile = (AccessibleTile) getTile(row, col);

          if (tile.hasItem()) {
            Item item = tile.getItem();
            Direction direction = item.getDirection();
            item.setDirection(clockwise
                ? direction.getClockwiseDirection() : direction.getAnticlockwiseDirection());
          } else if (tile.hasChallenge()) {
            ChallengeItem challenge = tile.getChallenge();
            Direction direction = challenge.getDirection();
            challenge.setDirection(clockwise
                ? direction.getClockwiseDirection() : direction.getAnticlockwiseDirection());
          }

        }
      }
    }

  }

  /**
   * This method adds a portal to the list of portals.
   *
   * @param portal the new value of the portal
   */
  public void addPortal(Portal portal) {
    portals.add(portal);
  }

  /**
   * This method removes a portal from the list of portals.
   *
   * @param portal the value of the portal to remove
   */
  public void removePortal(Portal portal) {
    portals.remove(portal);
  }

  /**
   * This method is a getter for the row value.
   *
   * @return the row value of the room within the board
   */
  public int getRow() {
    return row;
  }

  /**
   * This method is a getter for the column value.
   *
   * @return the column value of the room within the board
   */
  public int getCol() {
    return col;
  }

  /**
   * This method is a getter for a tile within the room.
   *
   * @param row the row value of the tile
   * @param col the column value of the tile
   * @return the resulting tile
   */
  public Tile getTile(int row, int col) {
    return tiles[row][col];
  }

  /**
   * This method is a getter for a tile within the room.
   *
   * @param tile the new tile value to set the tile to
   * @param row  the row value of the tile
   * @param col  the column value of the tile
   */
  public void setTile(Tile tile, int row, int col) {
    tiles[row][col] = tile;
  }

  /**
   * This method is a getter for the portals.
   *
   * @return a list of portals
   */
  public List<String> getDoors() {
    return doors;
  }

}
