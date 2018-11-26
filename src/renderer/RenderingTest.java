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

/**
 * This class tests all of the Rendering as well as the PolygonBlock logic.
 * Note some test are done in gameworld test.
 *
 * @author James Del Puerto 300375073
 */
public class RenderingTest {

  private Game game;
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
      player = game.getPlayer();
      renderer = new Renderer(game);
    }

  }

  /**
   * Test the getObjectDirection() method. Making sure that it returns the right directions
   * when called.
   */
  @Test void getObjectDirectionTest() {
    assertEquals("N.png", renderer.getObjectDirection(Direction.NORTH));
    assertEquals("S.png", renderer.getObjectDirection(Direction.SOUTH));
    assertEquals("E.png", renderer.getObjectDirection(Direction.WEST));
    assertEquals("W.png", renderer.getObjectDirection(Direction.EAST));
  }

  /**
   * Test the getRoot() method. Making sure it's not empty.
   */
  @Test
  public void getRootTest() {
    assertNotNull(renderer.getRoot());
  }

  /**
   * Test the newRoom() method. Test that renderer knows
   * the player is in a new room and not in the same room when player moves.
   */
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

  /**
   * Test that the player rotates when player rotates board or character; checking for
   * each direction.
   */
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

