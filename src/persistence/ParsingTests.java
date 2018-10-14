package persistence;

import gameworld.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import persistence.XMLParser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Using JUnit 5
 */
public class ParsingTests {

  @Test
  public void testReadGame() throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse("data/easy.xml");

    NodeList children = doc.getChildNodes();
    Element gameElement = (Element) children.item(0);
    assertEquals(gameElement.getTagName(), "game");
  }

  @Test
  public void badXMLTest() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/badXMLFile.xml")));
  }


  private Room[][] board;
  private Player player;
  private Game game;
  @Test
  public void testSaveFile(){

      board = new Room[3][3];
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          board[i][j] = new Room();
        }
      }
      player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(5,5), 100, "NORTH");

      ((AccessibleTile) board[0][0].getTile(6, 5)).setItem(new Diffuser(6, 5));

      game = new Game(board, player);

      XMLParser.saveFile(new File("data/testSave.xml"), game);

  }

  //test no properties specified (no row/col/health)
  //test too many rooms (> 9)
  //test not enough doors (< 1)
  //test too many players




}
