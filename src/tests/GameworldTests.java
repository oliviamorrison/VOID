//package tests;
//
//import gameworld.AccessibleTile;
//import gameworld.Game;
//import gameworld.Player;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class GameworldTests {
//
//  @Test
//  public void createsNewGameCorrectly() {
//
//    String room = "X X X X X X X X X X\n"
//        + "X                 X\n"
//        + "X   P             X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X           D     X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X X X X X X X X X X\n";
//
//    Game game = new Game(true);
//    Player player = game.getPlayer();
//
//    assertNotNull(player);
//    assertTrue(player.getInventory().isEmpty());
//    assertEquals(room, game.drawRoom());
//
//  }
//
//  @Test
//  public void playerCanMoveWithinARoom() {
//
//    String room = "X X X X X X X X X X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X     P           X\n"
//        + "X                 X\n"
//        + "X           D     X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X X X X X X X X X X\n";
//
//    Game game = new Game(true);
//    Player player = game.getPlayer();
//    player.moveTile(2, 1);
//    assertEquals(room, game.drawRoom());
//
//  }
//
//  @Test
//  public void rendersPlayerOnTokenTileCorrectly() {
//
//    String room = "X X X X X X X X X X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X           !     X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X X X X X X X X X X\n";
//
//    Game game = new Game(true);
//    Player player = game.getPlayer();
//
//    player.moveTile(4, 4);
//    AccessibleTile tile = (AccessibleTile) player.getTile();
//
//    assertTrue(tile.hasToken());
//    assertEquals(room, game.drawRoom());
//
//  }
//
//  @Test
//  public void playerCanPickupTokens() {
//
//    String room = "X X X X X X X X X X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X           P     X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X X X X X X X X X X\n";
//
//    Game game = new Game(true);
//    Player player = game.getPlayer();
//
//    player.moveTile(2, 2);
//    game.pickUpItem();
//
//    AccessibleTile tile = (AccessibleTile) player.getTile();
//    player.moveTile(2, 2);
//    game.pickUpItem();
//
//    assertFalse(tile.hasToken());
//    assertEquals(1, player.getInventory().size());
//    assertEquals(room, game.drawRoom());
//
//  }
//
//  @Test
//  public void playerCanDropTokens() {
//
//    String room = "X X X X X X X X X X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X       P D       X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X                 X\n"
//        + "X X X X X X X X X X\n";
//
//    Game game = new Game(true);
//    Player player = game.getPlayer();
//
//    player.moveTile(2, 2);
//    game.dropItem();
//
//    AccessibleTile firstTile = (AccessibleTile) player.getTile();
//    player.moveTile(2, 2);
//    game.pickUpItem();
//    assertFalse(firstTile.hasToken());
//
//    player.moveTile(-1, -1);
//
//    AccessibleTile secondTile = (AccessibleTile) player.getTile();
//    game.dropItem();
//    assertTrue(player.getInventory().isEmpty());
//    assertTrue(secondTile.hasToken());
//
//    player.moveTile(0, -1);
//
//    assertEquals(room, game.drawRoom());
//
//  }
//
//}
//
