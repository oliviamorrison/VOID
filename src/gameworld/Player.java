package gameworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Player {

	private Point location;
	private Tile tile;
	private List<Token> inventory;


	public Player(Point location, Tile tile) {
		this.location = location;
		this.tile = tile;
		this.inventory = new ArrayList<>();
	}

	/**
	 * Updates the player's current position on the board.
	 * @param dx amount of steps moved in the x direction
	 * @param dy amount of steps moved in the y direction
	 */
	public void move(int dx, int dy) {


		if(!moveTile(dx, dy)) {
			System.out.println("Couldn't move tile");
			return;
		}
		if(tile instanceof DoorTile){
			//This does nothing for now
			System.out.println("You are on a door tile. Do you want to go through the door?");
			//If the room the player is going to has a bomb, do not enter that room
			if(((DoorTile) tile).getToDoor().getRoom().checkActiveBomb()){
				System.out.println("The room you are moving to has a bomb, you must get a bomb diffuser before you can enter the room");
			}
			else {
				//TODO: Edit this so that the player changes which room it's in and it's location accordingly
				location.translate(dx, dy);
				return;
			}

		}
		location.translate(dx, dy);

		System.out.println(location.toString());
	}

	public boolean moveTile(int dx, int dy){
		Tile newTile = tile.getRoom().moveTile(tile,dx,dy);
		if(newTile == null) return false;
		tile = newTile;
		return true;
	}

	public Tile getTile(){
		return tile;
	}

	public AccessibleTile getAccessibleTile(){
		if(tile instanceof InaccessibleTile) return null;
		else return (AccessibleTile) tile;
	}

	public List<Token> getInventory() {
		return inventory;
	}

	public Point getLocation() {
		return location;
	}

	public void pickUp(Token item) {
		this.inventory.add(item);
	}

	public boolean diffuseBomb() {
		if(!(this.tile instanceof DoorTile)) {
			return false;
		}
		for(Token item : this.inventory) {
			if(item instanceof Diffuser) {
				return true;
			}
		}
		return false;
	}
}
