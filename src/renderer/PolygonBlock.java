package renderer;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonBlock {
    //testting
    private double originX;
    private double originY;
    private double z;
    private double center = 350;

    private final double tileWidth = 80;
    private final double tileHeight = 40;
    private final double strokeWidth = 1;
    private final Color strokeColor = Color.GRAY;

    private List<Polygon> polygons;

    private Polygon top;
    private double tpx1, tpy1;
    private double tpx2, tpy2;
    private double tpx3, tpy3;
    private double tpx4, tpy4;
    private Color topColor;
    private List<Double> topPoints;

    private Polygon left;
    private double lpx1, lpy1;
    private double lpx2, lpy2;
    private double lpx3, lpy3;
    private double lpx4, lpy4;
    private Color leftColor;
    private List<Double> leftPoints;

    private Polygon right;
    private double rpx1, rpy1;
    private double rpx2, rpy2;
    private double rpx3, rpy3;
    private double rpx4, rpy4;
    private Color rightColor;
    private List<Double> rightPoints;

    public PolygonBlock(double x, double y, double z, Color c) {
        originX = ((x - y) * tileWidth / 2) + center;
        originY = (x + y) * tileHeight / 2;
        this.z = z;
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
        // NORTH
        tpx1 = (0) + originX;
        tpy1 = (-z * tileHeight) + originY;
        tpx2 = (tileWidth / 2) + originX;
        tpy2 = (tileHeight / 2 - z * tileHeight) + originY;
        tpx3 = (0) + originX;
        tpy3 = (tileHeight - z * tileHeight) + originY;
        tpx4 = (-tileWidth / 2) + originX;
        tpy4 = (tileHeight / 2 - z * tileHeight) + originY;
        topPoints = new ArrayList<>();
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
        top.setStroke(strokeColor);
        top.setStrokeWidth(strokeWidth);
        polygons.add(top);
    }

    public void drawLeft() {
        lpx1 = (-tileWidth / 2) + originX;
        lpy1 = (tileHeight / 2 - z * tileHeight) + originY;
        lpx2 = (0) + originX;
        lpy2 = (tileHeight - z * tileHeight) + originY;
        lpx3 = (0) + originX;
        lpy3 = (tileHeight) + originY;
        lpx4 = (-tileWidth / 2) + originX;
        lpy4 = (tileHeight / 2) + originY;
        leftPoints = new ArrayList<>();
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
        left.setStroke(strokeColor);
        left.setStrokeWidth(strokeWidth);
        polygons.add(left);

    }

    public void drawRight() {
        rpx1 = (tileWidth / 2) + originX;
        rpy1 = (tileHeight / 2 - z * tileHeight) + originY;
        rpx2 = (0) + originX;
        rpy2 = (tileHeight - z * tileHeight) + originY;
        rpx3 = (0) + originX;
        rpy3 = (tileHeight) + originY;
        rpx4 = (tileWidth / 2) + originX;
        rpy4 = (tileHeight / 2) + originY;
        rightPoints = new ArrayList<>();
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
        right.setStroke(strokeColor);
        right.setStrokeWidth(strokeWidth);
        polygons.add(right);
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}

