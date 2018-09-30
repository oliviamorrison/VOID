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

            Node rowsNode = doc.getElementsByTagName("rows").item(0);
            int rows = parseInt(rowsNode.getTextContent());
            System.out.println("rows = " + rows);

            Node colsNode = doc.getElementsByTagName("cols").item(0);
            int cols = parseInt(colsNode.getTextContent());
            System.out.println("cols = " + cols);

            Room[][] board = new Room[rows][cols];

            NodeList roomList = doc.getElementsByTagName("room");
            for(int i = 0; i < roomList.getLength(); i++){
                parseRoom(roomList.item(i), board);
            }

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
        NodeList nodeList = room.getChildNodes();
        int row = -1;
        int col = -1;
        List<String> doors = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();

        for(int i = 0; i < nodeList.getLength(); i++){
            Node n = nodeList.item(i);
            if(n.getNodeName().equals("row")){
                row = parseInt(n.getTextContent());
                System.out.println("row = " + row);
            }
            else if(n.getNodeName().equals("col")){
                col = parseInt(n.getTextContent());
                System.out.println("col = " + col);
            }
            else if(n.getNodeName().equals("door")){
                String door = n.getTextContent();
                System.out.println("door = " + door);
                doors.add(door);
            }
            else if(n.getNodeName().equals("items")){
               parseItems(n.getChildNodes(), tokens);
            }
        }

        assert(row!=-1);
        assert(col!=-1);
        assert(!doors.isEmpty());

        Room newRoom = new Room(doors, tokens);
        board[row][col] = newRoom;
    }

    private static Player parsePlayer(Node playerNode, Room[][] board) {
        NodeList nodeList = playerNode.getChildNodes();
        int row = -1;
        int col = -1;
        for(int i = 0; i < nodeList.getLength(); i++){
            if(nodeList.item(i).getNodeName().equals("playerRow")){
                row = parseInt(nodeList.item(i).getTextContent());
                System.out.println("Player row = " + row);
            }
            else if (nodeList.item(i).getNodeName().equals("playerCol")) {
                col = parseInt(nodeList.item(i).getTextContent());
                System.out.println("Player col = " + col);
            }
        }

        assert(row!=-1);
        assert(col!=-1);

        Room playerRoom = board[row][col];
        Player player = new Player(playerRoom, (AccessibleTile) playerRoom.getTile(5,5)); //TODO: Hard coded for now

        Element playerElement = (Element) playerNode;
        NodeList inventory = playerElement.getElementsByTagName("inventory");

        parseItems(inventory, player.getInventory());

        return player;
    }

    private static void parseItems(NodeList items, List<Token> tokens) {
        for(int i = 0; i< items.getLength(); i++){
            assert(items.item(i).getNodeName().equals("item"));
            String token = items.item(i).getTextContent();
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
