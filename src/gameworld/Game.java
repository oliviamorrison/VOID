package gameworld;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main class the contains the logic for game play. It also connects
 * the rooms va their doors so that players can move between rooms.
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
    connectPortals();
    setupTimer();

  }

  public void movePlayer(int dx, int dy) {

    AccessibleTile currentTile = player.getTile();
    Direction nextDirection = player.getDirection().nextDirection(dx, dy);

    if (player.changeDirection(nextDirection)) {
      return;
    }

    AccessibleTile nextTile = currentRoom.findNextTile(currentTile, dx, dy);

    if (nextTile != null) {

      currentTile.setPlayer(false);
      player.setTile(nextTile);
      nextTile.setPlayer(true);

    }

  }

  public void moveRoom() {

    if (player.getTile() instanceof Portal) {

      Portal portal = (Portal) player.getTile();
      Room nextRoom = portal.getNeighbour();

      if (nextRoom == null) {
        return;
      }

      Direction oppositeDirection = portal.getDirection().getOppositeDirection();
      Portal destination = nextRoom.getDestinationPortal(oppositeDirection);

      if (destination == null) {
        return;
      }

      destination.setPlayer(true);
      portal.setPlayer(false);
      currentRoom = nextRoom;
      player.setRoom(currentRoom);
      player.setTile(destination);

    }

  }

  private void connectPortals() {

    Portal portal = null;
    int x = -1;
    int y = -1;

    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {

        Room room = board[row][col];

        if (room == null) {
          continue;
        }

        for (String direction : room.getDoors()) {

          switch (direction) {

            case "NORTH":
              if (row > 0) {
                x = Room.NORTH_PORTAL.x;
                y = Room.NORTH_PORTAL.y;
                portal = new Portal(x, y, board[row - 1][col], Direction.NORTH);
              }
              break;
            case "SOUTH":
              if (row < board[row].length - 1) {
                x = Room.SOUTH_PORTAL.x;
                y = Room.SOUTH_PORTAL.y;
                portal = new Portal(x, y, board[row + 1][col], Direction.SOUTH);
              }
              break;
            case "EAST":
              if (col < board.length - 1) {
                x = Room.EAST_PORTAL.x;
                y = Room.EAST_PORTAL.y;
                portal = new Portal(x, y, board[row][col + 1], Direction.EAST);
              }
              break;
            case "WEST":
              if (col > 0) {
                x = Room.WEST_PORTAL.x;
                y = Room.WEST_PORTAL.y;
                portal = new Portal(x, y, board[row][col - 1], Direction.WEST);
              }
              break;
            default:

          }

          if (portal != null && x > -1 && y > -1) {
            room.addPortal(portal);
            room.setTile(portal, x, y);
          }

        }
      }
    }
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

  public void pickUpItem() {

    AccessibleTile tile = player.getTile();

    if (tile.hasItem()) {

      if (player.hasItem()) {
        System.out.println("Player may only have one item at a time");
        return;
      }

      Item item = tile.getItem();
      player.addItem(item);
      tile.setItem(null);
      item.setRow(-1);
      item.setCol(-1);
      System.out.println("Player picked up " + item.toString());

    }

  }

  public void dropItem() {

    AccessibleTile tile = player.getTile();

    if (tile instanceof Portal) {
      return;
    }

    if (!tile.hasItem() && !tile.hasChallenge() && player.hasItem()) {

      Item item = player.dropItem();
      item.setRow(tile.getRow());
      item.setCol(tile.getCol());
      tile.setItem(item);
      System.out.println("Player dropped " + item.toString());

    }

  }

  public void diffuseBomb() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return;
    }

    if (challenge instanceof Bomb) {

      Bomb bomb = (Bomb) challenge;

      if (!bomb.isNavigable()) {

        Item item = player.getItem();

        if (item instanceof Diffuser) {
          bomb.setNavigable(true);
          System.out.println("Bomb diffused with " + item.toString());
        }

      }
    }
  }

  public void unlockVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return;
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;

      if (!vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof BoltCutter) {
          vendingMachine.setUnlocked(true);
          System.out.println("Chains are removed from Vending machine");
          System.out.println("Vending machine is available for use");
        }

      }
    }
  }

  public void useVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return;
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;

      if (vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof Coin) {

          player.dropItem();
          player.addItem(new Beer(-1, -1));
          System.out.println("Placed coin into vending machine...");
          System.out.println("Pick up the beer that is dispensed");

        }
      }
    }

  }

  public void bribeGuard() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return;
    }

    if (challenge instanceof Guard) {

      Guard guard = (Guard) challenge;

      if (!guard.isNavigable()) {

        Item item = player.getItem();

        if (item instanceof Beer) {

          player.dropItem();
          guard.setNavigable(true);
          System.out.println("Guard bribed with beer");

        }
      }
    }

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

    if (currentTile.hasItem()) {
      return currentTile.getItem() instanceof Antidote;
    }

    return false;

  }

  public void rotateRoomClockwise() {

    player.setDirection(player.getDirection().getClockwiseDirection());
    currentRoom.rotateRoomClockwise();

  }

  public void rotateRoomAnticlockwise() {

    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    currentRoom.rotateRoomAnticlockwise();

  }

  public Room[][] getBoard() {
    return Arrays.copyOf(board, board.length);
  }

  public Player getPlayer() {
    return player;
  }

}

