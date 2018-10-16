package renderer;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonBlock {

  private static final double tileWidth = 64;
  private static final double tileHeight = 32;
  private static final double strokeWidth = 1;
  private static final Color strokeColor = Color.rgb(80, 80, 80);

  private double originX;
  private double originY;
  private double zval;
  private double center = 350;
  private List<Polygon> polygons;

  private Polygon top;
  private Color topColor;

  private Polygon left;
  private Color leftColor;

  private Polygon right;
  private Color rightColor;

  public PolygonBlock(double x, double y, double z, Color c) {
    originX = ((x - y) * tileWidth / 2) + center;
    originY = (x + y) * tileHeight / 2;
    this.zval = z;
    this.topColor = c;
    this.leftColor = c.darker();
    this.rightColor = c.darker().darker();

    polygons = new ArrayList<>();
    top = new Polygon();
    left = new Polygon();
    right = new Polygon();

    drawTop();
    drawLeft();
    drawRight();
  }

  public void drawTop() {

    List<Double> topPoints = new ArrayList<>();
    // NORTH
    double tpx1 = (0) + originX;
    double tpy1 = (-zval * tileHeight) + originY;
    double tpx2 = (tileWidth / 2) + originX;
    double tpy2 = (tileHeight / 2 - zval * tileHeight) + originY;
    double tpx3 = (0) + originX;
    double tpy3 = (tileHeight - zval * tileHeight) + originY;
    double tpx4 = (-tileWidth / 2) + originX;
    double tpy4 = (tileHeight / 2 - zval * tileHeight) + originY;
    topPoints.add(tpx1);
    topPoints.add(tpy1);
    topPoints.add(tpx2);
    topPoints.add(tpy2);
    topPoints.add(tpx3);
    topPoints.add(tpy3);
    topPoints.add(tpx4);
    topPoints.add(tpy4);

    top.getPoints().addAll(topPoints);
    top.setFill(this.topColor);
    top.setStroke(topColor);

    top.setStrokeWidth(strokeWidth);
    polygons.add(top);
  }

  public void drawLeft() {
    List<Double> leftPoints = new ArrayList<>();
    double lpx1 = (-tileWidth / 2) + originX;
    double lpy1 = (tileHeight / 2 - zval * tileHeight) + originY;
    double lpx2 = (0) + originX;
    double lpy2 = (tileHeight - zval * tileHeight) + originY;
    double lpx3 = (0) + originX;
    double lpy3 = (tileHeight) + originY;
    double lpx4 = (-tileWidth / 2) + originX;
    double lpy4 = (tileHeight / 2) + originY;
    leftPoints.add(lpx1);
    leftPoints.add(lpy1);
    leftPoints.add(lpx2);
    leftPoints.add(lpy2);
    leftPoints.add(lpx3);
    leftPoints.add(lpy3);
    leftPoints.add(lpx4);
    leftPoints.add(lpy4);

    left.getPoints().addAll(leftPoints);
    left.setFill(this.leftColor);
    left.setStroke(leftColor);
    left.setStrokeWidth(strokeWidth);
    polygons.add(left);

  }

  public void drawRight() {
    List<Double> rightPoints = new ArrayList<>();
    double rpx1 = (tileWidth / 2) + originX;
    double rpy1 = (tileHeight / 2 - zval * tileHeight) + originY;
    double rpx2 = (0) + originX;
    double rpy2 = (tileHeight - zval * tileHeight) + originY;
    double rpx3 = (0) + originX;
    double rpy3 = (tileHeight) + originY;
    double rpx4 = (tileWidth / 2) + originX;
    double rpy4 = (tileHeight / 2) + originY;
    rightPoints.add(rpx1);
    rightPoints.add(rpy1);
    rightPoints.add(rpx2);
    rightPoints.add(rpy2);
    rightPoints.add(rpx3);
    rightPoints.add(rpy3);
    rightPoints.add(rpx4);
    rightPoints.add(rpy4);

    right.getPoints().addAll(rightPoints);
    right.setFill(this.rightColor);
    right.setStroke(rightColor);
    right.setStrokeWidth(strokeWidth);
    polygons.add(right);
  }

  public List<Polygon> getPolygons() {
    return polygons;
  }
}

