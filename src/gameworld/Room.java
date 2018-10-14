package gameworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room {

  public static final int ROOMSIZE = 10;
  public static final Point NORTH_PORTAL = new Point(0, 5);
  public static final Point SOUTH_PORTAL = new Point(9, 5);
  public static final Point EAST_PORTAL = new Point(5, 9);
  public static final Point WEST_PORTAL = new Point(5, 0);

  private int row;
  private int col;
  private Tile[][] tiles;
  private List<String> doors;
  private List<Portal> portals;

  public Room(int row, int col, Tile[][] tiles, List<String> doors) {

    this.row = row;
    this.col = col;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.doors = doors;
    this.portals = new ArrayList<>();

  }

  public Room() {

    this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
    this.doors = new ArrayList<>();
    this.portals = new ArrayList<>();

    setupTestRoom();

  }

  private void setupTestRoom() {

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

  public AccessibleTile findNextTile(Tile currentTile, int dx, int dy) {

    int[] coordinates = getTileCoordinates(currentTile);

    assert coordinates != null;

    int x = coordinates[0];
    int y = coordinates[1];

    int newX = x + dx;
    int newY = y + dy;

    if (newX < 0 || newY < 0 || newX >= 10 || newY >= 10) {
      return null;
    }

    Tile tile = tiles[newX][newY];

    if (tile instanceof AccessibleTile) {

      AccessibleTile nextTile = (AccessibleTile) tile;

      if (nextTile.checkNavigable()) {
        return nextTile;
      }

    }

    return null;

  }

  private int[] getTileCoordinates(Tile tile) {

    for (int i = 0; i < ROOMSIZE; i++) {
      for (int j = 0; j < ROOMSIZE; j++) {

        if (tiles[i][j].equals(tile)) {
          return new int[]{i, j};
        }

      }
    }

    return null;

  }

  // find tile in a given direction
  private Tile findTile(AccessibleTile tile, Direction direction) {

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

    if (row < 0 || col < 0 || row >= 10 || col >= 10) {
      return null;
    }

    return tiles[row][col];

  }

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

  public Portal getDestinationPortal(Direction direction) {

    for (Portal portal : portals) {

      if (portal.getDirection() == direction) {
        return portal;
      }

    }

    return null;

  }

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

  public void rotateRoomAnticlockwise() {

    Tile[][] tempArray = new Tile[ROOMSIZE][ROOMSIZE];

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        tempArray[ROOMSIZE - col - 1][row] = this.tiles[row][col];
      }
    }

    this.tiles = tempArray;

  }

  public void addPortal(Portal portal) {
    portals.add(portal);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public Tile getTile(int row, int col) {
    return tiles[row][col];
  }

  public void setTile(Tile tile, int row, int col) {
    tiles[row][col] = tile;
  }

  public List<String> getDoors() {
    return doors;
  }

}
