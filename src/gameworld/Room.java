package gameworld;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Room {

    private Tile[][] tiles;
    private List<Token> items;
    private final int ROOMSIZE = 10;

    private static final Point TOP = new Point(0,5);
    private static final Point BOTTOM = new Point(9,5);
    private static final Point LEFT = new Point(5,0);
    private static final Point RIGHT = new Point(5,9);


    public Room(HashMap<String, DoorTile> doors, List<Token> items){
        //may need to change this depending on XML
        this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
        this.items = items;

        //For now until we can load in an XML file
        for(int i = 0; i < ROOMSIZE; i++){
            for(int j = 0; j < ROOMSIZE; j++){
                if(i == 0 || j == 0 || j == ROOMSIZE-1 || i == ROOMSIZE-1) tiles[i][j] = new InaccessibleTile(this);
                else tiles[i][j] = new AccessibleTile(this);
            }
        }

        for(String direction : doors.keySet()){
            switch(direction){
                case "left": tiles[LEFT.y][LEFT.x] = new DoorTile(null, this); //null for now
                case "right": tiles[RIGHT.y][RIGHT.x] = new DoorTile(null, this); //null for now
                case "top": tiles[TOP.y][TOP.x] = new DoorTile(null, this); //null for now
                case "bottom": tiles[BOTTOM.y][BOTTOM.x] = new DoorTile(null, this); //null for now
            }
        }

        for(Token item : this.items){
            boolean itemPlaced = false;
            while(!itemPlaced){
                int randomX = (int)(Math.random() * 8)+1;
                int randomY = (int)(Math.random() * 8)+1;
                if (tiles[randomY][randomX] instanceof AccessibleTile ) {
                    AccessibleTile tile = (AccessibleTile)tiles[randomY][randomX];
                    if(!tile.hasToken()){
                        tile.setToken(item);
                        itemPlaced = true;
                    }
                }
            }
        }

        //TODO: and add doors depending on direction in room
        //This is just to test if door checking works
        DoorTile door = new DoorTile(null, this);
        tiles[0][0] = new DoorTile(door, this);


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
