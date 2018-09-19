package gameworld;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
	private static Player player;

	public Game(){
		// start the player in the centre of the room
//		this.player = new Player(new Point (5,5));
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static void movePlayer(String direction) {
		int dx = 0;
		int dy = 0;

		switch(direction) {
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
		}
	
		player.move(dx, dy);
	}
	
	public static void pickUpItem() {
		if(player.getTile().hasToken()) {
			player.pickUp(player.getTile().getToken());
			player.getTile().setToken(null);
		}
	}
	
	public static void dropItem() {
		if(!player.getInventory().isEmpty()) {
			player.getTile().setToken(player.getInventory().remove(0));
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

	public static void main(String[] args){
		new Game();

		while(true) {
			String dir = inputString("Direction: ");
			movePlayer(dir);
		}
	}
}
