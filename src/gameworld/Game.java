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
  private int health = 100;

  public Game(Room[][] board, Player player) {
    Game.player = player;
    this.board = board;
    this.currentRoom = player.getRoom();
    connectRooms();
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

  public void startTurn(String input) {
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

  public void moveRoom() {

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

    System.out.println("You are unlocking");
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
          if (item.equals(Item.BoltCutter)) {
            v.setUnlocked(true);
            System.out.println("Chains are removed from Vending machine");
            System.out.println("Vending machine is available for use");
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

    Item coin = null;

    if (challenge instanceof VendingMachine) {
      VendingMachine v = (VendingMachine) challenge;

      if (v.isUnlocked()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item.equals(Item.Coin)) {
            coin = item;
          }
        }
      }
    }

    if (coin != null) {
      player.removeItem(coin);
      player.addItem(Item.Beer);
      System.out.println("Placed coin into vending machine...");
      System.out.println("Pick up the beer that is dispensed");
    }

  }

  public void bribeGuard() {

    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkChallengeNearby(t);

    if (challengeTile == null)
      return;

    Challenge challenge = challengeTile.getChallenge();

    Item beer = null;
    Guard g = null;

    if (challenge instanceof Guard) {
      g = (Guard) challenge;

      if (!g.isNavigable()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item.equals(Item.Beer)) {
            beer = item;
          }
        }
      }
    }

    if (beer != null) {
      player.removeItem(beer);
      g.setNavigable(true);
      System.out.println("Guard bribed with beer");
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
      Item item = currentTile.getItem();
      player.pickUp(item);
      currentTile.setItem(null);
      System.out.println("Player picked up " + item.toString());
    }
  }

  public void dropItem() {
    List<Item> inventory = player.getInventory();
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (!currentTile.hasItem() && !inventory.isEmpty()) {
      Item item = player.getInventory().remove(0);
      currentTile.setItem(item);
      System.out.println("Player dropped " + item.toString());
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
          if (item.equals(Item.Diffuser)) {
            b.setNavigable(true);
            System.out.println("Bomb diffused with " + item.toString());
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

