package renderer;

import gameworld.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import persistence.RoomParser;

public class Renderer {
    Game game;
    public static Player player;
    private static Room currentRoom;
    private static Group root;

    public Renderer(Game game) {
        this.game = game;
        this.player = game.getPlayer();
        this.currentRoom = player.getRoom();
//        setUpGame();
        root = new Group();
        drawFloor();
        drawUpperWall();
        drawPlayer();
        drawLowerWall();
    }

    public Group getRoot() {
        return root;
    }

    private void setUpGame() {
        // create a starting room for testing
        Room defaultRoom = RoomParser.createRoom(RoomParser.getBombRoom());
        currentRoom = defaultRoom;
        AccessibleTile startingTile = (AccessibleTile) defaultRoom.getTile(2, 2);
        player = new Player(defaultRoom, startingTile);
        startingTile.setPlayer(true);
    }

    public void drawFloor() {
        for (int row = 1; row < Room.ROOMSIZE - 1; row++) {
            for (int col = 1; col < Room.ROOMSIZE - 1; col++) {
                AccessibleTile tile = (AccessibleTile) currentRoom.getTile(row, col);
                Color color = tile.getColor();
                if(tile.hasItem()) {
                    color = Color.BLUE;
                } else if(tile.hasChallenge()) {
                    color = Color.RED;
                } if(tile instanceof  DoorTile){
                    color = Color.ORANGE;
                }
                PolygonBlock poly = new PolygonBlock(col, row, tile.getHeight(), color);
                tile.setTilePolygon(poly);
                root.getChildren().addAll(poly.getPolygons());
            }
        }
    }

    public Color randomColor() {
        return Color.rgb((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
    }

    public void drawPlayer() {
        Point2D p = currentRoom.getPlayerTile().getCenter();
        player.getEllipse().setCenterX(p.getX());
        player.getEllipse().setCenterY(p.getY() - 13);
        root.getChildren().add(player.getEllipse());
    }

    public void drawUpperWall() {
        for (int row = 0; row < Room.ROOMSIZE - 1; row++) {
            for (int col = 0; col < Room.ROOMSIZE - 1; col++) {
                if (row == 0 || col == 0) {
                    Tile tile = currentRoom.getTile(row, col);
                    Color color = tile.getColor();
                    if(tile instanceof DoorTile){
                        color = Color.ORANGE;
                    }
                    PolygonBlock poly = new PolygonBlock(col, row, tile.getHeight(), color);
                    tile.setTilePolygon(poly);
                    root.getChildren().addAll(poly.getPolygons());
                }
            }
        }
    }

    public void drawLowerWall() {
        for (int row = 0; row < Room.ROOMSIZE; row++) {
            for (int col = 0; col < Room.ROOMSIZE; col++) {
                if (row == Room.ROOMSIZE - 1 || col == Room.ROOMSIZE - 1) {
                    Tile tile = currentRoom.getTile(row, col);
                    Color color = tile.getColor();
                    if(tile instanceof DoorTile){
                        color = Color.ORANGE;
                    }
                    PolygonBlock poly = new PolygonBlock(col, row, tile.getHeight(), color);
                    tile.setTilePolygon(poly);
                    root.getChildren().addAll(poly.getPolygons());
                }
            }
        }
    }

    public void redraw() {
        root.getChildren().clear();
        drawFloor();
        drawUpperWall();
        drawPlayer();
        drawLowerWall();
    }

    public void rotate() {
        currentRoom.rotateRoomClockwise();
        redraw();
    }

}