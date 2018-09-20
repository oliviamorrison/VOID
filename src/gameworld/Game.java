package gameworld;

import persistence.RoomParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {

  private static Player player;
  private Room currentRoom;
  private List<Room> rooms;

  public Game() {
    // start the player in the centre of the room
    // this.player = new Player(new Point (5,5));
    this.rooms = new ArrayList<>();
    setupTest();
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

  public void setupTest() {
    // create a starting room for testing
    Room defaultRoom = new RoomParser().createRoom();
    currentRoom = defaultRoom;
    rooms.add(defaultRoom);
    AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
    player = new Player(defaultRoom, startingTile);
    startingTile.setPlayer(true);
    startGame();
  }

  public void startGame() {

    while (true) {

      currentRoom.draw();
      String dir = inputString("Direction: ");
      movePlayer(dir);

    }
  }

  public static void pickUpItem() {
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (currentTile.hasToken()) {
      player.pickUp(currentTile.getToken());
      currentTile.setToken(null);
    }
  }

  public static void dropItem() {
    List<Token> inventory = player.getInventory();
    AccessibleTile currentTile = (AccessibleTile) player.getTile();
    if (!currentTile.hasToken() && !inventory.isEmpty()) {
      currentTile.setToken(player.getInventory().remove(0));
    }
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

  public static void main(String[] args) {
    new Game();
  }
}
