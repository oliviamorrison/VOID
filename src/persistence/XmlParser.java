package persistence;

import static java.lang.Integer.parseInt;

import gameworld.AccessibleTile;
import gameworld.Alien;
import gameworld.BoltCutter;
import gameworld.Bomb;
import gameworld.ChallengeItem;
import gameworld.Coin;
import gameworld.Diffuser;
import gameworld.Game;
import gameworld.InaccessibleTile;
import gameworld.Item;
import gameworld.OxygenTank;
import gameworld.Player;
import gameworld.Portal;
import gameworld.Potion;
import gameworld.Room;
import gameworld.SpaceShip;
import gameworld.Tile;
import gameworld.VendingMachine;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class saves and loads XML files using the javax.xml.parsers library and DOM parser.
 * The schema for the game's XML files is under "schema.xsd"
 *<p>
 * This uses the Java DOM parser because:
 *  - The structure of the document is important
 *  - Elements in the document need to be sorted into specific places in collections
 *</p>
 * @author Sam Ong 300363819
 *
 */

public class XmlParser {
  private static Schema schema;

  /**
   * Static method to save a game to an XML file using DOM parser.
   * It constructs a tree of nodes under the document builder
   * @param file file to save the XML to
   * @param game game to save
   * @throws ParserConfigurationException exception thrown if parser fails
   * @throws TransformerException exception thrown is transformer fails
   */
  public static void saveFile(File file, Game game)
      throws ParserConfigurationException, TransformerException {

    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();

    Room[][] board = game.getBoard();

    //add root to XML file
    Element root = document.createElement("game");
    root.setAttribute("row", board.length + "");
    root.setAttribute("col", board[0].length + "");
    root.setAttribute("direction", game.getDirection().toString());
    root.setAttribute("initDirection", game.getInitialDirection().toString());
    document.appendChild(root);

    //add rooms to XML file
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] != null) {
          root.appendChild(saveRoom(document, i, j, board[i][j]));
        }
      }
    }

    //add player to XML file
    root.appendChild(savePlayer(game.getPlayer(), document));

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    //Add line breaks and indentation
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    //Save to XML file
    DOMSource source = new DOMSource(document);
    StreamResult result = new StreamResult(file);
    document.getDocumentElement().normalize();
    transformer.transform(source, result);
  }

  /**
   * Method to save a room and its properties to an element in the document.
   * @param document document to save to
   * @param row y position of room in board
   * @param col x position of room in board
   * @param room Room to save
   * @return the room element to append to the root
   */
  private static Element saveRoom(Document document, int row, int col, Room room) {
    Element roomElement = document.createElement("room");
    roomElement.setAttribute("row", row + "");
    roomElement.setAttribute("col", col + "");

    //Go through each tile in the room and save their properties
    for (int y = 0; y < Room.ROOMSIZE; y++) {
      for (int x = 0; x < Room.ROOMSIZE; x++) {
        Tile tile = room.getTile(x,y);
        //Save portals
        if (tile instanceof Portal) {
          Portal portal = (Portal) tile;
          Element portalElement = document.createElement("portal");
          portalElement.appendChild((document.createTextNode(portal.getDirection().toString())));
          roomElement.appendChild(portalElement);
        } else if (tile instanceof AccessibleTile) {
          AccessibleTile accessibleTile = (AccessibleTile) tile;

          //Save items
          if (accessibleTile.hasItem()) {
            Element item = document.createElement("item");
            item.setAttribute("row", accessibleTile.getItem().getRow() + "");
            item.setAttribute("col", accessibleTile.getItem().getCol() + "");
            item.setAttribute("direction", accessibleTile.getItem().getDirection().toString());
            item.appendChild(document.createTextNode(accessibleTile.getItem().toString()));
            roomElement.appendChild(item);
          }

          //Save challenges
          if (accessibleTile.hasChallenge()) {
            ChallengeItem challengeItem = accessibleTile.getChallenge();
            Element challenge = document.createElement("challenge");

            if (challengeItem instanceof Bomb) {
              Bomb bomb = (Bomb) challengeItem;
              challenge.setAttribute("state", bomb.isNavigable() + "");
            } else if (challengeItem instanceof Alien) {
              Alien alien = (Alien) challengeItem;
              challenge.setAttribute("state", alien.isNavigable() + "");
            } else {
              VendingMachine vm = (VendingMachine) challengeItem;
              challenge.setAttribute("state", vm.isUnlocked() + "");
            }
            challenge.setAttribute("row", challengeItem.getRow() + "");
            challenge.setAttribute("col", challengeItem.getCol() + "");
            challenge.setAttribute("direction", challengeItem.getDirection().toString());
            String challengeName = accessibleTile.getChallenge().toString();
            challenge.appendChild(document.createTextNode(challengeName));
            roomElement.appendChild(challenge);
          }
        }
      }
    }
    return roomElement;
  }

  /**
   * Method to save a player and its properties to an element in the document.
   * @param player player to save
   * @param document document to save element to
   * @return player element to append to the root of the document
   */
  private static Element savePlayer(Player player, Document document) {
    Element playerElement = document.createElement("player");
    //Get position, oxygen and direction of player and add as attributes to player element
    playerElement.setAttribute("row", player.getTile().getRow() + "");
    playerElement.setAttribute("col", player.getTile().getCol() + "");
    playerElement.setAttribute("oxygen", player.getOxygen() + "");
    playerElement.setAttribute("direction", player.getDirection().toString());

    //Add coordinates of the room the player is in
    Element roomRow = document.createElement("roomRow");
    roomRow.appendChild(document.createTextNode(player.getRoom().getRow() + ""));
    Element roomCol = document.createElement("roomCol");
    roomCol.appendChild(document.createTextNode(player.getRoom().getCol() + ""));
    playerElement.appendChild(roomRow);
    playerElement.appendChild(roomCol);

    //Save players item if they are holding one
    Item playerItem = player.getItem();
    if (playerItem != null) {
      Element item = document.createElement("item");
      item.setAttribute("row", playerItem.getRow() + "");
      item.setAttribute("col", playerItem.getCol() + "");
      item.setAttribute("direction", playerItem.getDirection().toString());
      item.appendChild(document.createTextNode(playerItem.toString()));
      playerElement.appendChild(item);
    }

    return playerElement;
  }

  /**
   * Method to parse an XML file into a game.
   * @param file file to parse
   * @return the game created from file
   * @throws ParseError throws a parse error if the file is invalid
   */
  public static Game parseGame(File file) throws ParseError {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dbBuilder = factory.newDocumentBuilder();

      Document doc = dbBuilder.parse(file);

      DOMSource source = new DOMSource(doc);

      //using schema.xsd to validate the schema
      loadSchema("src/persistence/schema.xsd");
      Validator validator = schema.newValidator();
      validator.validate(source);

      doc.getDocumentElement().normalize();

      //parse rows and cols
      int row = parseInt(doc.getDocumentElement().getAttribute("row"));
      int col = parseInt(doc.getDocumentElement().getAttribute("col"));
      //create new board array
      Room[][] board = new Room[row][col];

      //parse rooms
      NodeList roomList = doc.getElementsByTagName("room");
      for (int i = 0; i < roomList.getLength(); i++) {
        parseRoom(roomList.item(i), board);
      }

      //parse player
      Node playerNode = doc.getElementsByTagName("player").item(0);
      Player player = parsePlayer(playerNode, board);

      //parse directions
      String dir = doc.getDocumentElement().getAttribute("direction");
      String initDir = doc.getDocumentElement().getAttribute("initDirection");

      return new Game(board, player, initDir, dir);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new ParseError("Invalid XML File!");
    }
  }

  /**
   * A method to parse rooms into the game.
   * @param room XML node element for the room
   * @param board board to place room in
   * @throws ParseError throws a parse error if room element is invalid
   */
  private static void parseRoom(Node room, Room[][] board) throws ParseError {
    List<String> portals = new ArrayList<>();
    Element roomElement = (Element) room;

    Tile[][] tiles = new Tile[Room.ROOMSIZE][Room.ROOMSIZE];

    for (int i = 0; i < Room.ROOMSIZE; i++) {
      for (int j = 0; j < Room.ROOMSIZE; j++) {
        if (i == 0 || j == 0 || j == Room.ROOMSIZE - 1 || i == Room.ROOMSIZE - 1) {
          tiles[i][j] = new InaccessibleTile(i, j);
        } else {
          tiles[i][j] = new AccessibleTile(i, j);
        }
      }
    }

    //parse portals
    NodeList portalList = roomElement.getElementsByTagName("portal");
    for (int i = 0; i < portalList.getLength(); i++) {
      String portal = portalList.item(i).getTextContent();
      portals.add(portal);
    }

    //parse items
    NodeList itemList = roomElement.getElementsByTagName("item");
    parseItems(itemList, tiles, null);

    //parse challenges
    NodeList challengeList = roomElement.getElementsByTagName("challenge");
    parseChallenges(challengeList, tiles);

    //parse row and col
    int row = parseInt(roomElement.getAttribute("row"));
    int col = parseInt(roomElement.getAttribute("col"));

    Room newRoom = new Room(row, col, tiles, portals);
    board[row][col] = newRoom;
  }

  /**
   * A method to parse the player into the game.
   * @param playerNode XML node element for the player
   * @param board board to find the room to place player in
   * @return player in the game
   * @throws ParseError throws a parse error if player element is invalid
   */
  private static Player parsePlayer(Node playerNode, Room[][] board) throws ParseError {
    Element playerElement = (Element) playerNode;

    //Get the player position from its row and col attributes
    if (playerElement.getAttribute("row").equals("")
        || playerElement.getAttribute("col").equals("")) {
      throw new ParseError("Player needs position attributes (row and col)");
    }
    int row = parseInt(playerElement.getAttribute("row"));
    int col = parseInt(playerElement.getAttribute("col"));

    //Get the player's health and the direction they are facing
    if (playerElement.getAttribute("oxygen").equals("")) {
      throw new ParseError("Player needs oxygen attribute");
    }
    int oxygen = parseInt(playerElement.getAttribute("oxygen"));

    if (playerElement.getAttribute("direction").equals("")) {
      throw new ParseError("Player needs direction attribute");
    }
    String direction = playerElement.getAttribute("direction");

    //Get the room in the board that the player is in
    int roomRow = parseInteger("roomRow", playerElement);
    int roomCol = parseInteger("roomCol", playerElement);
    Room playerRoom = board[roomRow][roomCol];
    AccessibleTile playerTile = (AccessibleTile) playerRoom.getTile(row, col);
    Player player = new Player(playerRoom, playerTile, oxygen, direction);
    playerTile.setPlayer(true);

    //Parse the player's item
    NodeList playerItem = playerElement.getElementsByTagName("item");
    parseItems(playerItem, null, player);

    return player;
  }

  /**
   * A method to parse a number from an element.
   * @param tagName the name of the tag to search
   * @param element the element to get the tag from
   * @return the integer value inside the element
   */
  private static int parseInteger(String tagName, Element element) {
    NodeList n = element.getElementsByTagName(tagName);
    return parseInt(n.item(0).getTextContent());
  }

  /**
   * A method to parse items from an element.
   *
   * @param items the node list to get the items from
   * @param tiles tiles in the room that can be set with items
   * @param p player to give item to
   * @throws ParseError throws a parse error if item element is invalid
   */
  private static void parseItems(NodeList items, Tile[][] tiles, Player p) throws ParseError {
    for (int i = 0; i < items.getLength(); i++) {
      String token = items.item(i).getTextContent().trim();
      if (!token.equals("")) {
        Element elem = (Element) items.item(i);
        if (elem.getAttribute("row").equals("")
            || elem.getAttribute("col").equals("")) {
          throw new ParseError("Item needs position attributes (row and col)");
        }
        int row = parseInt(elem.getAttribute("row"));
        int col = parseInt(elem.getAttribute("col"));
        String direction = elem.getAttribute("direction");
        Item item;
        switch (token) {
          case "Potion":
            item = new Potion(row, col, direction);
            break;
          case "SpaceShip":
            item = new SpaceShip(row, col, direction);
            break;
          case "BoltCutter":
            item = new BoltCutter(row, col, direction);
            break;
          case "Coin":
            item = new Coin(row, col, direction);
            break;
          case "Diffuser":
            item = new Diffuser(row, col, direction);
            break;
          case "OxygenTank":
            item = new OxygenTank(row, col, direction);
            break;
          default:
            throw new ParseError("Incorrect item name");
        }

        //Set the item for the tile at the given row and col
        if (tiles != null) {
          item.setRow(row);
          item.setCol(col);
          ((AccessibleTile) tiles[row][col]).setItem(item);
        } else if (p != null) {
          p.addItem(item); //Give the item to the player to hold
        }
      }
    }
  }

  /**
   * A method to parse each challenge and set their tiles.
   * @param challengeList the node list to get challenges from
   * @param tiles tiles in the room that can be set with challenges
   * @throws ParseError throws parse error
   */
  private static void parseChallenges(NodeList challengeList, Tile[][] tiles) throws ParseError {

    for (int i = 0; i < challengeList.getLength(); i++) {
      Node node = challengeList.item(i);
      Element elem = (Element) node;
      //Get challenge attributes
      if (elem.getAttribute("row").equals("")
          || elem.getAttribute("col").equals("")) {
        throw new ParseError("Challenge needs position attributes (row and col)");
      }
      int row = parseInt(elem.getAttribute("row"));
      int col = parseInt(elem.getAttribute("col"));
      if (elem.getAttribute("direction").equals("")
          || elem.getAttribute("state").equals("")) {
        throw new ParseError("Challenge needs direction and state attributes");
      }
      String state = elem.getAttribute("state");
      String direction = elem.getAttribute("direction");

      switch (node.getTextContent().trim()) {
        case "Bomb":
          Bomb bomb = new Bomb(row, col, direction);
          bomb.setNavigable(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[row][col]).setChallenge(bomb);
          break;
        case "Alien":
          Alien alien = new Alien(row, col, direction);
          alien.setNavigable(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[row][col]).setChallenge(alien);
          break;
        case "VendingMachine":
          VendingMachine vm = new VendingMachine(row,col, direction);
          vm.setUnlocked(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[row][col]).setChallenge(vm);
          break;
        default:
          throw new ParseError("Incorrect challenge name");
      }
    }
  }

  /**
   * A method to load the schema for XML files.
   * @param fileName name xsd file to parse
   * @throws SAXException throws SAX exception
   */
  private static void loadSchema(String fileName) throws SAXException {
    String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    SchemaFactory factory = SchemaFactory.newInstance(language);
    schema = factory.newSchema(new File(fileName));
  }

  /**
   * A custom inner exception class to handle errors.
   * @author Sam Ong 300363819
   */
  public static class ParseError extends Exception {
    ParseError(String message) {
      super(message);
    }
  }
}
