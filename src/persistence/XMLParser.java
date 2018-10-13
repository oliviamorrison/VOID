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
 * Schema is in schema.xsd
 */
//TODO: Make default new game file ineditable
//TODO: If we have time, add different difficulty levels for easy/medium/hard
//TODO: Add README
//TODO: Add tests
//TODO: UML Diagram

//TODO: Remove item from list of items in room when picked up
//TODO: Change door tile to portal

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

    for(int y = 0; y < 10; y++){
      for(int x = 0; x < 10; x++){
        Tile tile = room.getTile(x,y);
        if(tile instanceof Portal){
          Portal door = (Portal) tile;
          Element doorElement = document.createElement("door");
          doorElement.appendChild((document.createTextNode(door.toString())));
          roomElement.appendChild(doorElement);
        }
        else if(tile instanceof AccessibleTile){
          AccessibleTile aTile = (AccessibleTile) tile;

          //save items
          if(aTile.hasItem()){
            Element item = document.createElement("item");
            item.setAttribute("row", aTile.getItem().getRow()+"");
            item.setAttribute("col", aTile.getItem().getCol()+"");
            item.appendChild(document.createTextNode(aTile.getItem().toString()));
            roomElement.appendChild(item);
          }

          //save challenges
          if(aTile.hasChallenge()){
            ChallengeItem challengeItem = aTile.getChallenge();
            Element challenge = document.createElement("challenge");

            if(challengeItem instanceof Bomb) {
              Bomb bomb = (Bomb) challengeItem;
              challenge.setAttribute("state", bomb.isNavigable()+"");
            }
            else if(challengeItem instanceof Guard){
              Guard guard = (Guard) challengeItem;
              challenge.setAttribute("state", guard.isNavigable()+"");
            }
            else{
              VendingMachine vm = (VendingMachine) challengeItem;
              challenge.setAttribute("state", vm.isUnlocked()+"");
            }
            challenge.setAttribute("row", challengeItem.getRow()+"");
            challenge.setAttribute("col", challengeItem.getCol()+"");
            challenge.appendChild(document.createTextNode(aTile.getChallenge().toString()));
            roomElement.appendChild(challenge);
          }
        }
      }
    }
    return roomElement;
  }

  private static Element savePlayer(Game game, Document document) {
    Element player = document.createElement("player");
    //Get position of player and add as attribute to player element
    player.setAttribute("row", game.getPlayer().getTile().getRow()+"");
    player.setAttribute("col", game.getPlayer().getTile().getCol()+"");
    player.setAttribute("health", game.getPlayer().getHealth()+"");
    player.setAttribute("direction", game.getPlayer().getPlayerDir().toString());

    //Add coordinates of the room the player is in to the player element
    Element roomRow = document.createElement("roomRow");
    roomRow.appendChild(document.createTextNode(game.getPlayer().getRoom().getRow()+""));
    Element roomCol = document.createElement("roomCol");
    roomCol.appendChild(document.createTextNode(game.getPlayer().getRoom().getCol()+""));
    player.appendChild(roomRow);
    player.appendChild(roomCol);

    //save inventory
    Element inventory = document.createElement("inventory");
    for(Item item : game.getPlayer().getInventory()){
      Element itemElement = document.createElement("item");
      itemElement.appendChild(document.createTextNode(item.toString()));
      inventory.appendChild(itemElement);
    }

    player.appendChild(inventory);

    return player;
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
      throw new ParseError("Invalid XML File!");
    }
  }

  private static void parseRoom(Node room, Room[][] board) throws ParseError {
    List<String> doors = new ArrayList<>();
    Element roomElement = (Element) room;

    Tile[][] tiles = new Tile[Room.ROOMSIZE][Room.ROOMSIZE];

    //parse row and col
    int[] rowCol = getRowCol(roomElement);

    for(int i = 0; i < Room.ROOMSIZE; i++){
      for(int j = 0; j < Room.ROOMSIZE; j++){
        if (i == 0 || j == 0 || j == Room.ROOMSIZE - 1 || i == Room.ROOMSIZE - 1)
          tiles[i][j] = new InaccessibleTile(i, j);
        else tiles[i][j] = new AccessibleTile(i, j);
      }
    }

    //parse doors
    NodeList doorList = roomElement.getElementsByTagName("door");
    for(int i = 0; i< doorList.getLength(); i++){
      String door = doorList.item(i).getTextContent();
      doors.add(door);
    }

    //parse items
    NodeList itemList = roomElement.getElementsByTagName("item");
    parseItems(itemList, tiles, null);

    //parse challenges
    NodeList challengeList = roomElement.getElementsByTagName("challenge");
    parseChallenges(challengeList, tiles);


    Room newRoom = new Room(rowCol[0], rowCol[1], tiles, doors);

//    Room newRoom = new Room(rowCol[0], rowCol[1], doors, items, challenges);
    board[rowCol[0]][rowCol[1]] = newRoom;
  }

  private static Player parsePlayer(Node playerNode, Room[][] board) throws ParseError {
    Element playerElement = (Element) playerNode;
    int roomRow = parseInteger("roomRow", playerElement);
    int roomCol = parseInteger("roomCol", playerElement);

    Room playerRoom = board[roomRow][roomCol];
    int[] rowCol = getRowCol(playerElement);
    if(playerElement.getAttribute("health").equals("")) throw new ParseError("Player needs health attribute");
    int health = parseInt(playerElement.getAttribute("health"));
    if(playerElement.getAttribute("direction").equals("")) throw new ParseError("Player needs direction attribute");
    String direction = playerElement.getAttribute("direction");

    Player player = new Player(playerRoom, (AccessibleTile) playerRoom.getTile(rowCol[0], rowCol[1]), health, direction);
    ((AccessibleTile) playerRoom.getTile(rowCol[0], rowCol[1])).setPlayer(true);

    NodeList inventory = playerElement.getElementsByTagName("inventory");
    parseItems(inventory, null, player);

    return player;
  }

  private static int parseInteger(String tagName, Element element){
    NodeList n = element.getElementsByTagName(tagName);
    return parseInt(n.item(0).getTextContent());
  }

  private static void parseItems(NodeList items, Tile[][] tiles, Player p) throws ParseError {
    for(int i = 0; i< items.getLength(); i++){
      String token = items.item(i).getTextContent().trim(); //TODO: Figure out why when there are more than 1 item it doesn't trim it
      if(!token.equals("")){
        Element elem = (Element) items.item(i);
        int[] rowCol = getRowCol(elem);

        Item item;
        switch(token){
          case "Antidote":
            item = new Antidote(rowCol[0], rowCol[1]);
            break;
          case "Beer":
            item = new Beer(rowCol[0], rowCol[1]);
            break;
          case "BoltCutter":
            item = new BoltCutter(rowCol[0], rowCol[1]);
            break;
          case "Coin":
            item = new Coin(rowCol[0], rowCol[1]);
            break;
          case "Diffuser":
            item = new Diffuser(rowCol[0], rowCol[1]);
            break;
          case "HealthPack":
            item = new HealthPack(rowCol[0], rowCol[1]);
            break;
          default:
            throw new ParseError("Incorrect item name");
        }

        if(tiles!=null){
          item.setRow(rowCol[0]);
          item.setCol(rowCol[1]);
          ((AccessibleTile) tiles[rowCol[0]][rowCol[1]]).setItem(item);
        }
        else if(p!=null){
          p.getInventory().add(item);
        }

      }
    }
  }

  private static void parseChallenges(NodeList items, Tile[][] tiles) throws ParseError {

    for(int i = 0; i< items.getLength(); i++){
      Node node = items.item(i);
      Element elem = (Element) node;
      int[] rowCol = getRowCol(elem);
      String state = elem.getAttribute("state");
      switch(node.getTextContent().trim()){
        case "Bomb":
          Bomb bomb = new Bomb(rowCol[0], rowCol[1]);
          bomb.setNavigable(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[rowCol[0]][rowCol[1]]).setChallenge(bomb);
          break;
        case "Guard":
          Guard guard = new Guard(rowCol[0], rowCol[1]);
          guard.setNavigable(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[rowCol[0]][rowCol[1]]).setChallenge(guard);
          break;
        case "VendingMachine":
          VendingMachine vm = new VendingMachine(rowCol[0], rowCol[1]);
          vm.setUnlocked(Boolean.parseBoolean(state));
          ((AccessibleTile)tiles[rowCol[0]][rowCol[1]]).setChallenge(vm);
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

  private static int[] getRowCol(Element elem) throws ParseError {
    if(elem.getAttribute("row").equals("") || elem.getAttribute("col").equals(""))
      throw new ParseError("Player needs position attribute");

    int row = parseInt(elem.getAttribute("row"));
    int col = parseInt(elem.getAttribute("col"));
    return new int[]{row, col};
  }

  public static class ParseError extends Exception{
    ParseError(String message){
      super(message);
    }
  }
}
