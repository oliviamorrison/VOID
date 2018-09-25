package gameworld;

//import persistence.RoomParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {

  private Room[][] board;
  private static Player player;
  private Room currentRoom;
  private List<Room> rooms = new ArrayList<>();

  public Game(Room[][] board, Player player) {
    this.player = player;
    this.board = board;
    this.currentRoom = player.getRoom();
  }
//
//  public Game() {
//    setup();
//    startGame();
//  }
//
//  public Game(boolean testing) {
//    setup();
//  }

  public static void movePlayer(String direction) {
    int dx = 0;
    int dy = 0;

    switch (direction) {
      case "w":
        dx = -1;
        break;
      case "a":
        dy = -1;
        break;
      case "s":
        dx = 1;
        break;
      case "d":
        dy = 1;
        break;
      default:

    }

    player.moveTile(dx, dy);
  }

//  public void setup() {
//    // create a starting room for testing
//    Room defaultRoom = RoomParser.createRoom(RoomParser.getBombRoom());
//    currentRoom = defaultRoom;
//    rooms.add(defaultRoom);
//    AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
//    player = new Player(defaultRoom, startingTile);
//    startingTile.setPlayer(true);
//  }

  public void startGame() {

    connectRooms();
    while (true) {
      currentRoom.draw();
      startTurn();
    }

  }

  public void startTurn() {
    String input = inputString("Move:m Pickup:u Drop:d Diffuse:f Use Door:r");
    switch (input) {
      case "m":
        movePlayer();
        break;
      case "u":
        pickUpItem();
        break;
      case "d":
        dropItem();
        break;
      case "f":
        diffuseBomb();
        break;
      case "r":
        moveRoom();
        break;
      default:

    }
  }

  public void moveRoom() {

    if (player.getTile() instanceof DoorTile) {

      DoorTile currentTile = (DoorTile) player.getTile();
      Room nextRoom = findNextRoom(currentTile);

      if (nextRoom == null)
        return;

      DoorTile nextTile = nextRoom.getNextDoorTile(currentTile.getOppoositeDirection(currentTile.getDirection()));
      nextTile.setPlayer(true);
      currentTile.setPlayer(false);
      currentRoom = nextRoom;
      player.setRoom(currentRoom);
      player.setTile(nextTile);

    }
  }

  // for testing purposes
  public String drawRoom() {
    return currentRoom.draw();
  }

  // for testing purposes
  public Player getPlayer() {
    return player;
  }

  public void movePlayer() {
    String dir = inputString("Direction: ");
    movePlayer(dir);
  }

  public void pickUpItem() {
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (currentTile.hasToken()) {
      player.pickUp(currentTile.getToken());
      currentTile.setToken(null);
    }
  }

  public void dropItem() {
    List<Token> inventory = player.getInventory();
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (!currentTile.hasToken() && !inventory.isEmpty()) {
      currentTile.setToken(player.getInventory().remove(0));
    }
  }

  public void diffuseBomb() {
    AccessibleTile t = (AccessibleTile) player.getTile();
    if (!t.hasBomb()) {
      System.out.println("No Bomb here.");
      return;
    }
    List<Token> inventory = player.getInventory();
    Boolean hasdiffuser = false;
    for (Token token : inventory) {
      if (token instanceof Diffuser)
        hasdiffuser = true;
    }
    if (!hasdiffuser) {
      System.out.println("You need a diffuser to diffuse the bomb.");
      return;
    }
    System.out.println("You diffused the bomb." + t.getBomb().isActive());
    t.getBomb().setActive(false);
  }

  private static String inputString(String msg) {
    System.out.print(msg + " ");
    while (true) {
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      try {
        return input.readLine();
      } catch (IOException e) {
        System.out.println("I/O Error ... please try again!");
      }
    }
  }

  private Room findNextRoom(DoorTile tile) {

    Direction direction = tile.getDirection();

    int roomRow = 0;
    int roomCol = 0;

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {

        if (currentRoom.equals(board[i][j])) {
          roomRow = i;
          roomCol = j;
          break;
        }
      }
    }

    switch (direction) {
      case Top:
        roomRow -= 1;
        break;
      case Bottom:
        roomRow += 1;
      case Left:
        roomCol -= 1;
      case Right:
        roomCol += 1;
    }

    if (roomCol < 0 || roomCol >= board.length || roomRow < 0 || roomRow >= board[0].length)
      return null;

    return board[roomRow][roomCol];

  }

  private void connectRooms() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        Room room = board[i][j];
        if (room != null) {
          for (String dir : room.getDoors()) {
            switch (dir) {
              case "left":
                if (i > 0) {
                  room.setTile(new DoorTile(board[i - 1][j], room, Direction.Left), Room.LEFT.x, Room.LEFT.y);
                  break;
                }
              case "right":
                if (i < board.length) {
                  room.setTile(new DoorTile(board[i + 1][j], room, Direction.Right), Room.RIGHT.x, Room.RIGHT.y);
                  break;
                }
              case "top":
                if (j > 0) {
                  room.setTile(new DoorTile(board[i][j - 1], room, Direction.Top), Room.TOP.x, Room.TOP.y);
                  break;
                }
              case "bottom":
                if (j < board[i].length) {
                  room.setTile(new DoorTile(board[i][j + 1], room, Direction.Bottom), Room.BOTTOM.x, Room.BOTTOM.y);
                  break;
                }
            }
          }
        }
      }
    }
  }

}
