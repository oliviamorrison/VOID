package gameworld;

import java.awt.Point;
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

  public Game(Room[][] board, Player player) {

    this.player = player;
    this.board = Arrays.copyOf(board, board.length);
    this.currentRoom = player.getRoom();
    connectPortals();

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

  public void teleport() {

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
    final Point northPortal = new Point(0, 5);
    final Point southPortal = new Point(9, 5);
    final Point eastPortal = new Point(5, 9);
    final Point westPortal = new Point(5, 0);

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
                x = northPortal.x;
                y = northPortal.y;
                portal = new Portal(x, y, board[row - 1][col], Direction.NORTH);
              }
              break;
            case "SOUTH":
              if (row < board[row].length - 1) {
                x = southPortal.x;
                y = southPortal.y;
                portal = new Portal(x, y, board[row + 1][col], Direction.SOUTH);
              }
              break;
            case "EAST":
              if (col < board.length - 1) {
                x = eastPortal.x;
                y = eastPortal.y;
                portal = new Portal(x, y, board[row][col + 1], Direction.EAST);
              }
              break;
            case "WEST":
              if (col > 0) {
                x = westPortal.x;
                y = westPortal.y;
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

  public String pickUpItem() {

    AccessibleTile tile = player.getTile();

    if (tile.hasItem()) {

      if (player.hasItem()) {
        System.out.println("Player may only have one item at a time");
        return "You may only carry one item at a time";
      }

      Item item = tile.getItem();
      player.addItem(item);
      tile.setItem(null);
      item.setRow(-1);
      item.setCol(-1);
      System.out.println("Player picked up " + item.toString());
      return "You picked up " + item.getName();

    }

    else
      return "Tile does not have an item you can pick up";
  }

  public String dropItem() {

    AccessibleTile tile = player.getTile();

    if (tile instanceof Portal) {
      return "You can't drop an item on a portal!";
    }

    if (!tile.hasItem() && !tile.hasChallenge() && player.hasItem()) {

      Item item = player.dropItem();
      item.setRow(tile.getRow());
      item.setCol(tile.getCol());
      tile.setItem(item);
      System.out.println("Player dropped " + item.toString());
      return "You dropped " + item.getName();
    }

    return "You don't have any items to drop";

  }

  public String diffuseBomb() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no bomb to be diffused";
    }

    if (challenge instanceof Bomb) {

      Bomb bomb = (Bomb) challenge;

      if (!bomb.isNavigable()) {

        Item item = player.getItem();

        if (item instanceof Diffuser) {
          bomb.setNavigable(true);
          return "Bomb successfully diffused";
        }

      }
    }
    return "You do not have a diffuser to diffuse the bomb!";
  }

  public String unlockVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no vending machine to be unlocked";
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;
      Direction vmDirection = vendingMachine.getDirection();

      if (vmDirection == Direction.EAST || vmDirection == Direction.WEST) {
        if (!vmDirection.equals(direction)) {
          return "";
        }
      } else {
        if (!direction.getOppositeDirection().equals(vmDirection)) {
          return "";
        }
      }

      if (!vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof BoltCutter) {
          vendingMachine.setUnlocked(true);
          return "Vending machine successfully unlocked and ready to use";
        }

      }
    }
    return "You do not have the bolt cutter to unlock the vending machine!";
  }

  public String useVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no vending machine to be used";
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;
      Direction vmDirection = vendingMachine.getDirection();

      if (vmDirection == Direction.EAST || vmDirection == Direction.WEST) {
        if (!vmDirection.equals(direction)) {
          return "";
        }
      } else {
        if (!direction.getOppositeDirection().equals(vmDirection)) {
          return "";
        }
      }

      if (vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof Coin) {

          player.dropItem();
          player.addItem(new Potion(-1, -1, "NORTH"));
          System.out.println("Placed coin into vending machine...");
          System.out.println("Pick up the potion that is dispensed");
          return "Magic potion successfully dispensed from vending machine";

        }
      }
    }
    return "You do not have a coin to unlock the vending machine!";
  }

  public String bribeGuard() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no Alien to befriend";
    }

    if (challenge instanceof Alien) {

      Alien alien = (Alien) challenge;

      if (!alien.isNavigable()) {

        Item item = player.getItem();

        if (item instanceof Potion) {

          player.dropItem();
          alien.setNavigable(true);
          Direction nextDirection =
              (player.getDirection() == Direction.NORTH || player.getDirection() == Direction.SOUTH) ?
                  player.getDirection().getOppositeDirection() : player.getDirection();
          alien.setDirection(nextDirection);
          System.out.println("Alien befriended with potion");
          return "Alien successfully befriended";

        }
      }
    }
    return "You do not have the magic potion to befriend the Alien!";
  }

  public void checkForHealthPack() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem()) {

      Item item = currentTile.getItem();

      if (item instanceof OxygenTank) {
        player.boostHealth();
        currentTile.setItem(null);
      }

    }

  }

  public boolean checkForAntidote() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem()) {
      return currentTile.getItem() instanceof SpaceShip;
    }

    return false;

  }

  public void directTeleport(Room room, int row, int col) {

    AccessibleTile tile = player.getTile();
    AccessibleTile nextTile = (AccessibleTile) room.getTile(row, col);
    tile.setPlayer(false);
    currentRoom = room;
    player.setTile(nextTile);
    nextTile.setPlayer(true);

  }

  public void rotateRoomClockwise() {

    player.setDirection(player.getDirection().getClockwiseDirection());
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Room room = board[row][col];
        if (room == null) {
          continue;
        }
        room.rotateRoomClockwise();
      }
    }

  }

  public void rotateRoomAnticlockwise() {

    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Room room = board[row][col];
        if (room == null) {
          continue;
        }
        room.rotateRoomAnticlockwise();
      }
    }

  }

  public Room[][] getBoard() {
    return Arrays.copyOf(board, board.length);
  }

  public Player getPlayer() {
    return player;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }
}

