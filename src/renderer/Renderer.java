package renderer;

import gameworld.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Renderer {
    private final static Color INACCESSIBLE_COLOUR = Color.rgb(90, 90, 90);
    private final static Color ACCESSIBLE_COLOUR = Color.rgb(120, 120, 120);
    private final static Color DTColor = Color.rgb(161, 176, 201);
    private final static double floorHeight = 0;
    private final static double wallHeight = 2;
    private final static double doorHeight = 0;

    private final static double playerHeight = 60;
    private final static String NORTH = "images/north.png";
    private final static String SOUTH = "images/south.png";
    private final static String WEST = "images/west.png";
    private final static String EAST = "images/east.png";

    //Challenges
    private final static String bombImage = "images/bomb";
    private final static String vendingMachineImage = "images/vending-machine";

    //Items
    private final static String diffuserImage = "images/diffuser2.png";//TODO: needs orientation
    private final static String coinImage = "images/coin";
    private final static String boltCutterImage = "images/bolt-cutter";
    private final static String beerImage = "images/beer2.png";


    private Player player;
    private Room currentRoom;
    private Group root;

    public Renderer(Game game) {
        player = game.getPlayer();
        currentRoom = player.getRoom();
        root = new Group();
        draw();
    }

    public Group getRoot() {
        return root;
    }

    public void drawPlayer() {
        Point2D p = player.getTile().getCenter();
        ImageView playerImage = null;
        switch (player.getDirection()) {
            case NORTH:
                playerImage = getImage(NORTH);
                break;
            case SOUTH:
                playerImage = getImage(SOUTH);
                break;
            case WEST:
                playerImage = getImage(WEST);
                break;
            case EAST:
                playerImage = getImage(EAST);
                break;
        }
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
    }

    public void drawPolygonBlock(int row, int col){
        Tile tile = currentRoom.getTile(row, col);
        Color color = Color.BLACK;
        double height = 0;

        if(tile instanceof  AccessibleTile){
            AccessibleTile AT = (AccessibleTile) tile;
            color = ACCESSIBLE_COLOUR;
            height = floorHeight;
            if(AT.hasItem()){
                color = Color.BLUE;
            } else if(AT.hasChallenge()){
                color = Color.RED;
            }
        } else if (tile instanceof InaccessibleTile){
            height = wallHeight;
            color = INACCESSIBLE_COLOUR;
        }
        if(tile instanceof Portal){
            height = doorHeight;
            color = DTColor;
        }
        PolygonBlock poly = new PolygonBlock(col, row, height, color);
        tile.setTilePolygon(poly);

        ImageView gameObject = null;
        if(tile instanceof  AccessibleTile){
            AccessibleTile AT = (AccessibleTile) tile;
            if(AT.hasItem()){
                gameObject = getItemImage(AT);

            } else if(AT.hasChallenge()){
                gameObject = getChallengeImage(AT);
            }
        }

        root.getChildren().addAll(poly.getPolygons());
        if(gameObject != null){
            root.getChildren().add(gameObject);
        }
        if(player.getTile().equals(tile) ){
            drawPlayer();
        }
    }


    public ImageView getItemImage(AccessibleTile tile){
        Item item = tile.getItem();
        ImageView itemImage = null;
        Point2D c = tile.getCenter();
        if(item instanceof Diffuser){
            itemImage = getImage(diffuserImage);
            itemImage.setFitHeight(30);
            itemImage.setX(c.getX() - 14);
            itemImage.setY(c.getY() - 22);
        } else if(item instanceof Coin){
            String direction ="";
            if(item.getDirection() == Direction.NORTH || item.getDirection() == Direction.SOUTH){
                direction = "1.png";
            } else {
                direction = "2.png";
            }
            itemImage = getImage((coinImage+direction));
            itemImage.setFitHeight(30);
            itemImage.setX(c.getX() - 13);
            itemImage.setY(c.getY() - 22);
        } else if(item instanceof BoltCutter){
            itemImage = getImage((boltCutterImage+ getObjectDirection(item.getDirection())));
            itemImage.setFitHeight(20);
            itemImage.setX(c.getX() - 14);
            itemImage.setY(c.getY() - 12);
        } else if(item instanceof Potion){
            itemImage = getImage((beerImage));
            itemImage.setFitHeight(30);
            itemImage.setX(c.getX() - 5);
            itemImage.setY(c.getY() - 25);
        }
        return itemImage;

    }
    public ImageView getChallengeImage(AccessibleTile tile){
        Item challenge = tile.getChallenge();
        ImageView itemImage = null;
        Point2D c = tile.getCenter();
        if(challenge instanceof Bomb){
            itemImage = getImage((bombImage+ getObjectDirection(challenge.getDirection())));
            itemImage.setFitHeight(35);
            itemImage.setX(c.getX() - 20);
            itemImage.setY(c.getY() - 22);
        } else if(challenge instanceof VendingMachine){
            itemImage = getImage((vendingMachineImage+ getObjectDirection(challenge.getDirection())));
            itemImage.setFitHeight(80);
            itemImage.setX(c.getX() - 30);
            itemImage.setY(c.getY() - 65);
        } else if(challenge instanceof Alien){
            itemImage = getImage((vendingMachineImage+ getObjectDirection(challenge.getDirection())));
            itemImage.setFitHeight(80);
            itemImage.setX(c.getX() - 30);
            itemImage.setY(c.getY() - 65);
        }
        return itemImage;

    }

    public String getObjectDirection(Direction direction){
        switch (direction){
            case NORTH:
                return "N.png";
            case SOUTH:
                return "S.png";
            case WEST:
                return "W.png";
            case EAST:
                return "E.png";
            default:
                return null;
        }
    }

    public ImageView getImage(String imageName){
        Image image = null;
        try {
            image = new Image(new FileInputStream(imageName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public void newRoom(){
        currentRoom = player.getRoom();
        root.getChildren().clear();
        draw();
    }

}