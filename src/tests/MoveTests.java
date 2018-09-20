package tests;

import gameworld.Game;
import gameworld.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Annisha Akosah 300399598
 */
public class MoveTests {

  @Test
  public void playerMovesCorrectly() {

    String room = "X X X X X X X X X X\n"
        + "X                 X\n"
        + "X                 X\n"
        + "X                 X\n"
        + "X     P           X\n"
        + "X                 X\n"
        + "X           D     X\n"
        + "X                 X\n"
        + "X                 X\n"
        + "X X X X X X X X X X\n";

    Game game = new Game(true);
    Player player = game.getPlayer();
    player.moveTile(2, 1);
    assertEquals(game.drawRoom(), room);

  }

}

