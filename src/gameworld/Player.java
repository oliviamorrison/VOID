package gameworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private Point location;
	private List<Token> inventory;

	
	public Player(Point location) {
		this.location = location;
		this.inventory = new ArrayList<>();
	}

	/**
	 * Updates the player's current position on the board.
	 * @param dx amount of steps moved in the x direction
	 * @param dy amount of steps moved in the y direction
	 */
	public void move(int dx, int dy) {
		location.translate(dx, dy);
		System.out.println(location.toString());
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void pickUp(Token item) {
		this.inventory.add(item);
	}
}
