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

  public void draw() {
    
  }
}
