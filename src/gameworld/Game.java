package gameworld;

//import persistence.RoomParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {

  private Room[][] board;
  private static Player player;
  private Room currentRoom;

    public Game(boolean testing) {
        setup();
    }
  public Game(Room[][] board, Player player){
    this.player = player;
    this.board = board;
    this.currentRoom = player.getRoom();
  }

    public static void movePlayer(String direction) {
        int dx = 0;
        int dy = 0;

        switch (direction) {
            case "w":
                dx = -1;
                break;
            case "a":
                dy = -1;
                break;
            case "s":
                dx = 1;
                break;
            case "d":
                dy = 1;
                break;
            default:

        }

        player.moveTile(dx, dy);
    }

//  public void setup() {
//    // create a starting room for testing
//    Room defaultRoom = RoomParser.createRoom(RoomParser.getBombRoom());
//    currentRoom = defaultRoom;
//    rooms.add(defaultRoom);
//    AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
//    player = new Player(defaultRoom, startingTile);
//    startingTile.setPlayer(true);
//  }

  public void startGame() {
//    intialiseDoors();
    while (true) {
      currentRoom.draw();
      startTurn();
    }
  }

    public void startTurn() {
        String input = inputString("Move:m Pickup:u Drop:d Diffuse: f");
        switch (input) {
            case "m":
                movePlayer();
                break;
            case "u":
                pickUpItem();
                break;
            case "d":
                dropItem();
                break;
            case "f":
                diffuseBomb();
                break;
        }
    }

    // for testing purposes
    public String drawRoom() {
        return currentRoom.draw();
    }

    // for testing purposes
    public Player getPlayer() {
        return player;
    }

    public void movePlayer() {
        String dir = inputString("Direction: ");
        movePlayer(dir);
    }

    public void pickUpItem() {
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (currentTile.hasToken()) {
            player.pickUp(currentTile.getToken());
            currentTile.setToken(null);
        }
    }

    public void dropItem() {
        List<Token> inventory = player.getInventory();
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (!currentTile.hasToken() && !inventory.isEmpty()) {
            currentTile.setToken(player.getInventory().remove(0));
        }
    }

    public void diffuseBomb() {
        AccessibleTile t = (AccessibleTile) player.getTile();
        if(!t.hasBomb()) {
            System.out.println("No Bomb here.");
            return;
        }
        List<Token> inventory = player.getInventory();
        Boolean hasdiffuser = false;
        for(Token token : inventory) {
            if(token instanceof Diffuser)
                hasdiffuser = true;
        }
        if(!hasdiffuser) {
            System.out.println("You need a diffuser to diffuse the bomb.");
            return;
        }
        System.out.println("You diffused the bomb." + t.getBomb().isActive());
        t.getBomb().setActive(false);
    }

    private static String inputString(String msg) {
        System.out.print(msg + " ");
        while (true) {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                return input.readLine();
            } catch (IOException e) {
                System.out.println("I/O Error ... please try again!");
            }
        }
    }

  //TODO: Clean this up
  private void intialiseDoors(){
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        if(board[i][j]!=null) { //TODO: fix null rooms
            for (String dir : board[i][j].getDoors()) {
                switch (dir) {
                    case "left":
                        board[i][j].setTile(new DoorTile(board[i - 1][j], board[i][j], i, j), Room.LEFT.x, Room.LEFT.y);
                        break;
                    case "right":
                        board[i][j].setTile(new DoorTile(board[i + 1][j], board[i][j], i, j), Room.RIGHT.x, Room.RIGHT.y);
                        break;
                    case "top":
                        board[i][j].setTile(new DoorTile(board[i][j - 1], board[i][j], i, j), Room.TOP.x, Room.TOP.y);
                        break;
                    case "bottom":
                        board[i][j].setTile(new DoorTile(board[i][j + 1], board[i][j], i, j), Room.BOTTOM.x, Room.BOTTOM.y);
                        break;
                }
            }


        }
      }
    }

  }

  public Room[][] getBoard() {
    return board;
  }
}

