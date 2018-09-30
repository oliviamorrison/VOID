package persistence;
import gameworld.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class XMLParser {

    public static Game newGame() {
        while (true) {
            JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
            int res = chooser.showOpenDialog(null);
            if (res != JFileChooser.APPROVE_OPTION) {
                break;
            }
//            Game game = parseGame(chooser.getSelectedFile());
            Game game = parseGame();
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

    public static Game parseGame() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(file);
            Document doc = dBuilder.parse("data/gameworld.xml");
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
        List<Token> tokens = new ArrayList<>();

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
        parseItems(itemList, tokens);

        Room newRoom = new Room(doors, tokens);
        board[row][col] = newRoom;
    }

    private static Player parsePlayer(Node playerNode, Room[][] board) {
        Element playerElement = (Element) playerNode;
        int row = parseInteger("playerRow", playerElement);
        int col = parseInteger("playerCol", playerElement);

        Room playerRoom = board[row][col];
        Player player = new Player(playerRoom, (AccessibleTile) playerRoom.getTile(5,5)); //TODO: Hard coded for now

        NodeList inventory = playerElement.getElementsByTagName("inventory");
        parseItems(inventory, player.getInventory());

        return player;
    }

    private static int parseInteger(String tagName, Element element){
        return parseInt(element.getElementsByTagName(tagName).item(0).getTextContent());
    }

    private static void parseItems(NodeList items, List<Token> tokens) {
        for(int i = 0; i< items.getLength(); i++){
            String token = items.item(i).getTextContent().trim(); //TODO: Figure out why when there are more than 1 item it doesn't trim it
            System.out.println(token);
            switch(token){
                case "key": tokens.add(new Key()); break;
                case "prize": tokens.add(new Prize()); break;
                case "diffuser": tokens.add(new Diffuser()); break;
                case "bomb": tokens.add(new Bomb()); break;
                case "coin": tokens.add(new Coin()); break;
            }
        }
    }

    public static void main(String[] args){
        parseGame();
    }
}
