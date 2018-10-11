package gameworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import renderer.PolygonBlock;

public abstract class Tile {

//  private Room room;
  private PolygonBlock polygon;
  private int x;
  private int y;

  public Tile( int x, int y) {
    this.x = x;
    this.y = y;
//    this.room = r;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

//  public Room getRoom() {
//    return room;
//  }

  public PolygonBlock getTilePolygon() {
    return this.polygon;
  }

  public void setTilePolygon(PolygonBlock poly) {
    this.polygon = poly;
  }

}
