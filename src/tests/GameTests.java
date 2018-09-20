package tests;

import gameworld.Game;
import gameworld.Player;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GameTests {

  @Test
  public void createsNewGameCorrectly() {
    Game game = new Game(true);
    Player player = game.getPlayer();
    assertNotNull(player);
    assertTrue(player.getInventory().isEmpty());
  }

}

