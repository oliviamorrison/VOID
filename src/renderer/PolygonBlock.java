package renderer;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * This is the PolygonBlock which represents the tiles in the game as visual
 * blocks in the game. A PolygonBlock consist of three polygon which is drawn
 * into a 3D block.
 *
 * @author James Del Puerto 300375073
 */
public class PolygonBlock {

  //Dimension of tile.
  private static final double tileWidth = 64;
  private static final double tileHeight = 32;
  //Stroke width of polygon.
  private static final double strokeWidth = 1;

  //Origin point of the graphics pane where polygon refer to.
  private double originX;
  private double originY;

  //Z value which helps draw the height of the polygon
  private double zedHeightValue;

  //Center of the screen
  private double center = 350;

  //Holds the three polygon which make up the PolygonBlock.
  private List<Polygon> polygons;

  //Stores the top polygon of the PolygonBlock.
  private Polygon top;
  private Color topColor;

  //Stores the left polygon of the PolygonBlock.
  private Polygon left;
  private Color leftColor;

  //Stores the right polygon of the PolygonBlock.
  private Polygon right;
  private Color rightColor;

  /**
   * Constructor initialises everything needed to draw the PolygonBlock.
   * @param x The x position of the PolygonBlock
   * @param y The y position of the PolygonBlock
   * @param z The z which helps draw the height of the PolygonBlock
   * @param c The color of the PolygonBlock
   */
  public PolygonBlock(double x, double y, double z, Color c) {
    //Calculates the origin.
    originX = ((x - y) * tileWidth / 2) + center;
    originY = (x + y) * tileHeight / 2;
    this.zedHeightValue = z;
    //Sets the color for the three polygons that makes up the PolygonBlock.
    this.topColor = c;
    this.leftColor = c.darker();
    this.rightColor = c.darker().darker();

    polygons = new ArrayList<>();

    //Creates new list for all three polygons.
    top = new Polygon();
    left = new Polygon();
    right = new Polygon();

    drawTop();
    drawLeft();
    drawRight();
  }

  /**
   * Draws the top polygon of the PolygonBlock.
   */
  public void drawTop() {
    //List to store all x and y points of top polygon.
    List<Double> topPoints = new ArrayList<>();
    //Calculates the position of the points.
    double tpx1 = (0) + originX;
    double tpy1 = (-zedHeightValue * tileHeight) + originY;
    double tpx2 = (tileWidth / 2) + originX;
    double tpy2 = (tileHeight / 2 - zedHeightValue * tileHeight) + originY;
    double tpx3 = (0) + originX;
    double tpy3 = (tileHeight - zedHeightValue * tileHeight) + originY;
    double tpx4 = (-tileWidth / 2) + originX;
    double tpy4 = (tileHeight / 2 - zedHeightValue * tileHeight) + originY;
    //Adds points to list of points.
    topPoints.add(tpx1);
    topPoints.add(tpy1);
    topPoints.add(tpx2);
    topPoints.add(tpy2);
    topPoints.add(tpx3);
    topPoints.add(tpy3);
    topPoints.add(tpx4);
    topPoints.add(tpy4);

    //Adds points to polygon list of points.
    top.getPoints().addAll(topPoints);

    //Styles the polygon.
    top.setFill(this.topColor);
    top.setStroke(topColor);
    top.setStrokeWidth(strokeWidth);

    //Adds polygon to list of polygons.
    polygons.add(top);
  }

  /**
   * Draws the left polygon of the PolygonBlock.
   */
  public void drawLeft() {
    //List to store all x and y points of left polygon.
    List<Double> leftPoints = new ArrayList<>();
    //Calculates the position of the points.
    double lpx1 = (-tileWidth / 2) + originX;
    double lpy1 = (tileHeight / 2 - zedHeightValue * tileHeight) + originY;
    double lpx2 = (0) + originX;
    double lpy2 = (tileHeight - zedHeightValue * tileHeight) + originY;
    double lpx3 = (0) + originX;
    double lpy3 = (tileHeight) + originY;
    double lpx4 = (-tileWidth / 2) + originX;
    double lpy4 = (tileHeight / 2) + originY;
    //Adds points to list of points.
    leftPoints.add(lpx1);
    leftPoints.add(lpy1);
    leftPoints.add(lpx2);
    leftPoints.add(lpy2);
    leftPoints.add(lpx3);
    leftPoints.add(lpy3);
    leftPoints.add(lpx4);
    leftPoints.add(lpy4);

    //Adds points to polygon list of points.
    left.getPoints().addAll(leftPoints);

    //Styles the polygon.
    left.setFill(this.leftColor);
    left.setStroke(leftColor);
    left.setStrokeWidth(strokeWidth);

    //Adds polygon to list of polygons.
    polygons.add(left);

  }

  /**
   * Draws the right polygon of the PolygonBlock.
   */
  public void drawRight() {
    //List to store all x and y points of right polygon.
    List<Double> rightPoints = new ArrayList<>();
    //Calculates the position of the points.
    double rpx1 = (tileWidth / 2) + originX;
    double rpy1 = (tileHeight / 2 - zedHeightValue * tileHeight) + originY;
    double rpx2 = (0) + originX;
    double rpy2 = (tileHeight - zedHeightValue * tileHeight) + originY;
    double rpx3 = (0) + originX;
    double rpy3 = (tileHeight) + originY;
    double rpx4 = (tileWidth / 2) + originX;
    double rpy4 = (tileHeight / 2) + originY;
    //Adds points to list of points
    rightPoints.add(rpx1);
    rightPoints.add(rpy1);
    rightPoints.add(rpx2);
    rightPoints.add(rpy2);
    rightPoints.add(rpx3);
    rightPoints.add(rpy3);
    rightPoints.add(rpx4);
    rightPoints.add(rpy4);

    //Adds points to polygon list of points.
    right.getPoints().addAll(rightPoints);

    //Styles the polygon.
    right.setFill(this.rightColor);
    right.setStroke(rightColor);
    right.setStrokeWidth(strokeWidth);

    //Adds polygon to list of polygons.
    polygons.add(right);
  }

  /**
   * Returns all three polygons that make up the PolygonBlock.
   * @return The list of three polygons
   */
  public List<Polygon> getPolygons() {
    return polygons;
  }
}

