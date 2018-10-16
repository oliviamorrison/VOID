package gameworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.XmlParser;
import renderer.Renderer;

/**
 * This class tests all of the gameworld logic.
 *
 * @author Latrell Whata 300417220
 */
public class GameworldTests {

  private Game game;
  private Room[][] board;
  private Player player;
  private List<Item> items;

  /**
   * This method sets up a test scenario to ease testing.
   *
   * @throws XmlParser.ParseError xml parsing error
   */
  @BeforeEach
  public void setUp() throws XmlParser.ParseError {

    game = XmlParser.parseGame(new File("test_data/gameworldTestData.xml"));

    if (game != null) {

      board = game.getBoard();
      player = game.getPlayer();
      items = new ArrayList<>(Arrays.asList(
          new SpaceShip(-1, -1, "NORTH"),
          new Potion(-1, -1, "NORTH"),
          new Potion(-1, -1, "NORTH"),
          new BoltCutter(-1, -1, "NORTH"),
          new Bomb(-1, -1, "NORTH"),
          new Coin(-1, -1, "NORTH"),
          new Diffuser(-1, -1, "NORTH"),
          new Alien(-1, -1, "NORTH"),
          new OxygenTank(-1, -1, "NORTH"),
          new VendingMachine(-1, -1, "NORTH")
      ));

    }

  }

  /**
   * This method tests the game is created correctly.
   */
  @Test
  public void gameCreatedCorrectly() {

    assertNotNull(game.getBoard());
    assertNotNull(game.getPlayer());
    assertNotNull(game.getCurrentRoom());

  }

  /**
   * This method tests the player can change directions.
   */
  @Test
  public void playerCanChangeDirection() {

    AccessibleTile startTile = player.getTile();
    Direction startDirection = player.getDirection();

    game.movePlayer(1, 0);
    AccessibleTile nextTile = player.getTile();

    assertEquals(startTile, nextTile);
    assertNotEquals(startDirection, player.getDirection());

  }

  /**
   * This method tests the player can move.
   */
  @Test
  public void playerCanMove() {

    AccessibleTile startTile = player.getTile();

    game.movePlayer(1, 0);
    game.movePlayer(1, 0);

    // edge cases

    game.pickUpItem();
    game.dropItem();

    AccessibleTile nextTile = player.getTile();

    assertNotEquals(startTile, nextTile);
    assertEquals(nextTile, game.getCurrentRoom().getTile(6, 5));
    assertTrue(nextTile.hasPlayer());

  }

  /**
   * This method tests the player can teleport.
   */
  @Test
  public void playerCanTeleport() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5, 9);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    game.teleport();

    assertNotEquals(startRoom, game.getCurrentRoom());

  }

  /**
   * This method tests the player cannot teleport without portal.
   */
  @Test
  public void playerCannotTeleportWithoutPortal() {

    AccessibleTile startTile = player.getTile();

    game.teleport();

    assertEquals(startTile, player.getTile());

  }

  /**
   * This method tests the player cannot teleport without an adjacent room.
   */
  @Test
  public void playerCannotTeleportWithoutNextRoom() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(9, 5);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    game.teleport();

    assertEquals(startRoom, game.getCurrentRoom());

  }

  /**
   * This method tests the player cannot teleport without a destination portal.
   */
  @Test
  public void playerCannotTeleportWithoutDestinationPortal() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5, 9);
    player.getTile().setPlayer(false);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    Portal portal = board[0][1].getDestinationPortal(Direction.WEST);
    board[0][1].removePortal(portal);

    game.teleport();

    assertEquals(startRoom, game.getCurrentRoom());

  }

  /**
   * This method tests the player can pick up an item.
   */
  @Test
  public void playerCanPickupItem() {

    AccessibleTile startTile = player.getTile();
    startTile.setItem(new BoltCutter(startTile.getRow(), startTile.getCol(), "NORTH"));

    game.pickUpItem();

    assertTrue(player.hasItem());
    assertNotNull(player.getItem());
    assertFalse(startTile.hasItem());

  }

  /**
   * This method tests the player can only pick up one item at a time.
   */
  @Test
  public void playerCanOnlyHaveOneItemAtATime() {

    AccessibleTile startTile = player.getTile();
    startTile.setItem(new BoltCutter(startTile.getRow(), startTile.getCol(), "NORTH"));
    player.addItem(new Diffuser(-1, -1, "NORTH"));

    game.pickUpItem();

    assertTrue(player.hasItem());
    assertNotNull(player.getItem());
    assertTrue(startTile.hasItem());

  }

  /**
   * This method tests the player can drop an item.
   */
  @Test
  public void playerCanDropItem() {

    AccessibleTile startTile = player.getTile();
    Item item = new Diffuser(-1, -1, "NORTH");
    player.addItem(item);

    assertFalse(startTile.hasItem());
    assertFalse(player.hasSpecificItem("Coin"));

    game.dropItem();

    assertTrue(startTile.hasItem());
    assertFalse(player.hasItem());
    assertFalse(player.hasSpecificItem("Diffuser"));
    assertNull(player.getItem());
    assertEquals(player.getTile().getRow(), item.getRow());
    assertEquals(player.getTile().getCol(), item.getCol());

  }

  /**
   * This method tests the player cannot drop an item on a portal.
   */
  @Test
  public void playerCannotDropItemOnPortal() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5, 9);
    player.getTile().setPlayer(false);
    player.setTile(startTile);
    startTile.setPlayer(true);

    player.addItem(new Diffuser(-1, -1, "NORTH"));

    game.dropItem();

    assertTrue(player.hasItem());
    assertNotNull(player.getItem());
    assertFalse(startTile.hasItem());

  }

  /**
   * This method tests the player can diffuse a bomb.
   */
  @Test
  public void playerCanDiffuseBomb() {

    game.directTeleport(board[0][1], 7, 5);
    player.addItem(new Diffuser(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[0][1].getTile(8, 5);
    Bomb bomb = (Bomb) tile.getChallenge();
    bomb.setDirection(Direction.NORTH);
    player.setDirection(Direction.SOUTH);

    assertFalse(tile.checkNavigable());

    game.diffuseBomb();

    assertTrue(bomb.isNavigable());

  }

  /**
   * This method tests the player can unlock a vending machine.
   */
  @Test
  public void playerCanUnlockVendingMachine() {

    // best case scenario

    game.directTeleport(board[1][2], 7, 8);
    player.addItem(new BoltCutter(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[1][2].getTile(8, 8);
    VendingMachine vendingMachine = (VendingMachine) tile.getChallenge();
    vendingMachine.setDirection(Direction.NORTH);
    vendingMachine.setUnlocked(false);
    player.setDirection(Direction.SOUTH);

    game.unlockVendingMachine();
    assertTrue(vendingMachine.isUnlocked());

    // challenge = null case

    vendingMachine.setDirection(Direction.WEST);
    vendingMachine.setUnlocked(false);

    game.unlockVendingMachine();
    assertFalse(vendingMachine.isUnlocked());

  }

  /**
   * This method tests the player can use a vending machine.
   */
  @Test
  public void playerCanUseVendingMachine() {

    game.directTeleport(board[1][2], 7, 8);
    player.addItem(new Coin(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[1][2].getTile(8, 8);
    VendingMachine vendingMachine = (VendingMachine) tile.getChallenge();
    vendingMachine.setDirection(Direction.NORTH);
    vendingMachine.setUnlocked(true);
    player.setDirection(Direction.SOUTH);

    game.useVendingMachine();

    // extra test case for edge case

    game.befriendAlien();

    assertFalse(player.getItem() instanceof Coin);
    assertTrue(player.getItem() instanceof Potion);

    vendingMachine.setDirection(Direction.WEST);

    player.dropItem();
    player.addItem(new Coin(-1, -1, "EAST"));

    game.useVendingMachine();

    assertTrue(player.getItem() instanceof Coin);
    assertFalse(player.getItem() instanceof Potion);

  }

  /**
   * This method tests the player can befriend an alien.
   */
  @Test
  public void playerCanBefriendAlien() {

    // best case scenario

    game.directTeleport(board[2][1], 5, 2);
    player.addItem(new Potion(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[2][1].getTile(5, 1);
    Alien alien = (Alien) tile.getChallenge();
    alien.setDirection(Direction.WEST);
    player.setDirection(Direction.WEST);

    game.befriendAlien();

    // extra test cases for edge cases

    game.diffuseBomb();
    game.unlockVendingMachine();
    game.useVendingMachine();

    assertTrue(alien.isNavigable());
    assertFalse(player.getItem() instanceof Potion);

    // challenge = null case

    player.setDirection(Direction.EAST);
    player.addItem(new Potion(-1, -1, "NORTH"));

    game.befriendAlien();
    assertTrue(player.getItem() instanceof Potion);

    // alien changes direction case

    game.directTeleport(board[2][1], 4, 1);
    player.setDirection(Direction.SOUTH);
    alien.setDirection(Direction.EAST);
    alien.setNavigable(false);

    game.befriendAlien();
    assertFalse(player.getItem() instanceof Potion);

  }

  /**
   * This method tests player must be adjacent to a challenge.
   */
  @Test
  public void challengesAreNotCompletedWithoutAnAdjacentChallengeItem() {

    Game game = this.game;

    game.diffuseBomb();
    game.unlockVendingMachine();
    game.useVendingMachine();
    game.befriendAlien();

    assertSame(game, this.game);

  }

  /**
   * This method tests player can use oxygen tank.
   */
  @Test
  public void playerCanUseOxygenTank() {

    game.directTeleport(board[1][1], 3, 6);
    AccessibleTile tile = (AccessibleTile) board[1][1].getTile(4, 6);

    player.setDirection(Direction.SOUTH);
    game.movePlayer(1, 0);

    assertEquals(tile, player.getTile());

    game.checkForOxygenTank();

    assertEquals(100, player.getOxygen());

  }

  /**
   * This method tests game wins when the spaceship is found.
   */
  @Test
  public void playerWinsByFindingSpaceShip() {

    game.directTeleport(board[2][0], 2, 5);

    assertFalse(game.checkForSpaceship());

    player.setDirection(Direction.NORTH);
    game.movePlayer(-1, 0);

    assertTrue(game.checkForSpaceship());

  }

  /**
   * This method tests a room can be rotated clockwise.
   */
  @Test
  public void roomCanBeRotatedClockwise() {

    AccessibleTile tile = player.getTile();
    tile.setChallenge(new Bomb(-1, -1, "NORTH"));

    player.setDirection(Direction.NORTH);
    Direction direction = player.getDirection();

    game.rotateRoomClockwise();

    assertNotEquals(direction, player.getDirection());
    assertEquals(Direction.EAST, player.getDirection());

  }

  /**
   * This method tests a room can be rotated anticlockwise.
   */
  @Test
  public void roomCanBeRotatedAnticlockwise() {

    AccessibleTile tile = player.getTile();
    tile.setChallenge(new Bomb(-1, -1, "NORTH"));

    player.setDirection(Direction.NORTH);
    Direction direction = player.getDirection();

    game.rotateRoomAnticlockwise();

    assertNotEquals(direction, player.getDirection());
    assertEquals(Direction.WEST, player.getDirection());

  }

  /**
   * This method tests a room can create a test room.
   */
  @Test
  public void canCreateTestRoom() {

    Room room = new Room(0, 0);
    Tile tile = new AccessibleTile(-1, -1);

    assertNotNull(room);
    assertTrue(room.getTile(5, 5) instanceof AccessibleTile);
    assertNull(room.getTileCoordinates(tile));
    assertEquals(0, room.getRow());
    assertEquals(0, room.getCol());

  }

  /**
   * This method tests a player stays within room boundaries.
   */
  @Test
  public void playerCannotMoveOutsideRoomBounds() {

    game.directTeleport(board[1][1], 0, 5);
    AccessibleTile startTile = player.getTile();
    Room room = game.getCurrentRoom();

    room.findTile(player.getTile(), Direction.NORTH);

    assertEquals(room, game.getCurrentRoom());

    Tile tileA = room.findNextTile(startTile, -1, 0);
    Tile tileB = room.findNextTile(startTile, 0, 1);

    assertNull(tileA);
    assertNull(tileB);
    assertEquals(startTile, player.getTile());

  }

  /**
   * This method tests player's oxygen stays within bounds.
   */
  @Test
  public void playerOxygenCannotExceedBounds() {

    player.setOxygen(150);
    player.loseOxygen();
    player.boostOxygen();

    assertTrue(player.getOxygen() <= 100);

    player.setOxygen(0);
    player.loseOxygen();

    assertFalse(player.getOxygen() < 0);

  }

  /**
   * This method tests a game can be connected with a renderer.
   */
  @Test
  public void gameCanBeConnectedWithRenderer() {

    Renderer renderer = new Renderer(game);
    assertNotNull(renderer);

  }

  /**
   * This method tests directions are implemented correctly.
   */
  @Test
  public void directionEnumReturnsCorrectValue() {

    // opposite direction

    assertEquals(Direction.SOUTH, Direction.NORTH.getOppositeDirection());
    assertEquals(Direction.NORTH, Direction.SOUTH.getOppositeDirection());
    assertEquals(Direction.WEST, Direction.EAST.getOppositeDirection());
    assertEquals(Direction.EAST, Direction.WEST.getOppositeDirection());

    // clockwise direction


    assertEquals(Direction.EAST, Direction.NORTH.getClockwiseDirection());
    assertEquals(Direction.WEST, Direction.SOUTH.getClockwiseDirection());
    assertEquals(Direction.SOUTH, Direction.EAST.getClockwiseDirection());
    assertEquals(Direction.NORTH, Direction.WEST.getClockwiseDirection());

    // anticlockwise direction

    assertEquals(Direction.WEST, Direction.NORTH.getAnticlockwiseDirection());
    assertEquals(Direction.EAST, Direction.SOUTH.getAnticlockwiseDirection());
    assertEquals(Direction.NORTH, Direction.EAST.getAnticlockwiseDirection());
    assertEquals(Direction.SOUTH, Direction.WEST.getAnticlockwiseDirection());

    // next direction

    assertEquals(Direction.NORTH, Direction.NORTH.nextDirection(-1, 0));
    assertEquals(Direction.SOUTH, Direction.NORTH.nextDirection(1, 0));
    assertEquals(Direction.WEST, Direction.NORTH.nextDirection(0, -1));
    assertEquals(Direction.EAST, Direction.NORTH.nextDirection(0, 1));

    // direction from string

    assertEquals(Direction.NORTH, Direction.directionFromString("NORTH"));
    assertEquals(Direction.SOUTH, Direction.directionFromString("SOUTH"));
    assertEquals(Direction.EAST, Direction.directionFromString("EAST"));
    assertEquals(Direction.WEST, Direction.directionFromString("WEST"));

  }

  /**
   * This method tests items implement getName(), getDescription()
   * and toString().
   */
  @Test
  public void itemNamesAndDescriptionsAreImplemented() {

    for (Item item : items) {
      assertNotNull(item.getName());
      assertNotNull(item.getDescription());
      assertNotNull(item.toString());

    }

    assertNotNull(new Portal(-1, -1, null, Direction.NORTH).toString());

  }

}
