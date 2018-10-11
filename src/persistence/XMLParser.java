package persistence;
import gameworld.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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

import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;

/**
 * Using javax.xml.parsers library
 * Schema is in schema.xsd
 */
//TODO: Make default new game file ineditable
//TODO: If we have time, add different difficulty levels for easy/medium/hard
//TODO: Add README
//TODO: Add tests
//TODO: UML Diagram


//TODO: Test player inventory parsing
//TODO: Remove item from list of items in room when picked up
//TODO: Change doortile to portal
public class XMLParser {
  private static Schema schema;

  public static void saveFile(File file, Game game){
    try {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();

      Room[][] board = game.getBoard();

      //add root to XML file
      Element root = document.createElement("game");
      root.setAttribute("row", board.length+"");
      root.setAttribute("col", board[0].length+"");
      document.appendChild(root);

      //save rooms
      for(int i = 0; i < board.length; i++){
        for(int j = 0; j < board[i].length; j++){
          if(board[i][j]!=null){
            root.appendChild(saveRoom(document, i, j, board[i][j]));
          }
        }
      }

      //save player
      root.appendChild(savePlayer(game, document));

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

  private static Element saveRoom(Document document, int i, int j, Room room) {
    Element roomElement = document.createElement("room");
    roomElement.setAttribute("row", i+"");
    roomElement.setAttribute("col", j+"");

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
    //Get position of player and add as attribute to player element
    player.setAttribute("row", game.getPlayer().getTile().getX()+"");
    player.setAttribute("col", game.getPlayer().getTile().getY()+"");

    //Add coordinates of the room the player is in to the player element
    Element roomRow = document.createElement("roomRow");
    roomRow.appendChild(document.createTextNode(game.getPlayer().getRoom().getRow()+""));
    Element roomCol = document.createElement("roomCol");
    roomCol.appendChild(document.createTextNode(game.getPlayer().getRoom().getCol()+""));
    player.appendChild(roomRow);
    player.appendChild(roomCol);

    //save inventory
    Element inventory = document.createElement("inventory");
    saveItems(document, game.getPlayer().getInventory(), inventory); //TODO: Change this bc inventory items don't have X and Y
    player.appendChild(inventory);

    return player;
  }


  private static void saveItems(Document document, List<Item> items, Element itemCollector){
    for(Item token: items){
      Element item = document.createElement("item");
      if(token.getX() == -1 || token.getY() == -1) return;
      item.setAttribute("row", token.getX()+"");
      item.setAttribute("col", token.getY()+"");
      item.appendChild(document.createTextNode(token.toString()));
      itemCollector.appendChild(item);
    }
  }

  private static void saveChallenges(Document document, List<Challenge> challenges, Element challengeCollector){
    for(Challenge challengeItem: challenges){
      Element challenge = document.createElement("challenge");

      if(challengeItem instanceof Bomb) {
        Bomb bomb = (Bomb) challengeItem;
        challenge.setAttribute("row", bomb.getX()+"");
        challenge.setAttribute("col", bomb.getY()+"");
        challenge.setAttribute("state", bomb.isNavigable()+"");
      }
      else if(challengeItem instanceof Guard){
        Guard guard = (Guard) challengeItem;
        challenge.setAttribute("row", guard.getX()+"");
        challenge.setAttribute("col", guard.getY()+"");
        challenge.setAttribute("state", guard.isNavigable()+"");
      }
      else{
        VendingMachine vm = (VendingMachine) challengeItem;
        challenge.setAttribute("row", vm.getX()+"");
        challenge.setAttribute("col", vm.getY()+"");
        challenge.setAttribute("state", vm.isNavigable()+"");
      }

      challenge.appendChild(document.createTextNode(challengeItem.toString()));
      challengeCollector.appendChild(challenge);
    }
  }


  public static Game parseGame(File file) throws ParseError {
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
      int[] rowCol = getRowCol(doc.getDocumentElement());

      //create new board array
      Room[][] board = new Room[rowCol[0]][rowCol[1]];

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
      throw new ParseError("Invalid XML");
    }
  }

  public static class ParseError extends Exception{
    ParseError(String message){
      super(message);
    }
  }

  private static void parseRoom(Node room, Room[][] board) {
    List<String> doors = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<Challenge> challenges = new ArrayList<>();

    Element roomElement = (Element) room;

    //parse row and col
    int[] rowCol = getRowCol(roomElement);

    //parse doors
    NodeList doorList = roomElement.getElementsByTagName("door");
    for(int i = 0; i< doorList.getLength(); i++){
      String door = doorList.item(i).getTextContent();
      doors.add(door);
    }

    //parse items
    NodeList itemList = roomElement.getElementsByTagName("item");
    parseItems(itemList, items, false);

    //parse challenges
    NodeList challengeList = roomElement.getElementsByTagName("challenge");
    parseChallenges(challengeList, challenges);

    Room newRoom = new Room(rowCol[0], rowCol[1], doors, items, challenges);
    board[rowCol[0]][rowCol[1]] = newRoom;
  }

  private static Player parsePlayer(Node playerNode, Room[][] board) {
    Element playerElement = (Element) playerNode;
    int roomRow = parseInteger("roomRow", playerElement);
    int roomCol = parseInteger("roomCol", playerElement);

    Room playerRoom = board[roomRow][roomCol];
    int[] rowCol = getRowCol(playerElement);

    Player player = new Player(playerRoom, (AccessibleTile) playerRoom.getTile(rowCol[0], rowCol[1]), -1);
    ((AccessibleTile) playerRoom.getTile(rowCol[0], rowCol[1])).setPlayer(true);

    NodeList inventory = playerElement.getElementsByTagName("inventory");
    parseItems(inventory, player.getInventory(), true);

    return player;
  }

  private static int parseInteger(String tagName, Element element){
    NodeList n = element.getElementsByTagName(tagName);
    return parseInt(n.item(0).getTextContent());
  }

  private static void parseItems(NodeList items, List<Item> tokens, boolean isInventory) {
    for(int i = 0; i< items.getLength(); i++){
      String token = items.item(i).getTextContent().trim(); //TODO: Figure out why when there are more than 1 item it doesn't trim it
      if(!token.equals("")){
        Element elem = (Element) items.item(i);

        Item item = null;
        switch(token){
          case "Antidote":
            item = Item.Antidote;break;
          case "Beer":
            item = Item.Beer;
            break;
          case "BoltCutter":
            item = Item.BoltCutter;
            break;
          case "Coin":
            item = Item.Coin;
            break;
          case "Diffuser":
            item = Item.Diffuser;
            break;
          case "HealthPack":
            item = Item.HealthPack;
            break;
        }

        if(item!=null && !isInventory){
          int[] rowCol = getRowCol(elem);
          item.setX(rowCol[0]);
          item.setY(rowCol[1]);
          tokens.add(item);
        }
        else if(item!=null)tokens.add(item);

      }
    }
  }

  private static void parseChallenges(NodeList items, List<Challenge> challenges) {

    for(int i = 0; i< items.getLength(); i++){
      Node node = items.item(i);
      Element elem = (Element) node;
      int[] rowCol = getRowCol(elem);
      String state = elem.getAttribute("state");
      switch(node.getTextContent().trim()){
        case "Bomb":
          Bomb bomb = new Bomb(rowCol[0], rowCol[1]);
          bomb.setNavigable(Boolean.parseBoolean(state));
          challenges.add(bomb);
          break;
        case "Guard":
          Guard guard = new Guard(rowCol[0], rowCol[1]);
          guard.setNavigable(Boolean.parseBoolean(state));
          challenges.add(guard);
          break;
        case "VendingMachine":
          VendingMachine vm = new VendingMachine(rowCol[0], rowCol[1]);
          vm.setUnlocked(Boolean.parseBoolean(state));
          challenges.add(vm);
          break;
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

  private static int[] getRowCol(Element elem){
    int row = parseInt(elem.getAttribute("row"));
    int col = parseInt(elem.getAttribute("col"));
    return new int[]{row, col};
  }

  //TODO: Save and load players health
}
