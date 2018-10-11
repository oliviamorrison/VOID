package gameworld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

  private int row;
  private int col;
  private Tile[][] tiles;
  private List<String> doors;
  private boolean hasHealthPack = false;

  public static final Point TOP = new Point(0, 5);
  public static final Point BOTTOM = new Point(9, 5);
  public static final Point LEFT = new Point(5, 0);
  public static final Point RIGHT = new Point(5, 9);
  public static final int ROOMSIZE = 10;

  public Room(int row, int col, Tile[][] tiles, List<String> doors){
    this.row = row;
    this.col = col;
    this.tiles = tiles;
    this.doors = doors;
  }

  public Room() {
    this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
    for (int i = 0; i < ROOMSIZE; i++) {
      for (int j = 0; j < ROOMSIZE; j++) {
        if (i == 0 || j == 0 || j == ROOMSIZE - 1 || i == ROOMSIZE - 1)
          tiles[i][j] = new InaccessibleTile(i, j);
        else tiles[i][j] = new AccessibleTile(i, j);
      }
    }
    this.doors = new ArrayList<>();
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public List<String> getDoors() {
    return doors;
  }

  public Tile moveTile(Tile t, int dx, int dy) {
//    based on dx and dy values change currentdirection of player

    int[] coordinates = getTileCoordinates(t);

    assert coordinates != null;

    int x = coordinates[0];
    int y = coordinates[1];

    int newX = x + dx;
    int newY = y + dy;

    if (newX < 0 || newY < 0 || newX >= 10 || newY >= 10)
      return null;

    Tile tile = tiles[newX][newY];

    // cannot move onto bomb until disabled
    if (tile instanceof AccessibleTile) {
      AccessibleTile at = (AccessibleTile) tile;
      if (at.hasChallenge()) {
        Challenge c = at.getChallenge();
        if (!c.isNavigable()) {
          return null;
        }
      }
    }

    //if the newCoordinates are inbounds and the tile is not inaccessible
    if (!(tile instanceof InaccessibleTile)) {
      return tile;
    }

    return null;
  }

  private int[] getTileCoordinates(Tile t) {
    for (int i = 0; i < ROOMSIZE; i++) {
      for (int j = 0; j < ROOMSIZE; j++) {

        //returns coordinates of the tile
        if (tiles[i][j].equals(t)) return new int[]{i, j};
      }
    }

    return null;
  }

  public Tile getTile(int row, int col) {
    return tiles[row][col];
  }

  public void setTile(Tile tile, int row, int col) {
    tiles[row][col] = tile;
  }

  public AccessibleTile checkChallengeNearby(AccessibleTile tile) {


    Tile t;

    for (Direction direction : Direction.values()) {

      int row = tile.getX();
      int col = tile.getY();

      switch (direction) {
        case Left:
          col -= 1;
          break;
        case Right:
          col += 1;
          break;
        case Top:
          row -= 1;
          break;
        case Bottom:
          row += 1;
          break;
      }

      t = tiles[row][col];
      if (t instanceof InaccessibleTile || t instanceof DoorTile) {
      } else {
        AccessibleTile a = (AccessibleTile) t;
        if (a.hasChallenge())
          return a;
      }
    }

    return null;

  }

  /**
   * This method creates and prints out the visual
   * representation of the room to the user.
   */
  public String draw() {

    StringBuilder room = new StringBuilder();

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {

        Tile tile = tiles[row][col];

        if (tile instanceof InaccessibleTile)
          room.append("X");
        else if (tile instanceof DoorTile) {
          DoorTile doorTile = (DoorTile) tile;

          if (doorTile.hasPlayer())
            room.append("!");
          else
            room.append("0");
        } else if (tile instanceof AccessibleTile) {
          AccessibleTile accessibleTile = (AccessibleTile) tile;

          if (accessibleTile.hasPlayer() && accessibleTile.hasItem())
            room.append("!");
          else if (accessibleTile.hasPlayer())
            room.append("P");
          else if (accessibleTile.hasItem()) {
            Item item = accessibleTile.getItem();
            if (item.equals(Item.Diffuser))
              room.append("D");
            if (item.equals(Item.Antidote))
              room.append("A");
            if (item.equals(Item.Coin))
              room.append("C");
            if (item.equals(Item.Beer))
              room.append("R");
            if (item.equals(Item.BoltCutter))
              room.append("Z");
            if (item.equals(Item.HealthPack))
              room.append("H");
          } else if (accessibleTile.hasChallenge()) {
            Challenge challenge = accessibleTile.getChallenge();
            if (challenge instanceof Bomb)
              room.append("B");
            if (challenge instanceof VendingMachine)
              room.append("V");
            if (challenge instanceof Guard)
              room.append("G");
          } else
            room.append(" ");
        }
        if (col < ROOMSIZE - 1)
          room.append(" ");
      }
      room.append("\n");
    }
    System.out.println(room.toString());
    return room.toString();
  }

  public DoorTile getNextDoorTile(Direction dir) {

    Point point = null;

    for (String direction : doors) {

      String d = dir.toString();

      if (d.equals(direction))
        point = getNextPoint(dir);

    }

    assert point != null;

    return (DoorTile) (tiles[point.x][point.y]);

  }

  public Point getNextPoint(Direction direction) {

    switch (direction) {
      case Left:
        return LEFT;
      case Right:
        return RIGHT;
      case Bottom:
        return BOTTOM;
      case Top:
        return TOP;
      default:
        return null;
    }
  }

  public AccessibleTile getPlayerTile() {
    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        if (this.tiles[row][col] instanceof AccessibleTile) {
          AccessibleTile tile = (AccessibleTile) this.tiles[row][col];
          if (tile.hasPlayer()) {
            return tile;
          }
        }
      }
    }
    return null;
  }

  public void rotateRoomAnticlockwise() {
    int x = ROOMSIZE / 2;
    int y = ROOMSIZE - 1;
    for (int i = 0; i < x; i++) {
      for (int j = i; j < y - i; j++) {
        Tile value = this.tiles[i][j];
        this.tiles[i][j] = this.tiles[y - j][i];
        this.tiles[y - j][i] = this.tiles[y - i][y - j];
        this.tiles[y - i][y - j] = this.tiles[j][y - i];
        this.tiles[j][y - i] = value;
      }
    }
  }

  public void rotateRoomClockwise(){
    Tile[][] tempArray = new Tile[ROOMSIZE][ROOMSIZE];
    for(int row = 0; row < ROOMSIZE; row++){
      for(int col = 0; col < ROOMSIZE; col++){
        tempArray[ROOMSIZE - col - 1][row] = this.tiles[row][col];
      }
    }
    this.tiles = tempArray;
  }

}
