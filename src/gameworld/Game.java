package gameworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Game {
  private static Player player;

  public Game() {
    // start the player in the centre of the room
    // this.player = new Player(new Point (5,5));
  }

  public Player getPlayer() {
    return player;
  }

  public static void movePlayer(String direction) {
    int dx = 0;
    int dy = 0;

    switch (direction) {
      case "w":
        dy = -1;
        break;
      case "a":
        dx = -1;
        break;
      case "s":
        dy = 1;
        break;
      case "d":
        dx = 1;
        break;
      default:

    }

    player.move(dx, dy);
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

    while (true) {
      String dir = inputString("Direction: ");
      movePlayer(dir);
    }
  }
}
