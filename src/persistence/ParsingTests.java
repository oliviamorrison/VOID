package persistence;

import gameworld.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests use JUnit 5 to test the persistence package of the game
 *
 * @author Sam Ong 300363819
 */
public class ParsingTests {

  /**
   * Test reading a valid XML file into a game
   * @throws XMLParser.ParseError if it is an invalid file
   */
  @Test
  public void testReadGame() throws XMLParser.ParseError {
    Game game = XMLParser.parseGame(new File("data/parserTestData.xml"));
    assertNotNull(game);
  }

  /**
   * Test the parser throws an XMLParser error when an invalid file is loaded
   */
  @Test
  public void testIncorrectSchema() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/incorrectSchema.xml")));
  }

  /**
   * Test the parser throws an XMLParser error when a file with incorrect item names are loaded
   */
  @Test
  public void testIncorrectItemName() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/incorrectItemName.xml")));
  }

  /**
   * Test the parser throws an XMLParser error when a file with incorrect challenge names are loaded
   */
  @Test
  public void testIncorrectChallengeName() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/incorrectChallengeName.xml")));
  }

  /**
   * Test the parser saves an XML file from a game that starts with a root node <game></game>
   */
  @Test
  public void testSaveFile() throws TransformerException, ParserConfigurationException, IOException, SAXException {
    //Hard coding a room to test saving
    Room[][] board = new Room[3][3];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = new Room(i,j);
      }
    }

    Player player = new Player(board[2][2], (AccessibleTile) board[2][2].getTile(5, 5), 100, "NORTH");
    board[0][0].setTile(new Portal(5, 9, board[0][1], Direction.NORTH), 5, 9);
    ((AccessibleTile) board[0][0].getTile(6, 5)).setItem(new Diffuser(6, 5, "NORTH"));
    ((AccessibleTile) board[0][1].getTile(4, 7)).setChallenge(new VendingMachine(4,7, "NORTH"));
    ((AccessibleTile) board[1][0].getTile(2, 2)).setChallenge(new Alien(2,2, "NORTH"));
    ((AccessibleTile) board[2][0].getTile(6, 3)).setChallenge(new Bomb(6,3, "NORTH"));

    player.addItem(new Diffuser(-1,-1, "NORTH"));

    Game game = new Game(board, player);

    File testFile = new File("data/testSave.xml");
    XMLParser.saveFile(new File("data/testSave.xml"), game);
    //Check the file has been written
    assertTrue(testFile.length() > 0);

    //Check the first element in the file is
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse("data/testSave.xml");
    NodeList children = doc.getChildNodes();
    Element gameElement = (Element) children.item(0);
    assertEquals(gameElement.getTagName(), "game");
  }
}
