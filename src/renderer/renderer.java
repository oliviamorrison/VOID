package renderer;

import gameworld.AccessibleTile;
import gameworld.Player;
import gameworld.Room;
import gameworld.Tile;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class renderer {

    private Player player;
    private Room room;
    private List<Polygon> tilePolygons;

    public renderer(Player player, Room room){
        this.player = player;
        this.room = room;
    }

    public void draw(){
        setTilePolygons();
        tilesToPolygonList();
        twoDToIso();
        setPlayerPos();
        Group root = new Group();
        root.getChildren().addAll(this.tilePolygons);
        root.getChildren().add(this.player.getEllipse());
    }

    public void setTilePolygons() {
        int size = 10;
        double polySize = 20;
        double top = 100;
        double left = 100;
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                List<Double> points = new ArrayList<Double>();
                double p1y = top + (row * polySize);
                double p1x = left + (col * polySize);

                double p2y = top + (row * polySize);
                double p2x = left + (col * polySize) + polySize;

                double p3y = top + (row * polySize) + polySize;
                double p3x = left + (col * polySize) + polySize;

                double p4y = top + (row * polySize) + polySize;
                double p4x = left + (col * polySize);

                points.add(p1x);
                points.add(p1y);
                points.add(p2x);
                points.add(p2y);
                points.add(p3x);
                points.add(p3y);
                points.add(p4x);
                points.add(p4y);

                Polygon poly = new Polygon();
                poly.getPoints().addAll(points);
                this.room.getTile(row, col).setTilePolygon(poly);
                if (this.room.getTile(row, col) instanceof AccessibleTile) {
                    AccessibleTile tile = (AccessibleTile) this.room.getTile(row, col);
                    if (tile.hasToken()) {
                        poly.setFill(Color.BLUE);
                    } else if (tile.hasBomb()) {
                        poly.setFill(Color.RED);
                    } else {
                        poly.setFill(Color.TRANSPARENT);
                    }
                } else {
                    poly.setFill(Color.GREEN);
                }

                poly.setStroke(Color.BLACK);
                poly.setStrokeWidth(1);
            }
        }
    }
    public void tilesToPolygonList() {
        this.tilePolygons = new ArrayList<Polygon>();
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                this.tilePolygons.add(this.room.getTile(row, col).getTilePolygon());
            }
        }
    }

    public void twoDToIso() {
        for (Polygon p : this.tilePolygons) {
            for (int i = 0; i < p.getPoints().size() - 1; i += 2) {
                double x = p.getPoints().get(i) - p.getPoints().get(i + 1);
                double y = (p.getPoints().get(i) + p.getPoints().get(i + 1)) / 2;

                p.getPoints().set(i, x + 300);
                p.getPoints().set(i + 1, y);
            }
        }
    }
    public void setPlayerPos() {
        Point2D p = this.room.getPlayerTile().getCenter();
        this.player.getEllipse().setCenterX(p.getX());
        this.player.getEllipse().setCenterY(p.getY() - 13);
        System.out.println("AGAIN");
    }
}

