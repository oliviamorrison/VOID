package gameworld;

import javafx.scene.shape.Polygon;

public abstract class Tile {
    private Room room;
    private Polygon polygon;

    public Tile(Room r){
        this.room = r;
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
