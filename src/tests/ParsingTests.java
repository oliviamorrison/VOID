package tests;

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
    Document doc = dBuilder.parse("data/gameworld.xml");

    NodeList children = doc.getChildNodes();
    Element gameElement = (Element) children.item(0);
    assertEquals(gameElement.getTagName(), "game");
  }

  @Test
  public void badXMLTest() {
    assertThrows(XMLParser.ParseError.class,
        ()-> XMLParser.parseGame(new File("data/badXMLFile.xml")));
  }

  //test no properties specified (no row/col/health)
  //test too many rooms (> 9)
  //test not enough doors (< 1)




}
