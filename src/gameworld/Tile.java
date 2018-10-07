package gameworld;

import javafx.scene.shape.Polygon;

public abstract class Tile {
    private Room room;
    private Polygon polygon;
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

    public Polygon getTilePolygon() {
        return this.polygon;
    }

    public void setTilePolygon(Polygon poly) {
        this.polygon = poly;
    }
}
