package gameworld;

import renderer.PolygonBlock;

public abstract class Tile {

  private int row;
  private int col;
  private PolygonBlock polygon;

  public Tile(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public PolygonBlock getTilePolygon() {
    return this.polygon;
  }

  public void setTilePolygon(PolygonBlock poly) {
    this.polygon = poly;
  }

}
