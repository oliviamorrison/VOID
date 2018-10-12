package tests;

import gameworld.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameworldTests {

  private Room[][] board;
  private Player player;
  private Game game;


  @BeforeEach
  public void setUp() {
    board = new Room[3][3];
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        board[i][j] = new Room();
      }
    }
    player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(5,5), 100, Direction.Top);

    ((AccessibleTile) board[0][0].getTile(6,5)).setItem(Item.Diffuser);

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
    assertEquals(player.getTile().getX(), 4); //TODO: is this right ??? It's moving the X coord not the Y coord
    assertEquals(player.getTile().getY(), 5);

    //move player right
    game.movePlayer("d");

    //player should be at 4,6
    assertEquals(player.getTile().getX(), 4);
    assertEquals(player.getTile().getY(), 6);
  }

  @Test
  public void pickUpItem(){
    game.movePlayer("s");

    game.pickUpItem();
    assertEquals(player.getInventory().get(0), Item.Diffuser);
  }





}

