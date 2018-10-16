package gameworld;

import java.util.Arrays;

/**
 * This is the main class the contains the logic for game play. It also connects
 * the rooms va their doors so that players can move between rooms.
 *
 * @author Latrell Whata 300417220
 */
public class Game {

  private Room[][] board;
  private Player player;
  private Room currentRoom;

  /**
   * This constructor creates a new void game.
   *
   * @param board  the 2D array of room objects that make up the game
   * @param player the current game player
   */
  public Game(Room[][] board, Player player) {

    this.player = player;
    this.board = Arrays.copyOf(board, board.length);
    this.currentRoom = player.getRoom();
    connectPortals();

  }

  /**
   * This method moves the player from its current position to a new tile.
   *
   * @param dx the difference between the row values
   * @param dy the difference between the column values
   */
  public void movePlayer(int dx, int dy) {

    AccessibleTile currentTile = player.getTile();
    Direction nextDirection = player.getDirection().nextDirection(dx, dy);

    // only change direction of player
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

  /**
   * This method moves a player to an adjacent room.
   */
  public void teleport() {

    if (player.getTile() instanceof Portal) {

      // find neighbouring room
      Portal portal = (Portal) player.getTile();
      Room nextRoom = portal.getNeighbour();

      if (nextRoom == null) {
        return;
      }

      // next portal is in the adjacent room, in the opposite direction of the current portal
      Direction oppositeDirection = portal.getDirection().getOppositeDirection();
      Portal destination = nextRoom.getDestinationPortal(oppositeDirection);

      if (destination == null) {
        return;
      }

      // update player position, relevant tiles
      destination.setPlayer(true);
      portal.setPlayer(false);
      currentRoom = nextRoom;
      player.setRoom(currentRoom);
      player.setTile(destination);

    }

  }

  /**
   * This method connects each portal to the neighbouring room.
   */
  private void connectPortals() {

    // row, col values for each portal
    final int[] northPortal = new int[]{0, 5};
    final int[] southPortal = new int[]{9, 5};
    final int[] eastPortal = new int[]{5, 9};
    final int[] westPortal = new int[]{5, 0};

    Portal portal = null;
    int x = -1;
    int y = -1;

    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {

        Room room = board[row][col];

        if (room == null) {
          continue;
        }

        // iterate through each door within a room
        for (String direction : room.getDoors()) {

          switch (direction) {

            case "NORTH":
              if (row > 0) {
                x = northPortal[0];
                y = northPortal[1];
                portal = new Portal(x, y, board[row - 1][col], Direction.NORTH);
              }
              break;
            case "SOUTH":
              if (row < board[row].length - 1) {
                x = southPortal[0];
                y = southPortal[1];
                portal = new Portal(x, y, board[row + 1][col], Direction.SOUTH);
              }
              break;
            case "EAST":
              if (col < board.length - 1) {
                x = eastPortal[0];
                y = eastPortal[1];
                portal = new Portal(x, y, board[row][col + 1], Direction.EAST);
              }
              break;
            case "WEST":
              if (col > 0) {
                x = westPortal[0];
                y = westPortal[1];
                portal = new Portal(x, y, board[row][col - 1], Direction.WEST);
              }
              break;
            default:

          }

          // create portals and add them to the corresponding rooms
          if (portal != null && x > -1 && y > -1) {
            room.addPortal(portal);
            room.setTile(portal, x, y);
          }

        }
      }
    }
  }

  /**
   * This method picks up an item from the tile.
   *
   * @return a notification message for the user
   */
  public String pickUpItem() {

    AccessibleTile tile = player.getTile();

    if (tile.hasItem()) {

      // player has a pack weight limit of one
      if (player.hasItem()) {
        return "You may only carry one item at a time";
      }

      // add item to player, remove from tile
      Item item = tile.getItem();
      player.addItem(item);
      tile.setItem(null);
      item.setRow(-1);
      item.setCol(-1);
      return "You picked up " + item.getName();

    } else {
      return "Tile does not have an item you can pick up";
    }
  }

  /**
   * This method drops an item from a player to a tile.
   *
   * @return a notification message for the user
   */
  public String dropItem() {

    AccessibleTile tile = player.getTile();

    if (tile instanceof Portal) {
      return "You can't drop an item on a portal!";
    }

    // limit of one item per tile
    if (tile.hasItem()) {
      return "You can't drop an item on another item!";
    }

    if (!tile.hasItem() && !tile.hasChallenge() && player.hasItem()) {

      Item item = player.dropItem();
      item.setRow(tile.getRow());
      item.setCol(tile.getCol());
      tile.setItem(item);
      return "You dropped " + item.getName();
    }

    return "You don't have any items to drop";

  }

  /**
   * This method diffuses a bomb challenge.
   *
   * @return a notification message for the user
   */
  public String diffuseBomb() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    // check there is a challenge directly opposite the player
    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no bomb to be diffused";
    }

    if (challenge instanceof Bomb) {

      Bomb bomb = (Bomb) challenge;

      // confirm bomb hasn't already been diffused
      if (!bomb.isNavigable()) {

        Item item = player.getItem();

        if (item instanceof Diffuser) {
          bomb.setNavigable(true);
          return "Bomb successfully diffused";
        }

      }

      if (bomb.isNavigable()) {
        return "You have already diffused the bomb, it is safe to walk over";
      }

    }

    return "You do not have a diffuser to diffuse the bomb!";

  }

  /**
   * This method unlocks a vending machine challenge.
   *
   * @return a notification message for the user
   */
  public String unlockVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    // check there is a challenge directly opposite the player
    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no vending machine to be unlocked";
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;
      Direction vmDirection = vendingMachine.getDirection();

      // vending machine must be facing towards the player to interact with it
      if (!direction.getOppositeDirection().equals(vmDirection)) {
        return "";
      }

      if (!vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof BoltCutter) {
          vendingMachine.setUnlocked(true);
          return "Vending machine successfully unlocked and ready to use";
        }
      }

      if (vendingMachine.isUnlocked()) {
        return "You have already unlocked the vending machine!";
      }

    }

    return "You do not have the bolt cutter to unlock the vending machine!";

  }

  /**
   * This method uses a vending machine challenge.
   *
   * @return a notification message for the user
   */
  public String useVendingMachine() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    // check there is a challenge directly opposite the player
    ChallengeItem challenge = this.currentRoom.getAdjacentChallenge(tile, direction);

    if (challenge == null) {
      return "There is no vending machine to be used";
    }

    if (challenge instanceof VendingMachine) {

      VendingMachine vendingMachine = (VendingMachine) challenge;
      Direction vmDirection = vendingMachine.getDirection();

      // vending machine must be facing towards the player to interact with it
      if (!direction.getOppositeDirection().equals(vmDirection)) {
        return "";
      }

      if (vendingMachine.isUnlocked()) {

        Item item = player.getItem();

        if (item instanceof Coin) {

          player.dropItem();
          player.addItem(new Potion(-1, -1, "NORTH"));
          return "Magic potion successfully dispensed from vending machine";

        }
      }
    }
    return "You do not have a coin to unlock the vending machine!";
  }

  /**
   * This method befriends an alien challenge.
   *
   * @return a notification message for the user
   */
  public String befriendAlien() {

    AccessibleTile tile = player.getTile();
    Direction direction = player.getDirection();

    // check there is a challenge directly opposite the player
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

          // update the alien direction so that it is facing the player
          Direction nextDirection =
              (direction == Direction.NORTH || direction == Direction.SOUTH)
                  ? direction.getOppositeDirection() : direction;
          alien.setDirection(nextDirection);
          return "Alien successfully befriended";

        }
      }
      if (alien.isNavigable()) {
        return "You have already made friends with the Alien, you may pass.";
      }
    }

    return "You do not have the magic potion to befriend the Alien!";

  }

  /**
   * This method checks for a oxygen tank where the player is located.
   *
   * @return a notification message for the user
   */
  public String checkForOxygenTank() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem()) {

      Item item = currentTile.getItem();

      if (item instanceof OxygenTank) {
        player.boostOxygen();
        currentTile.setItem(null);
        return "You replenished your oxygen supply";
      }
    }

    return "";

  }

  /**
   * This method checks if the player has found it's spaceship.
   *
   * @return whether or not the spaceship is on the player tile
   */
  public boolean checkForSpaceship() {

    AccessibleTile currentTile = player.getTile();

    if (currentTile.hasItem()) {
      return currentTile.getItem() instanceof SpaceShip;
    }

    return false;

  }

  /**
   * This method is a helper for the test class. It enables a
   * player to be directly moved to a room at a specific tile.
   *
   * @param room the destination room
   * @param row  the row value of the new player tile
   * @param col  the column value of the new player tile
   */
  public void directTeleport(Room room, int row, int col) {

    AccessibleTile tile = player.getTile();
    AccessibleTile nextTile = (AccessibleTile) room.getTile(row, col);
    tile.setPlayer(false);
    currentRoom = room;
    player.setTile(nextTile);
    nextTile.setPlayer(true);

  }

  /**
   * This method rotates the every room on the board clockwise.
   */
  public void rotateRoomClockwise() {

    // update the player direction
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

  /**
   * This method rotates the every room on the board anticlockwise.
   */
  public void rotateRoomAnticlockwise() {

    // update the player direction
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

  /**
   * This method is a getter for the game board.
   *
   * @return the current game board
   */
  public Room[][] getBoard() {
    return Arrays.copyOf(board, board.length);
  }

  /**
   * This method is a getter for the game player.
   *
   * @return the current game player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * This method is a getter for the current room for the game.
   *
   * @return the current game room
   */
  public Room getCurrentRoom() {
    return currentRoom;
  }
}

