package persistence;
import gameworld.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class XMLParser {

    public static void saveFile(Game game){

        JFileChooser fileChooser = new JFileChooser("./data/");
        fileChooser.showSaveDialog(null);
        String fileName = fileChooser.getSelectedFile().getName();
        if(!fileName.endsWith(".xml")) fileName = fileName + ".xml";

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Room[][] board = game.getBoard();

            //load game
            Element root = saveGame(document, board);
            document.appendChild(root);

            //load room
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

            //load player
            Element player = savePlayer(game, document);
            root.appendChild(player);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //Add line breaks and indentation
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
             StreamResult result = new StreamResult(new File("./data/"+fileName));

//            StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
            System.out.println("File saved!");

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

        Element items = document.createElement("items");
        for(Item token: room.getItems()){
            Element item = document.createElement("item");
            item.appendChild(document.createTextNode(token.toString()));
            items.appendChild(item);
        }
        roomElement.appendChild(items);

        Element challenges = document.createElement("challenges");
        for(Challenge challengeItem: room.getChallenges()){
            Element challenge = document.createElement("challenge");
            challenge.appendChild(document.createTextNode(challengeItem.toString()));
            challenges.appendChild(challenge);
        }
        roomElement.appendChild(items);
        return roomElement;
    }

    private static Element savePlayer(Game game, Document document) {
        Element player = document.createElement("player");

        //room coordinates
        int roomX = game.getPlayer().getRoom().getRow();
        int roomY = game.getPlayer().getRoom().getCol();
        Element roomRow = document.createElement("roomRow");
        roomRow.appendChild(document.createTextNode(roomX+""));
        Element roomCol = document.createElement("roomCol");
        roomCol.appendChild(document.createTextNode(roomY+""));

        player.appendChild(roomRow);
        player.appendChild(roomCol);

        //tile coordinates
        int tileX = game.getPlayer().getTile().getX();
        int tileY = game.getPlayer().getTile().getY();
        Element tileRow = document.createElement("tileRow");
        tileRow.appendChild(document.createTextNode(tileX+""));
        Element tileCol = document.createElement("tileCol");
        tileCol.appendChild(document.createTextNode(tileY+""));

        player.appendChild(tileRow);
        player.appendChild(tileCol);

        //inventory
        Element inventory = document.createElement("inventory");
        for(Item token: game.getPlayer().getInventory()){
            Element item = document.createElement("item");
            item.appendChild(document.createTextNode(token.toString()));
            inventory.appendChild(item);
        }
        player.appendChild(inventory);
        return player;
    }


    public static Game loadFile() {
        while (true) {
            JFileChooser chooser = new JFileChooser(".");
            int res = chooser.showOpenDialog(null);
            if (res != JFileChooser.APPROVE_OPTION) {
                break;
            }
            Game game = parseGame(chooser.getSelectedFile());
            System.out.println("Parsing completed");
            if (game != null) {
                System.out.println("game parsed");
                return game;
            }
            System.out.println("=================");
        }
        System.out.println("Done");
        return null;
    }

        public static Game parseGame(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(false);
            factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
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
        NodeList itemList = roomElement.getElementsByTagName("items");
        parseItems(itemList, items);

        //parse challenges
        NodeList challengeList = roomElement.getElementsByTagName("challenges");
        parseChallenges(challengeList, challenges);

        Room newRoom = new Room(row, col, doors, items);
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
                case "antidote": tokens.add(new Key()); break;
                case "beer": tokens.add(new Prize()); break;
                case "diffuser": tokens.add(new Diffuser()); break;
                case "coin": tokens.add(new Coin()); break;
                case "boltcutter": tokens.add(new Coin()); break;
            }
        }
    }

    private static void parseChallenges(NodeList items, List<Challenge> challenges) {
        for(int i = 0; i< items.getLength(); i++){
            String token = items.item(i).getTextContent().trim(); //TODO: Figure out why when there are more than 1 item it doesn't trim it
            switch(token){
                case "bomb": challenges.add(null); break;
                case "guard": challenges.add(null); break;
                case "vendingmachine": challenges.add(null); break; //TODO: Null for now
            }
        }
    }
}
