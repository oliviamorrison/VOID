package renderer;

import gameworld.AccessibleTile;
import gameworld.Player;
import gameworld.Room;
import gameworld.Item;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import persistence.RoomParser;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private static List<Polygon> poly;
    public static Player player;
    private static Room currentRoom;
    private static List<Room> rooms = new ArrayList<>();
    private static Group root;
    private static final double POLYSIZE = 40;
    private static final double TOP = 0;
    private static final double LEFT = 0;

    public Renderer() {
        setUpGame();
        createPolygons();
        polygonsToList();
		twoDToIso();
        setPlayerPos();
        root = new Group();
        root.getChildren().addAll(poly);
        root.getChildren().add(player.getEllipse());
    }

    public Group getRoot() {
        return root;
    }

    public void pickUpItem() {
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (currentTile.hasItem()) {
            player.pickUp(currentTile.getItem());
            currentTile.setItem(null);
        }
    }

    public void dropItem() {
        List<Item> inventory = player.getInventory();
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (!currentTile.hasItem() && !inventory.isEmpty()) {
            currentTile.setItem(player.getInventory().remove(0));
        }
    }

    public void rotate() {
<<<<<<< src/renderer/Renderer.java
        if(!this.poly.isEmpty()) {
            poly.clear();
            System.out.println(poly.size());
        }
        currentRoom.rotateRoomClockwise();
        setTilePolygons();
        tilesToPolygonList();
        twoDToIso();
        System.out.println("");
=======
        if (!poly.isEmpty()) {
            poly.clear();
        }
        currentRoom.rotateRoomClockwise();
        createPolygons();
        polygonsToList();
		twoDToIso();
>>>>>>> src/renderer/Renderer.java
    }

    public void setUpGame() {
        // create a starting room for testing
        Room defaultRoom = RoomParser.createRoom(RoomParser.getBombRoom());
        currentRoom = defaultRoom;
        rooms.add(defaultRoom);
        AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
        player = new Player(defaultRoom, startingTile);
        startingTile.setPlayer(true);
    }

<<<<<<< src/renderer/Renderer.java
    public void setTilePolygons() {
        System.out.println("setTilePolygons");
        double polySize = 20;
        double top = 100;
        double left = 100;
=======
    public void createPolygons() {
>>>>>>> src/renderer/Renderer.java
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                List<Double> points = new ArrayList<Double>();
                double p1y = TOP + (row * POLYSIZE);
                double p1x = LEFT + (col * POLYSIZE);

                double p2y = TOP + (row * POLYSIZE);
                double p2x = LEFT + (col * POLYSIZE) + POLYSIZE;

                double p3y = TOP + (row * POLYSIZE) + POLYSIZE;
                double p3x = LEFT + (col * POLYSIZE) + POLYSIZE;

                double p4y = TOP + (row * POLYSIZE) + POLYSIZE;
                double p4x = LEFT + (col * POLYSIZE);

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
                currentRoom.getTile(row, col).setTilePolygon(poly);
                if (currentRoom.getTile(row, col) instanceof AccessibleTile) {
                    AccessibleTile tile = (AccessibleTile) currentRoom.getTile(row, col);
                    if (tile.hasItem()) {
                        poly.setFill(Color.BLUE);
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

<<<<<<< src/renderer/Renderer.java
    public void tilesToPolygonList() {
        System.out.println("tilesToPolygonList");
=======
    public void polygonsToList() {
>>>>>>> src/renderer/Renderer.java
        poly = new ArrayList<Polygon>();
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                poly.add(currentRoom.getTile(row, col).getTilePolygon());
            }
        }
    }

    public void setPlayerPos() {
        Point2D p = currentRoom.getPlayerTile().getCenter();
        player.getEllipse().setCenterX(p.getX());
        player.getEllipse().setCenterY(p.getY() - 13);
    }

    public void twoDToIso() {
        for (Polygon p : poly) {
            for (int i = 0; i < p.getPoints().size() - 1; i += 2) {
                double x = p.getPoints().get(i) - p.getPoints().get(i + 1);
                double y = (p.getPoints().get(i) + p.getPoints().get(i + 1)) / 2;

                p.getPoints().set(i, x+300);
                p.getPoints().set(i + 1, y);
            }
        }
    }


}
