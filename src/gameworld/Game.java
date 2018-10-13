package gameworld;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main class the contains the logic for game play. It also connects the rooms va their doors
 * so that players can move between rooms.
 */
public class Game {

  private Room[][] board;
  private Player player;
  private Room currentRoom;
  private Timer timer;

  public Game(Room[][] board, Player player) {

    this.player = player;
    this.board = Arrays.copyOf(board, board.length);
    this.currentRoom = player.getRoom();
    createPortals();
    setupTimer();

  }

  public void setupTimer() {

    timer = new Timer();
    timer.schedule(new TimerTask() {

      @Override
      public void run() {
        player.loseHealth();
      }

    }, 0, 1000);

  }

  public void movePlayer(String direction) {
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

  public void moveRoom() {

    if (player.getTile() instanceof DoorTile) {

      DoorTile currentTile = (DoorTile) player.getTile();
      Room nextRoom = currentTile.getNeighbour();

      if (nextRoom == null)
        return;

      Direction oppositeDirection = currentTile.getDirection().getOppositeDirection();
      DoorTile nextTile = nextRoom.getNextDoorTile(oppositeDirection);
      nextTile.setPlayer(true);
      currentTile.setPlayer(false);
      currentRoom = nextRoom;
      player.setRoom(currentRoom);
      player.setTile(nextTile);

    }
  }

  public void unlockVendingMachine() {

    AccessibleTile t = player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkFacingChallenge(t, player.getDirectionFacing());

    if (challengeTile == null)
      return;

    ChallengeItem challenge = challengeTile.getChallenge();

    if (challenge instanceof VendingMachine) {
      VendingMachine v = (VendingMachine) challenge;

      if (!v.isUnlocked()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof BoltCutter) {
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

    AccessibleTile challengeTile = this.currentRoom.checkFacingChallenge(t, player.getDirectionFacing());

    if (challengeTile == null)
      return;

    ChallengeItem challenge = challengeTile.getChallenge();

    Item coin = null;

    if (challenge instanceof VendingMachine) {
      VendingMachine v = (VendingMachine) challenge;

      if (v.isUnlocked()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Coin) {
            coin = item;
          }
        }
      }
    }

    if (coin != null) {
      player.removeItem(coin);
      player.addItem(new Beer(-1, -1));
      System.out.println("Placed coin into vending machine...");
      System.out.println("Pick up the beer that is dispensed");
    }

  }

  public void bribeGuard() {

    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkFacingChallenge(t, player.getDirectionFacing());

    if (challengeTile == null)
      return;

    ChallengeItem challenge = challengeTile.getChallenge();

    Item beer = null;
    Guard g = null;

    if (challenge instanceof Guard) {
      g = (Guard) challenge;

      if (!g.isNavigable()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Beer) {
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


  public void pickUpItem() {
    AccessibleTile currentTile = player.getTile();
    if (currentTile.hasItem()) {
      if (!player.getInventory().isEmpty()) {
        System.out.println("Player can only have one item at a time");
        return;
      }
      Item item = currentTile.getItem();
      player.pickUp(item);
      currentTile.setItem(null);
      item.setX(-1);
      item.setY(-1);
      System.out.println("Player picked up " + item.toString());
    }
  }

  public void dropItem() {
    List<Item> inventory = player.getInventory();
    AccessibleTile currentTile = player.getTile();
    if (currentTile instanceof DoorTile) {
      return;
    }
    if (!currentTile.hasItem() && !inventory.isEmpty()) {
      Item item = player.getInventory().remove(0);
      item.setX(currentTile.getX());
      item.setY(currentTile.getY());
      currentTile.setItem(item);
      System.out.println("Player dropped " + item.toString());
    }
  }


  public void diffuseBomb() {
    AccessibleTile t = (AccessibleTile) player.getTile();

    AccessibleTile challengeTile = this.currentRoom.checkFacingChallenge(t, player.getDirectionFacing());

    if (challengeTile == null)
      return;

    ChallengeItem challenge = challengeTile.getChallenge();

    if (challenge instanceof Bomb) {
      Bomb b = (Bomb) challenge;
      if (!b.isNavigable()) {
        List<Item> pack = player.getInventory();
        for (Item item : pack) {
          if (item instanceof Diffuser) {
            b.setNavigable(true);
            System.out.println("Bomb diffused with " + item.toString());
          }
        }
      }
    }
  }

  private void createPortals() {

    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {

        Room room = board[row][col];

        if (room == null)
          continue;

        for (String direction : room.getDoors()) {

          switch (direction) {

            case "NORTH":
              if (row > 0)
                room.setTile(new DoorTile(board[row - 1][col], row, col, Direction.NORTH), Room.TOP.x, Room.TOP.y);
              break;
            case "SOUTH":
              if (row < board[row].length - 1)
                room.setTile(new DoorTile(board[row + 1][col], row, col, Direction.SOUTH), Room.BOTTOM.x, Room.BOTTOM.y);
              break;
            case "EAST":
              if (col < board.length - 1)
                room.setTile(new DoorTile(board[row][col + 1], row, col, Direction.EAST), Room.RIGHT.x, Room.RIGHT.y);
              break;
            case "WEST":
              if (col > 0)
                room.setTile(new DoorTile(board[row][col - 1], row, col, Direction.WEST), Room.LEFT.x, Room.LEFT.y);
              break;
            default:

          }
        }
      }
    }
  }

  public void rotateRoomClockwise() {

    player.setDirectionFacing(player.getDirectionFacing().getClockwiseDirection());
    currentRoom.rotateRoomClockwise();

  }

  public void rotateRoomAnticlockwise() {

    player.setDirectionFacing(player.getDirectionFacing().getAnticlockwiseDirection());
    currentRoom.rotateRoomAnticlockwise();

  }

  public void checkForHealthPack() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem()) {

      Item item = currentTile.getItem();

      if (item instanceof HealthPack) {
        player.boostHealth();
        currentTile.setItem(null);
      }

    }

  }

  public boolean checkForAntidote() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem())
      return currentTile.getItem() instanceof Antidote;

    return false;

  }

  public Room[][] getBoard() {
    return Arrays.copyOf(board, board.length);
  }

  public Player getPlayer() {
    return player;
  }

}

