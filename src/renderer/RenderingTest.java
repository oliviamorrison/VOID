package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gameworld.AccessibleTile;
import gameworld.Direction;
import gameworld.Game;
import gameworld.Player;
import gameworld.Room;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import persistence.XmlParser;


public class RenderingTest {

  private Game game;
  private Room[][] board;
  private Player player;
  private Renderer renderer;

  /**
   * This method sets up a test scenario to ease testing.
   *
   * @throws XmlParser.ParseError xml parsing error
   */
  @BeforeEach
  public void setUp() throws XmlParser.ParseError {

    game = XmlParser.parseGame(new File("test_data/renderTesting2.xml"));

    if (game != null) {
      board = game.getBoard();
      player = game.getPlayer();
      renderer = new Renderer(game);
    }

  }

  @Test void getObjectDirectionTest() {
    assertEquals("N.png", renderer.getObjectDirection(Direction.NORTH));
    assertEquals("S.png", renderer.getObjectDirection(Direction.SOUTH));
    assertEquals("E.png", renderer.getObjectDirection(Direction.WEST));
    assertEquals("W.png", renderer.getObjectDirection(Direction.EAST));
  }

  @Test
  public void getRootTest() {
    assertNotNull(renderer.getRoot());
  }

  @Test
  public void newRoomTest() {
    AccessibleTile startTile = (AccessibleTile) game.getCurrentRoom().getTile(5, 9);
    player.setTile(startTile);
    startTile.setPlayer(true);
    Room startRoom = game.getCurrentRoom();
    renderer.newRoom();
    game.teleport();
    assertNotEquals(startRoom, player.getRoom());
  }

  @Test
  public void rotationTest() {
    Direction startDirection = player.getDirection();
    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    renderer.draw();
    assertNotEquals(startDirection, player.getDirection());

    startDirection = player.getDirection();
    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    renderer.draw();
    assertNotEquals(startDirection, player.getDirection());

    startDirection = player.getDirection();
    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    renderer.draw();
    assertNotEquals(startDirection, player.getDirection());

    startDirection = player.getDirection();
    player.setDirection(player.getDirection().getAnticlockwiseDirection());
    renderer.draw();
    assertNotEquals(startDirection, player.getDirection());
  }

}

