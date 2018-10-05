package gameworld;

public abstract class Tile {
    private Room room;
    private int x;
    private int y;

    public Tile(Room r, int x, int y){
        this.x = x;
        this.y = y;
        this.room = r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Room getRoom() {
        return room;
    }
}
