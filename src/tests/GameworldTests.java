package tests;

import gameworld.AccessibleTile;
import gameworld.Game;
import gameworld.Player;
import gameworld.Room;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameworldTests {

  private Room[][] board;
  private Player player;
  private Game game;


  @Before
  public void setUp() {
    board = new Room[3][3];
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        board[i][j] = new Room();
      }
    }
    player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(5,5));

    game = new Game(board, player);

  }
  @Test
  public void movePlayerTest(){
    //player starts at 5,5
    assertEquals(player.getTile().getX(), 5);
    assertEquals(player.getTile().getY(), 5);

    //move player up
    game.movePlayer("w");

    //player should be at 4,5
    assertEquals(player.getTile().getX(), 4);
    assertEquals(player.getTile().getY(), 5);

  }




}

