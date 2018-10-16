package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    game = XmlParser.parseGame(new File("test_data/gameworldTestData.xml"));

    if (game != null) {
      board = game.getBoard();
      player = game.getPlayer();
      renderer = new Renderer(game);
    }

  }

  @Test void getObjectDirectionTest() {
    assertEquals("N.png", renderer.getObjectDirection(Direction.NORTH));
    assertEquals("S.png", renderer.getObjectDirection(Direction.SOUTH));
    assertEquals("W.png", renderer.getObjectDirection(Direction.WEST));
    assertEquals("E.png", renderer.getObjectDirection(Direction.EAST));
  }

}

