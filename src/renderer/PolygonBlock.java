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

    private final double tileWidth = 64;
    private final double tileHeight = 32;
    private final double strokeWidth = 1;
    private final Color strokeColor = Color.rgb(80, 80, 80);

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
        double tpx1, tpy1, tpx2, tpy2, tpx3, tpy3, tpx4, tpy4;
        List<Double> topPoints = new ArrayList<>();
        // NORTH
        tpx1 = (0) + originX;
        tpy1 = (-z * tileHeight) + originY;
        tpx2 = (tileWidth / 2) + originX;
        tpy2 = (tileHeight / 2 - z * tileHeight) + originY;
        tpx3 = (0) + originX;
        tpy3 = (tileHeight - z * tileHeight) + originY;
        tpx4 = (-tileWidth / 2) + originX;
        tpy4 = (tileHeight / 2 - z * tileHeight) + originY;
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
        double lpx1, lpy1, lpx2, lpy2, lpx3, lpy3, lpx4, lpy4;
        List<Double>leftPoints = new ArrayList<>();
        lpx1 = (-tileWidth / 2) + originX;
        lpy1 = (tileHeight / 2 - z * tileHeight) + originY;
        lpx2 = (0) + originX;
        lpy2 = (tileHeight - z * tileHeight) + originY;
        lpx3 = (0) + originX;
        lpy3 = (tileHeight) + originY;
        lpx4 = (-tileWidth / 2) + originX;
        lpy4 = (tileHeight / 2) + originY;
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
        double rpx1, rpy1, rpx2, rpy2, rpx3, rpy3, rpx4, rpy4;
        List<Double>rightPoints = new ArrayList<>();
        rpx1 = (tileWidth / 2) + originX;
        rpy1 = (tileHeight / 2 - z * tileHeight) + originY;
        rpx2 = (0) + originX;
        rpy2 = (tileHeight - z * tileHeight) + originY;
        rpx3 = (0) + originX;
        rpy3 = (tileHeight) + originY;
        rpx4 = (tileWidth / 2) + originX;
        rpy4 = (tileHeight / 2) + originY;
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

