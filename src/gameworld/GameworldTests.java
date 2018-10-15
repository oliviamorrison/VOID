package gameworld;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.XMLParser;

import static org.junit.jupiter.api.Assertions.*;


public class GameworldTests {

  private Game game;
  private Room[][] board;
  private Player player;

  @BeforeEach
  public void setUp() {

    try {
      game = XMLParser.parseGame(new File("data/gameworldTestData.xml"));
    } catch (XMLParser.ParseError parseError) {
      parseError.printStackTrace();
    }

    if (game != null) {

      board = game.getBoard();
      player = game.getPlayer();

    }

  }

  @Test
  public void gameCreatedCorrectly() {

    assertNotNull(game.getBoard());
    assertNotNull(game.getPlayer());
    assertNotNull(game.getCurrentRoom());

  }

  @Test
  public void playerCanChangeDirection() {

    AccessibleTile startTile = player.getTile();
    Direction startDirection = player.getDirection();

    game.movePlayer(1, 0);
    AccessibleTile nextTile = player.getTile();

    assertEquals(startTile, nextTile);
    assertNotEquals(startDirection, player.getDirection());

  }

  @Test
  public void playerCanMove() {

    AccessibleTile startTile = player.getTile();

    game.movePlayer(1, 0);
    game.movePlayer(1, 0);

    AccessibleTile nextTile = player.getTile();

    assertNotEquals(startTile, nextTile);
    assertEquals(nextTile, game.getCurrentRoom().getTile(6, 5));

  }

  @Test
  public void playerCanTeleport() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5,9);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    game.teleport();

    assertNotEquals(startRoom, game.getCurrentRoom());

  }

  @Test
  public void playerCannotTeleportWithoutPortal() {

    AccessibleTile startTile = player.getTile();

    game.teleport();

    assertEquals(startTile, player.getTile());

  }

  @Test
  public void playerCannotTeleportWithoutNextRoom() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(9,5);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    game.teleport();

    assertEquals(startRoom, game.getCurrentRoom());

  }

  @Test
  public void playerCannotTeleportWithoutDestinationPortal() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5,9);
    player.getTile().setPlayer(false);
    player.setTile(startTile);
    startTile.setPlayer(true);

    Room startRoom = game.getCurrentRoom();

    Portal portal = board[0][1].getDestinationPortal(Direction.WEST);
    board[0][1].removePortal(portal);

    game.teleport();

    assertEquals(startRoom, game.getCurrentRoom());

  }

  @Test
  public void playerCanPickupItem() {

    AccessibleTile startTile = player.getTile();
    startTile.setItem(new BoltCutter(startTile.getRow(), startTile.getCol(), "NORTH"));

    game.pickUpItem();

    assertTrue(player.hasItem());
    assertNotNull(player.getItem());
    assertFalse(startTile.hasItem());

  }

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

  @Test
  public void playerCanDropItem() {

    AccessibleTile startTile = player.getTile();
    player.addItem(new Diffuser(-1, -1, "NORTH"));

    game.dropItem();

    assertFalse(player.hasItem());
    assertNull(player.getItem());
    assertTrue(startTile.hasItem());

  }

  @Test
  public void playerCannotDropItemOnPortal() {

    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5,9);
    player.getTile().setPlayer(false);
    player.setTile(startTile);
    startTile.setPlayer(true);

    player.addItem(new Diffuser(-1, -1, "NORTH"));

    game.dropItem();

    assertTrue(player.hasItem());
    assertNotNull(player.getItem());
    assertFalse(startTile.hasItem());

  }

  @Test
  public void playerCanDiffuseBomb() {

    game.teleport(board[0][1], 7, 5);
    player.addItem(new Diffuser(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[0][1].getTile(8, 5);
    Bomb bomb = (Bomb) tile.getChallenge();
    bomb.setDirection(Direction.NORTH);
    player.setDirection(Direction.SOUTH);

    game.diffuseBomb();

    assertTrue(bomb.isNavigable());

  }

  @Test
  public void playerCanUnlockVendingMachine() {

    game.teleport(board[1][2], 8, 7);
    player.addItem(new BoltCutter(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[1][2].getTile(8, 8);
    VendingMachine vendingMachine = (VendingMachine) tile.getChallenge();
    vendingMachine.setDirection(Direction.WEST);
    player.setDirection(Direction.EAST);

    game.unlockVendingMachine();

    assertTrue(vendingMachine.isUnlocked());

    vendingMachine.setDirection(Direction.NORTH);
    vendingMachine.setUnlocked(false);

    game.unlockVendingMachine();

    assertFalse(vendingMachine.isUnlocked());

  }

  @Test
  public void playerCanUseVendingMachine() {

    game.teleport(board[1][2], 8, 7);
    player.addItem(new Coin(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[1][2].getTile(8, 8);
    VendingMachine vendingMachine = (VendingMachine) tile.getChallenge();
    vendingMachine.setDirection(Direction.WEST);
    vendingMachine.setUnlocked(true);
    player.setDirection(Direction.EAST);

    game.useVendingMachine();

    assertFalse(player.getItem() instanceof Coin);
    assertTrue(player.getItem() instanceof Beer);

    vendingMachine.setDirection(Direction.NORTH);

    player.dropItem();
    player.addItem(new Coin(-1, -1, "NORTH"));

    game.useVendingMachine();

    assertTrue(player.getItem() instanceof Coin);
    assertFalse(player.getItem() instanceof Beer);

  }

  @Test
  public void playerCanBribeGuard() {

    game.teleport(board[2][1], 5, 2);
    player.addItem(new Beer(-1, -1, "NORTH"));

    AccessibleTile tile = (AccessibleTile) board[2][1].getTile(5, 1);
    Guard guard = (Guard) tile.getChallenge();
    guard.setDirection(Direction.EAST);
    player.setDirection(Direction.WEST);

    game.bribeGuard();

    assertTrue(guard.isNavigable());
    assertFalse(player.getItem() instanceof Beer);

    guard.setDirection(Direction.NORTH);
    guard.setNavigable(false);
    player.addItem(new Beer(-1, -1, "NORTH"));

    game.bribeGuard();

    assertTrue(player.getItem() instanceof Beer);

  }

  @Test
  public void challengesAreNotCompletedWithoutAnAdjacentChallengeItem() {

    Game game = this.game;

    game.diffuseBomb();
    game.unlockVendingMachine();
    game.useVendingMachine();
    game.bribeGuard();

    assertSame(game, this.game);

  }

  @Test
  public void playerCanUseHealthPack() {

    game.teleport(board[1][1], 3, 6);
    AccessibleTile tile = (AccessibleTile) board[1][1].getTile(4, 6);

    player.setDirection(Direction.SOUTH);
    game.movePlayer(1, 0);

    game.checkForHealthPack();

    assertEquals(tile, player.getTile());
    assertEquals(100, player.getHealth());

  }

  @Test
  public void playerWinsByFindingAntidote() {

    game.teleport(board[2][0], 2, 5);

    assertFalse(game.checkForAntidote());

    player.setDirection(Direction.NORTH);
    game.movePlayer(-1, 0);

    assertTrue(game.checkForAntidote());

  }

  @Test
  public void roomCanBeRotatedClockwise() {

    player.setDirection(Direction.NORTH);
    Direction direction = player.getDirection();

    game.rotateRoomClockwise();

    assertNotEquals(direction, player.getDirection());
    assertEquals(Direction.EAST, player.getDirection());

  }

  @Test
  public void roomCanBeRotatedAnticlockwise() {

    player.setDirection(Direction.NORTH);
    Direction direction = player.getDirection();

    game.rotateRoomAnticlockwise();

    assertNotEquals(direction, player.getDirection());
    assertEquals(Direction.WEST, player.getDirection());

  }

}
