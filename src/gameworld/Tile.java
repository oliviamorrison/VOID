package gameworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import renderer.PolygonBlock;

public abstract class Tile {

  private Room room;
  private PolygonBlock polygon;
  private int x;
  private int y;
  private Color color;
  private double height;

  public Tile(Room r, int x, int y, Color c, double h) {
    this.x = x;
    this.y = y;
    this.room = r;
    this.color = c;
    this.height = h;
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

  public PolygonBlock getTilePolygon() {
    return this.polygon;
  }

  public void setTilePolygon(PolygonBlock poly) {
    this.polygon = poly;
  }

  public Color getColor(){
    return this.color;
  }

  public double getHeight(){
    return this.height;
  }

}
