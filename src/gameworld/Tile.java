package gameworld;

public abstract class Tile {
    private Room room;

    public Tile(Room r){
        this.room = r;
    }

    public Room getRoom() {
        return room;
    }
}
