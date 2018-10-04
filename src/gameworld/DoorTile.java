package gameworld;

public class DoorTile extends Tile{
    private Room toRoom;

    public DoorTile(Room room, Room toRoom, int x, int y) {
        super(room, x, y);
        this.toRoom = toRoom;
    }

    public Room getToRoom() {
        return toRoom;
    }
}
