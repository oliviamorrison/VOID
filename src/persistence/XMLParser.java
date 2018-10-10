package persistence;
import gameworld.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Integer.parseInt;

/**
 * Using javax.xml.parsers library
 */

public class XMLParser {
  private static Schema schema;

  public static void saveFile(File file, Game game){
    try {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();

      Room[][] board = game.getBoard();

      //save game
      Element root = saveGame(document, board);
      document.appendChild(root);

      //save room
      for(int i = 0; i < board.length; i++){
        for(int j = 0; j < board[i].length; j++){
          if(board[i][j]!=null){
            Room room = board[i][j];
            Element roomElement = saveRoom(document, i, j, room);
            //Add room
            root.appendChild(roomElement);
          }
        }
      }

      //save player
      Element player = savePlayer(game, document);
      root.appendChild(player);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      //Add line breaks and indentation
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      //Save to file
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(file);
      document.getDocumentElement().normalize();
      transformer.transform(source, result);

    } catch (ParserConfigurationException | TransformerException e) {
      e.printStackTrace();
    }
  }

  private static Element saveGame(Document document, Room[][] board) {
    // root element
    Element root = document.createElement("game");

    // rows element
    Element rows = document.createElement("rows");
    rows.appendChild(document.createTextNode(board.length+""));
    root.appendChild(rows);

    //cols element
    Element cols = document.createElement("cols");
    cols.appendChild(document.createTextNode(board[0].length+""));
    root.appendChild(cols);
    return root;
  }

  private static Element saveRoom(Document document, int i, int j, Room room) {
    Element roomElement = document.createElement("room");

    //row
    Element row = document.createElement("row");
    row.appendChild(document.createTextNode(i+""));
    roomElement.appendChild(row);
    //col
    Element col = document.createElement("col");
    col.appendChild(document.createTextNode(j+""));
    roomElement.appendChild(col);

    for(String direction: room.getDoors()){
      Element door = document.createElement("door");
      door.appendChild((document.createTextNode(direction)));
      roomElement.appendChild(door);
    }

    saveItems(document, room.getItems(), roomElement);
    saveChallenges(document, room.getChallenges(), roomElement);

    return roomElement;
  }

  private static Element savePlayer(Game game, Document document) {
    Element player = document.createElement("player");

    //room coordinates
    Element roomRow = document.createElement("roomRow");
    roomRow.appendChild(document.createTextNode(game.getPlayer().getRoom().getRow()+""));
    Element roomCol = document.createElement("roomCol");
    roomCol.appendChild(document.createTextNode(game.getPlayer().getRoom().getCol()+""));

    player.appendChild(roomRow);
    player.appendChild(roomCol);

    //tile coordinates
    Element tileRow = document.createElement("tileRow");
    tileRow.appendChild(document.createTextNode(game.getPlayer().getTile().getX()+""));
    Element tileCol = document.createElement("tileCol");
    tileCol.appendChild(document.createTextNode(game.getPlayer().getTile().getY()+""));

    player.appendChild(tileRow);
    player.appendChild(tileCol);

    //save inventory
    Element inventory = document.createElement("inventory");
    saveItems(document, game.getPlayer().getInventory(), inventory);
    player.appendChild(inventory);

    return player;
  }


  private static void saveItems(Document document, List<Item> items, Element itemCollector){
    for(Item token: items){
      Element item = document.createElement("item");
      item.appendChild(document.createTextNode(token.toString()));
      itemCollector.appendChild(item);
    }
  }

  private static void saveChallenges(Document document, List<Challenge> challenges, Element challengeCollector){
    for(Challenge challengeItem: challenges){
      Element challenge = document.createElement("challenge");

      if(challengeItem instanceof Bomb) {
        Bomb bomb = (Bomb) challengeItem;
        challenge.setAttribute("door", bomb.getDirection());
      }
      else if(challengeItem instanceof Guard){
        Guard guard = (Guard) challengeItem;
        challenge.setAttribute("door", guard.getDirection());
      }

      challenge.appendChild(document.createTextNode(challengeItem.toString()));
      challengeCollector.appendChild(challenge);
    }
  }


  public static Game parseGame(File file) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = factory.newDocumentBuilder();

      Document doc = dBuilder.parse(file);

      DOMSource source = new DOMSource(doc);
      //using schema.xsd to validate the schema
      loadSchema();
      Validator validator = schema.newValidator();
      validator.validate(source);

      doc.getDocumentElement().normalize();

      //parse rows and cols
      int rows = parseInteger("rows", doc.getDocumentElement());
      int cols = parseInteger("cols", doc.getDocumentElement());

      //create new board array
      Room[][] board = new Room[rows][cols];

      //parse rooms
      NodeList roomList = doc.getElementsByTagName("room");
      for(int i = 0; i < roomList.getLength(); i++) parseRoom(roomList.item(i), board);

      //parse player
      Node playerNode = doc.getElementsByTagName("player").item(0);
      Player player = parsePlayer(playerNode, board);

      Game game = new Game(board, player);
      return game;
    }
    catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static void parseRoom(Node room, Room[][] board) {
    List<String> doors = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<Challenge> challenges = new ArrayList<>();

    Element roomElement = (Element) room;

    //parse row and col
    int row = parseInteger("row", roomElement);
    int col = parseInteger("col", roomElement);

    //parse doors
    NodeList doorList = roomElement.getElementsByTagName("door");
    for(int i = 0; i< doorList.getLength(); i++){
      String door = doorList.item(i).getTextContent();
      doors.add(door);
    }

    //parse items
    NodeList itemList = roomElement.getElementsByTagName("item");
    parseItems(itemList, items);

    //parse challenges
    NodeList challengeList = roomElement.getElementsByTagName("challenge");
    parseChallenges(challengeList, challenges);

    Room newRoom = new Room(row, col, doors, items, challenges);
    board[row][col] = newRoom;
  }

  private static Player parsePlayer(Node playerNode, Room[][] board) {
    Element playerElement = (Element) playerNode;
    int roomRow = parseInteger("roomRow", playerElement);
    int roomCol = parseInteger("roomCol", playerElement);

    Room playerRoom = board[roomRow][roomCol];

    int tileRow = parseInteger("tileRow", playerElement);
    int tileCol = parseInteger("tileCol", playerElement);

    Player player = new Player(playerRoom, (AccessibleTile) playerRoom.getTile(tileRow,tileCol));
    ((AccessibleTile) playerRoom.getTile(tileRow, tileCol)).setPlayer(true);

    NodeList inventory = playerElement.getElementsByTagName("inventory");
    parseItems(inventory, player.getInventory());

    return player;
  }

  private static int parseInteger(String tagName, Element element){
    NodeList n = element.getElementsByTagName(tagName);
    return parseInt(n.item(0).getTextContent());
  }

  private static void parseItems(NodeList items, List<Item> tokens) {
    for(int i = 0; i< items.getLength(); i++){
      String token = items.item(i).getTextContent().trim(); //TODO: Figure out why when there are more than 1 item it doesn't trim it
      switch(token){
        case "Antidote": tokens.add(Item.Antidote); break;
        case "Beer": tokens.add(Item.Beer); break;
        case "Diffuser": tokens.add(Item.Diffuser); break;
        case "Coin": tokens.add(Item.Coin); break;
        case "BoltCutter": tokens.add(Item.BoltCutter); break;
      }
    }
  }

  private static void parseChallenges(NodeList items, List<Challenge> challenges) {

    for(int i = 0; i< items.getLength(); i++){
      Node node = items.item(i);
      switch(node.getTextContent().trim()){
        case "Bomb":
          Element elem = (Element) node;
          String direction = elem.getAttribute("door");
          challenges.add(new Bomb(direction));
          break;
        case "Guard":
          elem = (Element) node;
          direction = elem.getAttribute("door");
          challenges.add(new Guard(direction));
          break;
        case "VendingMachine": challenges.add(new VendingMachine()); break;
      }
    }
  }

  private static void loadSchema(){
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File("src/persistence/schema.xsd"));
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }
}
