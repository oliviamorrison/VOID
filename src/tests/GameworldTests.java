package tests;

import gameworld.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(5,5), 100, "NORTH");

    ((AccessibleTile) board[0][0].getTile(6,5)).setItem(new Diffuser(6, 5));

    game = new Game(board, player);

  }
  @Test
  public void movePlayerTest(){

  }

  @Test
  public void pickUpItem(){

  }





}

