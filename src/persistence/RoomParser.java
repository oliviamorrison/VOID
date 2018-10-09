
package persistence;

import gameworld.*;

public class RoomParser {


/**
   * This method creates a new room based on the information
   * passed in from a data file. (should have a file as an argument)
   * @return new room as described by the data file
   */

  public static Room createRoom(String[] data) {

    Room room = new Room();
    Tile tile;

    for (int row = 0, count = 0; row < Room.ROOMSIZE; row++) {
      for (int col = 0; col < Room.ROOMSIZE; col++) {

        Character character = data[row].charAt(count);

        if (character == 'X') {
          tile = new InaccessibleTile(room, row, col);
        } else {
          tile = new AccessibleTile(room, row, col);
          if (character == 'D') {
            AccessibleTile accessibleTile = (AccessibleTile) tile;
            accessibleTile.setItem(diffuser);
          }
          else if(character == 'B') {
        	  Bomb bomb = new Bomb("");
        	  AccessibleTile accessibleTile = (AccessibleTile) tile;
              accessibleTile.setBomb(bomb);
          }
        }
        room.setTile(tile, row, col);
        count++;
      }
      count = 0;
    }

    return room;

  }


/**
   * This method creates a default room with a single token.
   * @return default room information as string[]
   */

  public static String[] getDefaultRoom() {

    String file = "X X X X X X X X X X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - D - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X X X X X X X X X X\n";

    return file.replace(" ", "").split("\n");

  }
  public static String[] getBombRoom() {

    String file = "X X X X X X X X X X\n"
        + "X - - - - - - - - X\n"
        + "X D - - B - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X - - - - - - - - X\n"
        + "X X X X X X X X X X\n";

    return file.replace(" ", "").split("\n");

  }
}

