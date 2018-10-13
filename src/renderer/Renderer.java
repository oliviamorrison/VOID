package renderer;

import gameworld.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Renderer {
    private final static Color ITColor = Color.rgb(237, 185, 177);
    private final static Color ATColor = Color.rgb(226, 209, 206);
    private final static Color DTColor = Color.rgb(161, 176, 201);
    private  final static double floorHeight = 0;
    private  final static double wallHeight = 1.25;
    private final static  double doorHeight = 0;

    private final static double playerHeight = 60;
    private String NORTH = "src/application/north.png";
    private String SOUTH = "src/application/south.png";
    private String WEST = "src/application/west.png";
    private String EAST = "src/application/east.png";

    private Game game;
    private static Player player;
    private static Room currentRoom;
    private static Group root;

    public Renderer(Game game) {
        this.game = game;
        player = game.getPlayer();
        currentRoom = player.getRoom();
        root = new Group();
        draw();
    }

    public Group getRoot() {
        return root;
    }

    public void drawPlayer() {
        Point2D p = currentRoom.getPlayerTile().getCenter();
        Image image = null;
        try {
            switch (player.getDirection()) {
                case NORTH:
                    image = new Image(new FileInputStream(NORTH));
                    break;
                case SOUTH:
                    image = new Image(new FileInputStream(SOUTH));
                    break;
                case WEST:
                    image = new Image(new FileInputStream(WEST));
                    break;
                case EAST:
                    image = new Image(new FileInputStream(EAST));
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView playerImage = new ImageView(image);
        playerImage.setPreserveRatio(true);
        playerImage.setFitHeight(playerHeight);
        playerImage.setX(p.getX() - 15);
        playerImage.setY(p.getY() - 55);
        root.getChildren().add(playerImage);
    }

    public void draw(){
        root.getChildren().clear();
        int w = 10;
        int h = 10;
        for (int k = 0; k <= w + h - 2; k++) {
            for (int col = 0; col <= k; col++) {
                int row = k - col;
                if (row < h && col < w) {
                    drawPolygonBlock(row, col);
                }
            }
        }
//        drawHealthBar();

    }

    public void drawPolygonBlock(int row, int col){
        Tile tile = currentRoom.getTile(row, col);
        Color color = Color.BLACK;
        double height = 0;
        if(tile instanceof  AccessibleTile){
            AccessibleTile AT = (AccessibleTile) tile;
            color = ATColor;
            height = floorHeight;
            if(AT.hasItem()){
                color = Color.BLUE;
            } else if(AT.hasChallenge()){
                color = Color.RED;
            }
        } else if (tile instanceof InaccessibleTile){
            height = wallHeight;
            InaccessibleTile IT = (InaccessibleTile) tile;
            color = ITColor;
        }
        if(tile instanceof Portal){
            height = doorHeight;
            color = DTColor;
        }

        PolygonBlock poly = new PolygonBlock(col, row, height, color);
        tile.setTilePolygon(poly);
        root.getChildren().addAll(poly.getPolygons());
        if(currentRoom.getPlayerTile().equals(tile) ){
            drawPlayer();
        }
    }

    public void drawHealthBar(){
        //TODO: Need to fix this.
        System.out.println(game.getPlayer().getHealth());
        double height = 20;
        double width = (game.getPlayer().getHealth()) * 2;
//        double width = 200;
        Rectangle healthBar =  new Rectangle();
        healthBar.setX(20);
        healthBar.setY(20);
        healthBar.setHeight(height);
        healthBar.setWidth(width);
//        healthBar.setFill(Color.RED);
        root.getChildren().add(healthBar);
    }

    public void newRoom(){
        currentRoom = player.getRoom();
        root.getChildren().clear();
        draw();
    }

    public Color randomColor() {
        return Color.rgb((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
    }

}