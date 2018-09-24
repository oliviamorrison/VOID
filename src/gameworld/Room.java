package gameworld;

public class Room {
  private Tile[][] tiles;
  public static final int ROOMSIZE = 10;

  public Room() {
    //may need to change this depending on XML
    this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
  }

  public Tile moveTile(Tile t, int dx, int dy) {
    int[] coords = getTileCoordinates(t);

    int x = coords[0];
    int y = coords[1];

    int newX = x + dx;
    int newY = y + dy;

    //if the newCoordinates are inbounds and the tile is not inaacessible
    if (newX < 11 && newY < 11 && !(tiles[newX][newY] instanceof InaccessibleTile)) {
      return tiles[newX][newY];
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
        else if (tile instanceof AccessibleTile) {

          AccessibleTile accessibleTile = (AccessibleTile) tile;

          if (accessibleTile.hasPlayer() && accessibleTile.hasToken())
            room.append("!");
          else if (accessibleTile.hasPlayer())
            room.append("P");
          else if (accessibleTile.hasToken()) {
            Token token = accessibleTile.getToken();
            if (token instanceof Diffuser)
              room.append("D");
          } else if(accessibleTile.hasBomb()) {
            room.append("B");
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
}
