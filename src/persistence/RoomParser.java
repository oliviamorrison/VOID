package persistence;

import gameworld.AccessibleTile;
import gameworld.Diffuser;
import gameworld.InaccessibleTile;
import gameworld.Room;
import gameworld.Tile;

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
          tile = new InaccessibleTile(room);
        } else {
          tile = new AccessibleTile(room);
          if (character == 'D') {
            Diffuser diffuser = new Diffuser();
            AccessibleTile accessibleTile = (AccessibleTile) tile;
            accessibleTile.setToken(diffuser);
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
}
