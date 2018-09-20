package gameworld;

public class DoorTile extends Tile{
    private DoorTile toDoor;

    public DoorTile(Room toRoom, Room room){
        super(room);
        this.toDoor = toDoor;
    }

    public DoorTile getToDoor() {
        return toDoor;
    }
}
