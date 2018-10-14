package persistence;

import gameworld.*;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Using JUnit 5
 */
public class ParsingTests {

  @Test
  public void testReadGame() throws XMLParser.ParseError {

    Game game = XMLParser.parseGame(new File("data/parserTestData.xml"));
    assertNotNull(game);
  }

  @Test
  public void testIncorrectSchema() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/incorrectSchema.xml")));
  }

  @Test
  public void testIncorrectItemName() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/incorrectItemName.xml")));
  }


  private Room[][] board;
  private Player player;
  private Game game;
  @Test
  public void testSaveFile() throws TransformerException, ParserConfigurationException {

    board = new Room[3][3];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = new Room();
      }
    }
    player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(5,5), 100, "NORTH");
    board[0][0].setTile(new Portal(5, 9,board[0][1], Direction.NORTH), 5, 9);
    ((AccessibleTile) board[0][0].getTile(6, 5)).setItem(new Diffuser(6, 5));
    ((AccessibleTile) board[0][0].getTile(4, 7)).setChallenge(new VendingMachine(4,7));
    ((AccessibleTile) board[0][0].getTile(2, 2)).setChallenge(new Guard(2,2));
    ((AccessibleTile) board[0][0].getTile(6, 3)).setChallenge(new Bomb(6,3));

    player.addItem(new Diffuser(-1,-1));

    game = new Game(board, player);

    File testFile = new File("data/testSave.xml");
    XMLParser.saveFile(new File("data/testSave.xml"), game);
    assertTrue(testFile.length() > 0);
  }


}
