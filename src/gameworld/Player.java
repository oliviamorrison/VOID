package gameworld;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private Room room;
    private AccessibleTile tile;
    private List<Token> inventory;

	public Player(Room room, AccessibleTile tile) {
		this.room = room;
		this.tile = tile;
		this.inventory = new ArrayList<>();
	}

	/**
	 * Updates the player's current position on the board.
	 */
//	public void move(int dx, int dy) {
//		location.translate(dx, dy);
//		System.out.println(location.toString());
//	}

//	public void moveTile(int dx, int dy){
//	    tile = tile.getRoom().moveTile(tile,dx,dy);
//    }

    public Tile getTile(){
	    return tile;
    }



	public void pickUp(Token item) {
		this.inventory.add(item);
	}

	public List<Token> getInventory() {
		return inventory;
	}



  public void moveTile(int dx, int dy) {
    tile.setPlayer(false);
    tile = (AccessibleTile) room.moveTile(tile, dx, dy);
    tile.setPlayer(true);
  }

}
