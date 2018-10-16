package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gameworld.AccessibleTile;
import gameworld.Alien;
import gameworld.Bomb;
import gameworld.Diffuser;
import gameworld.Direction;
import gameworld.Game;
import gameworld.Player;
import gameworld.Portal;
import gameworld.Room;
import gameworld.VendingMachine;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * These tests use JUnit 5 to test the persistence package of the game.
 *
 * @author Sam Ong 300363819
 */
public class ParsingTests {

  /**
   * Test reading a valid XML file into a game.
   * @throws XmlParser.ParseError if it is an invalid file
   */
  @Test
  public void testReadValidFile() throws XmlParser.ParseError {
    Game game = XmlParser.parseGame(new File("test_data/parserTestData.xml"));
    assertNotNull(game);
  }

  /**
   * Test the parser saves an XML file from a game that starts with a root node <game></game>.
   *
   * @throws TransformerException exception thrown if transformer fails
   * @throws ParserConfigurationException exception thrown if parser fails
   * @throws IOException exception thrown if reading/writing files fails
   * @throws SAXException exception thrown if simple API for XML fails
   */
  @Test
  public void testValidSaveFile()
      throws TransformerException, ParserConfigurationException, IOException, SAXException {
    //Hard coding a room to test saving
    Room[][] board = new Room[3][3];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = new Room(i,j);
      }
    }
    board[0][0].setTile(new Portal(5, 9, board[0][1], Direction.NORTH), 5, 9);
    ((AccessibleTile) board[0][0].getTile(6, 5)).setItem(new Diffuser(6, 5, "NORTH"));
    ((AccessibleTile) board[0][1].getTile(4, 7)).setChallenge(new VendingMachine(4,7, "NORTH"));
    ((AccessibleTile) board[1][0].getTile(2, 2)).setChallenge(new Alien(2,2, "NORTH"));
    ((AccessibleTile) board[2][0].getTile(6, 3)).setChallenge(new Bomb(6,3, "NORTH"));
    AccessibleTile playerTile = (AccessibleTile) board[2][2].getTile(5, 5);
    Player player = new Player(board[2][2], playerTile, 100, "NORTH");

    player.addItem(new Diffuser(-1,-1, "NORTH"));

    Game game = new Game(board, player);

    File testFile = new File("test_data/testSave.xml");
    XmlParser.saveFile(new File("test_data/testSave.xml"), game);
    //Check the file has been written
    assertTrue(testFile.length() > 0);

    //Check the first element in the file is
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
    Document doc = dbBuilder.parse("test_data/testSave.xml");
    NodeList children = doc.getChildNodes();
    Element gameElement = (Element) children.item(0);
    assertEquals(gameElement.getTagName(), "game");
  }

  /**
   * Test the parser throws an XmlParser error when an invalid file is loaded.
   */
  @Test
  public void testIncorrectSchema() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectSchema.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with incorrect item names are loaded.
   */
  @Test
  public void testIncorrectItemName() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectItemName.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with an item.
   * that is missing its position
   */
  @Test
  public void testIncorrectItemProperty() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectItemProperty.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with incorrect challenge
   * names are loaded.
   */
  @Test
  public void testIncorrectChallengeName() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectChallengeName.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with a challenge
   * that is missing the direction property.
   */
  @Test
  public void testIncorrectChallengeProperty() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectChallengeProperty.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with a challenge
   * that is missing its position.
   */
  @Test
  public void testIncorrectChallengeProperty2() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectChallengeProperty2.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with a player
   * that is missing its position.
   */
  @Test
  public void testIncorrectPlayerProperty() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectPlayerProperty.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with a player
   * that is missing its oxygen property.
   */
  @Test
  public void testIncorrectPlayerProperty2() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectPlayerProperty2.xml")));
  }

  /**
   * Test the parser throws an XmlParser error when a file with a player
   * that is missing its direction property.
   */
  @Test
  public void testIncorrectPlayerProperty3() {
    assertThrows(XmlParser.ParseError.class,
        () -> XmlParser.parseGame(new File("test_data/incorrectPlayerProperty3.xml")));
  }

}
