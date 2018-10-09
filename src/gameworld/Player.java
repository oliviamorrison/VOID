package gameworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private AccessibleTile tile;
  private List<Item> inventory;
  private Room room;
  private Ellipse ellipse;

  public Player(Room room, AccessibleTile tile) {
    this.room = room;
    this.tile = tile;
    this.inventory = new ArrayList<>();

    this.ellipse = new Ellipse();
    this.ellipse.setFill(Color.ORANGE);
    this.ellipse.setRadiusX(8);
    this.ellipse.setRadiusY(15);

  }

  public Room getRoom() {
    return room;
  }
//	/**
//	 * Updates the player's current position on the board.
//	 * @param dx amount of steps moved in the x direction
//	 * @param dy amount of steps moved in the y direction
//	 */
//	public void move(int dx, int dy) {
//
//
//		if(!moveTile(dx, dy)) {
//			System.out.println("Couldn't move tile");
//			return;
//		}
//		if(tile instanceof DoorTile){
//			//This does nothing for now
//			System.out.println("You are on a door tile. Do you want to go through the door?");
//			//If the room the player is going to has a bomb, do not enter that room
//			if(((DoorTile) tile).getToDoor().getRoom().checkActiveBomb()){
//				System.out.println("The room you are moving to has a bomb, you must get a bomb diffuser before you can enter the room");
//			}
//			else {
//				//TODO: Edit this so that the player changes which room it's in and it's location accordingly
//				location.translate(dx, dy);
//				return;
//			}
//
//		}
//		location.translate(dx, dy);
//
//		System.out.println(location.toString());
//	}

  public void moveTile(int dx, int dy) {

    if (room.moveTile(tile, dx, dy) == null) {
      return;
    }
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

  public AccessibleTile getTile() {
    return tile;
  }

  public void setTile(AccessibleTile tile) {
    this.tile = tile;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public List<Item> getInventory() {
    return inventory;
  }

  public void removeItem(Item item) {
    inventory.remove(item);
  }

  public void addItem(Item item) {
    inventory.add(item);
  }

  public void pickUp(Item item) {
    this.inventory.add(item);
  }

  public Ellipse getEllipse() {
    return this.ellipse;
  }

  public void setEllipse(Ellipse e) {
    this.ellipse = e;
  }

}
