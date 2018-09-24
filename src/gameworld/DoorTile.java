package gameworld;

public class DoorTile extends Tile{
    private Room toRoom;

    public DoorTile(Room toRoom, Room room){
        super(room);
        this.toRoom = toRoom;
    }

    public Room getToRoom() {
        return toRoom;
    }
}
