package gameworld;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private AccessibleTile tile;
    private List<Token> inventory;
    private Room room;

    public Player(Room room, AccessibleTile tile) {
        this.room = room;
        this.tile = tile;
        this.inventory = new ArrayList<>();
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
//    AccessibleTile tile = (AccessibleTile) this.tile;
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

  public Tile getTile() {
    return tile;
  }

  public void setTile(AccessibleTile tile) {
    this.tile = tile;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public List<Token> getInventory() {
    return inventory;
  }

  public void pickUp(Token item) {
    this.inventory.add(item);
  }

}
