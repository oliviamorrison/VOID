package persistence;

import gameworld.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class XMLReader {

    public static Game parseGame() {
        while (true) {
            JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
            int res = chooser.showOpenDialog(null);
            if (res != JFileChooser.APPROVE_OPTION) {
                break;
            }
            Game game = parseFile(chooser.getSelectedFile());
//            System.out.println("Parsing completed");
            if (game != null) {
                System.out.println("game parsed");
                return game;
            }
            System.out.println("=================");
        }
        System.out.println("Done");
        return null;
    }

    /**
     * Top level parse method, called by the RobotParser.World
     */
    static Game parseFile(File code) {
        Scanner scan = null;
        try {
            scan = new Scanner(code);

            // the only time tokens can be next to each other is
            // when one of them is one of (){},;
            scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

            Game game = parseGame(scan); // You need to implement this!!!

//            scan.close();
            return game;
        } catch (FileNotFoundException e) {
            System.out.println("RobotParser.Robot program source file not found");
        }
        return null;
    }

    private static Pattern NUMPAT = Pattern.compile("-?(0|[1-9][0-9]*)");
    private static Pattern DIRPAT = Pattern.compile("right|left|top|bottom");

    /**
     * GAME ::= "<game>" "<rows>" NUM "</rows>" "<cols>" NUM "</cols>"
     *          "<room>" ROOM "</room>"+ "<player>" PLAYER "</player>" "</game>"
     * @param scan scanner
     * @return new game
     */
    private static Game parseGame(Scanner scan) {
        require("<game>", "Expected <game>", scan);

        //rows
        require("<rows>", "Expected <rows>", scan);
        int rows = requireInt(NUMPAT, "Expected number", scan);
        require("</rows>", "Expected </rows>", scan);

        //cols
        require("<cols>", "Expected <cols>", scan);
        int cols = requireInt(NUMPAT, "Expected number", scan);
        require("</cols>", "Expected </cols>", scan);

        Room[][] board = new Room[rows][cols];

        //rooms
        while(true){
            if(scan.hasNext("<room>")){
                parseRoom(scan, board);
            }
            else break;
        }

        require("<player>", "Expected <player>", scan);
        Player player = parsePlayer(scan, board); //TODO: This is hardcoded for now
        require("</player>", "Expected </player>", scan);


        Game game = new Game(board, player);

        require("</game>", "Expected </game>", scan);
        return game;
    }

    /**
     * ROOM ::= "<room>" "<row>"NUM"</row>" "<col>"NUM"</col>"
     *          DOOR+ ITEM+ "</room>"
     * @param scan scanner
     * @param board board
     */
    private static void parseRoom(Scanner scan, Room[][] board) {
        require("<room>", "Expected <room>", scan);

        require("<row>", "Expected <row>", scan);
        int row = requireInt(NUMPAT, "Expected number", scan);
        require("</row>", "Expected </row>", scan);

        require("<col>", "Expected <col>", scan);
        int col = requireInt(NUMPAT, "Expected number", scan);
        require("</col>", "Expected </col>", scan);


        List<String> doors = new ArrayList<>();
        while(true){
            if(scan.hasNext("<door>")){
                parseDoor(scan, doors);
            }
            else break;
        }

        List<Token> items = new ArrayList<>();
        while(true){
            if(scan.hasNext("<item>")){
                Token item = parseItem(scan);
                if(item!=null) items.add(item);
            }
            else break;
        }

        board[row][col] = new Room(doors, items);

        require("</room>", "Expected </room>", scan);

    }

    /**
     * PLAYER ::= INVENTORY
     * @param scan scanner
     * @return player in room
     */
    private static Player parsePlayer(Scanner scan, Room[][] board) {
        require("<roomrow>", "Expected <roomrow>", scan);
        int roomrow = requireInt(NUMPAT, "Expected number", scan);
        require("</roomrow>", "Expected </roomrow>", scan);

        require("<roomcol>", "Expected <roomcol>", scan);
        int roomcol = requireInt(NUMPAT, "Expected number", scan);
        require("</roomcol>", "Expected </roomcol>", scan);

        Room room = board[roomrow][roomcol];

        Player player = new Player(room, (AccessibleTile) room.getTile(5, 5)); //TODO: THIS IS HARD CODED IN THE CENTRE FOR NOW
        ((AccessibleTile) room.getTile(5,5)).setPlayer(true);

        if(scan.hasNext("<inventory>")) parseInventory(scan, player);
        return player;
    }

    /**
     * INVENTORY ::= "<inventory>" ITEM* "</inventory>"
     * @param scan scanner
     * @param player player
     */
    private static void parseInventory(Scanner scan, Player player) {
        require("<inventory>", "Expected <inventory>", scan);
        while (true){
            if(scan.hasNext("<item>")){
                Token item = parseItem(scan);
                player.getInventory().add(item);
            }
            else break;
        }
        require("</inventory>", "Expected </inventory>", scan);
    }

    /**
     * ITEM ::= "<item>" "diffuser|key|prize|coin|bomb" "</item>"
     * @param scan scanner
     * @return item
     */
    private static Token parseItem(Scanner scan) {
        require("<item>", "Expected <item>", scan);
        Token item = null;

        if(checkFor("diffuser", scan)) item =new Diffuser();
        else if(checkFor("key", scan)) item = new Key();
        else if(checkFor("prize", scan)) item =new Prize();
        else if(checkFor("coin", scan)) item =new Coin();
        else if(checkFor("bomb", scan)) item =new Bomb();

        //Can add more items, and change item names later
        require("</item>", "Expected </item>", scan);
        return item; //to make compiler happy
    }

    /**
     * DOOR ::= "<door>" "top|bottom|left|right" "</door>"
     * @param scan scanner
     * @param doors map of doors
     */
    private static void parseDoor(Scanner scan, List<String> doors){
        require("<door>", "Expected <door>", scan);
        String direction = require(DIRPAT, "Expected direction", scan);
        require("</door>", "Expected </door>", scan);
        doors.add(direction);
    }

    /**
     * Report a failure in the parser.
     */
    static void fail(String message, Scanner s) {
        String msg = message + "\n   @ ...";
        for (int i = 0; i < 5 && s.hasNext(); i++) {
            msg += " " + s.next();
        }
        try {
            throw new Exception(msg + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Requires that the next token matches a pattern if it matches, it consumes
     * and returns the token, if not, it throws an exception with an error
     * message
     */
    static String require(String p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    static String require(Pattern p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    /**
     * Requires that the next token matches a pattern (which should only match a
     * number) if it matches, it consumes and returns the token as an integer if
     * not, it throws an exception with an error message
     */
    static int requireInt(String p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    static int requireInt(Pattern p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    /**
     * Checks whether the next token in the scanner matches the specified
     * pattern, if so, consumes the token and return true. Otherwise returns
     * false without consuming anything.
     */
    static boolean checkFor(String p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }

    static boolean checkFor(Pattern p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }



}
