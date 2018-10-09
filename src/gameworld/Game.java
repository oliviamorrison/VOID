package gameworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This is the main class the contains the logic for game play. It also connects the rooms va their doors
 * so that players can move between rooms.
 */
public class Game {

  private Room[][] board;
  private static Player player;
  private Room currentRoom;

  public Game(Room[][] board, Player player) {
    Game.player = player;
    this.board = board;
    this.currentRoom = player.getRoom();
  }

  private static void movePlayer(String direction) {
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
      if (player.getTile().hasItem()) {
        if (player.getTile().getItem() instanceof Antidote) {
          System.out.println("you win");
          return;
        }
      }
      currentRoom.draw();
      startTurn();
    }
  }

  private void startTurn() {
    String input = inputString("Move:m Pickup:u Drop:d Diffuse:f Unlock Vend:t use Vend:v Use Door:r");
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
      case "t":
        unlockVendingMachine();
        break;
      case "v":
        useVendingMachine();
        break;
      case "r":
        moveRoom();
        break;
      case "b":
        bribeGuard();
        break;
      default:

    }
  }

  private void moveRoom() {

    if (player.getTile() instanceof DoorTile) {

      DoorTile currentTile = (DoorTile) player.getTile();
      Room nextRoom = findNextRoom(currentTile);

      if (nextRoom == null)
        return;

      DoorTile nextTile = nextRoom.getNextDoorTile(currentTile.getOppositeDirection(currentTile.getDirection()));
      nextTile.setPlayer(true);
      currentTile.setPlayer(false);
      currentRoom = nextRoom;
      player.setRoom(currentRoom);
      player.setTile(nextTile);

    }
  }

  public void unlockVendingMachine() {

    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkChallengeNearby(t);

    if (challengeTile == null)
      return;

    Challenge challenge = challengeTile.getChallenge();

    if (challenge instanceof VendingMachine) {
      VendingMachine v = (VendingMachine) challenge;

      if (!v.isUnlocked()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof BoltCutter) {
            v.setUnlocked(true);
          }
        }
      }
    }

  }

  public void useVendingMachine() {

    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkChallengeNearby(t);

    if (challengeTile == null)
      return;

    Challenge challenge = challengeTile.getChallenge();

    Coin coin = null;

    if (challenge instanceof VendingMachine) {
      VendingMachine v = (VendingMachine) challenge;

      if (v.isUnlocked()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Coin) {
            coin = (Coin) item;
          }
        }
      }
    }

    if (coin != null) {
      player.removeItem(coin);
      player.addItem(new Beer());
    }

  }

  public void bribeGuard() {

    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkChallengeNearby(t);

    if (challengeTile == null)
      return;

    Challenge challenge = challengeTile.getChallenge();

    Beer beer = null;
    Guard g = null;

    if (challenge instanceof Guard) {
       g = (Guard) challenge;

      if (!g.isNavigable()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Beer) {
            beer = (Beer) item;
          }
        }
      }
    }

    if (beer != null) {
      player.removeItem(beer);
      g.setNavigable(true);
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
    if (currentTile.hasItem()) {
      player.pickUp(currentTile.getItem());
      currentTile.setItem(null);
    }
  }

  public void dropItem() {
    List<Item> inventory = player.getInventory();
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (!currentTile.hasItem() && !inventory.isEmpty()) {
      currentTile.setItem(player.getInventory().remove(0));
    }
  }

  public void diffuseBomb() {
    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkChallengeNearby(t);

    if (challengeTile == null)
      return;

    Challenge challenge = challengeTile.getChallenge();

    if (challenge instanceof Bomb) {
      Bomb b = (Bomb) challenge;
      if (!b.isNavigable()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Diffuser) {
            b.setNavigable(true);
          }
        }
      }
    }
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
        break;
      case Left:
        roomCol -= 1;
        break;
      case Right:
        roomCol += 1;
        break;
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
              // flip that shit to make it work
              case "left":
                if (j > 0) {
                  room.setTile(new DoorTile(board[i][j - 1], room, i, j, Direction.Left), Room.LEFT.x, Room.LEFT.y);
                  break;
                }
              case "right":
                if (j < board.length - 1) {
                  room.setTile(new DoorTile(board[i][j + 1], room, i, j, Direction.Right), Room.RIGHT.x, Room.RIGHT.y);
                  break;
                }
              case "top":
                if (i > 0) {
                  room.setTile(new DoorTile(board[i - 1][j], room, i, j, Direction.Top), Room.TOP.x, Room.TOP.y);
                  break;
                }
              case "bottom":
                if (i < board[i].length - 1) {
                  room.setTile(new DoorTile(board[i + 1][j], room, i, j, Direction.Bottom), Room.BOTTOM.x, Room.BOTTOM.y);
                  break;
                }
            }
          }
        }
      }
    }
  }

  public Room[][] getBoard() {
    return board;
  }
}

