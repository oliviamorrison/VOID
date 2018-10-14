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
  public void playerCannotTeleportWithoutPortal() {

    AccessibleTile startTile = player.getTile();

    game.teleport();

    assertEquals(startTile, player.getTile());

  }


}
