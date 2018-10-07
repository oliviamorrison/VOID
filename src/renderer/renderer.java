package renderer;
import java.util.ArrayList;
import java.util.List;

import gameworld.AccessibleTile;
import gameworld.Player;
import gameworld.Room;
import gameworld.Token;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import persistence.RoomParser;

public class renderer extends Application {

    private static List<Polygon> poly;
    private static Player player;
    private static Room currentRoom;
    private static List<Room> rooms = new ArrayList<>();
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // TODO Auto-generated method stub
        setup();
        setTilePolygons();
        tilesToPolygonList();
        twoDToIso();
        setPlayerPos();

        Group root = new Group();
        root.getChildren().addAll(this.poly);
        root.getChildren().add(this.player.getEllipse());
        scene = new Scene(root, 600, 600);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                int dx = 0;
                int dy = 0;
                switch (event.getCode()) {
                    case W:
                        dx = -1;
                        break;
                    case A:
                        dy = -1;
                        break;
                    case S:
                        dx = 1;
                        break;
                    case D:
                        dy = 1;
                        break;
                    case R:
                        rotate();
                        break;
                    case P:
                        AccessibleTile currentTile1 = (AccessibleTile) player.getTile();
                        System.out.println("Pick up: " +currentTile1.hasToken());
                        pickUpItem();
                        poly.clear();
                        setTilePolygons();
                        tilesToPolygonList();
                        twoDToIso();
                        System.out.println("Pick up: " +currentTile1.hasToken());
                        break;

                    case L:
                        AccessibleTile currentTile2 = (AccessibleTile) player.getTile();
                        System.out.println("Drop: " + currentTile2.hasToken());
                        dropItem();
                        poly.clear();
                        setTilePolygons();
                        tilesToPolygonList();
                        twoDToIso();
                        System.out.println("Drop: " + currentTile2.hasToken());
                        break;
                    default:

                }
                player.moveTile(dx, dy);
                setPlayerPos();
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public void pickUpItem() {
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (currentTile.hasToken()) {
            player.pickUp(currentTile.getToken());
            currentTile.setToken(null);
        }
    }

    public void dropItem() {
        List<Token> inventory = player.getInventory();
        AccessibleTile currentTile = (AccessibleTile) player.getTile();
        if (!currentTile.hasToken() && !inventory.isEmpty()) {
            currentTile.setToken(player.getInventory().remove(0));
        }
    }


    public void rotate() {
        if(!this.poly.isEmpty()) {
            poly.clear();
            System.out.println(poly.size());
        }
        currentRoom.rotateRoomClockwise();
        setTilePolygons();
        tilesToPolygonList();
        twoDToIso();
        System.out.println("");
    }

    public void setup() {
        // create a starting room for testing
        Room defaultRoom = RoomParser.createRoom(RoomParser.getBombRoom());
        currentRoom = defaultRoom;
        rooms.add(defaultRoom);
        AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
        player = new Player(defaultRoom, startingTile);
        startingTile.setPlayer(true);
    }

    public void setTilePolygons() {
        System.out.println("setTilePolygons");
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
                this.currentRoom.getTile(row, col).setTilePolygon(poly);
                if (this.currentRoom.getTile(row, col) instanceof AccessibleTile) {
                    AccessibleTile tile = (AccessibleTile) this.currentRoom.getTile(row, col);
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
        System.out.println("tilesToPolygonList");
        poly = new ArrayList<Polygon>();
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                poly.add(currentRoom.getTile(row, col).getTilePolygon());
            }
        }
    }

    public void setPlayerPos() {
        Point2D p = currentRoom.getPlayerTile().getCenter();
        this.player.getEllipse().setCenterX(p.getX());
        this.player.getEllipse().setCenterY(p.getY() - 13);
    }

    public void twoDToIso() {
        System.out.println("twoDToIso");
        for (Polygon p : this.poly) {
            for (int i = 0; i < p.getPoints().size() - 1; i += 2) {
                double x = p.getPoints().get(i) - p.getPoints().get(i + 1);
                double y = (p.getPoints().get(i) + p.getPoints().get(i + 1)) / 2;

                p.getPoints().set(i, x + 300);
                p.getPoints().set(i + 1, y);
            }
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

}
