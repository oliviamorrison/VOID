package gameworld;

import renderer.PolygonBlock;

public abstract class Tile {

  private int x;
  private int y;
  private PolygonBlock polygon;

  public Tile( int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public PolygonBlock getTilePolygon() {
    return this.polygon;
  }

  public void setTilePolygon(PolygonBlock poly) {
    this.polygon = poly;
  }

}
