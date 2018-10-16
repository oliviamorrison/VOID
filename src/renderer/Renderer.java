package renderer;

import gameworld.AccessibleTile;
import gameworld.Alien;
import gameworld.BoltCutter;
import gameworld.Bomb;
import gameworld.Coin;
import gameworld.Diffuser;
import gameworld.Direction;
import gameworld.Game;
import gameworld.InaccessibleTile;
import gameworld.Item;
import gameworld.OxygenTank;
import gameworld.Player;
import gameworld.Portal;
import gameworld.Potion;
import gameworld.Room;
import gameworld.SpaceShip;
import gameworld.Tile;
import gameworld.VendingMachine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Renderer {

  private static final double doorHeight = 0;
  private static final double floorHeight = 0;
  private static final double playerHeight = 60;
  private static final double wallHeight = 2;

  private static final Color ACCESSIBLE_COLOUR = Color.rgb(120, 120, 120);
  private static final Color INACCESSIBLE_COLOUR = Color.rgb(90, 90, 90);
  private static final Color DTColor = Color.rgb(161, 176, 201);

  private static final String NORTH = "images/north.png";
  private static final String SOUTH = "images/south.png";
  private static final String WEST = "images/west.png";
  private static final String EAST = "images/east.png";

  //Challenges
  private static final String bombImage = "images/bomb";
  private static final String vendingMachineImage = "images/vending-machine";
  private static final String alienImage = "images/alien";

  //Items
  private static final String diffuserImage = "images/diffuser";
  private static final String boltCutterImage = "images/bolt-cutter";
  private static final String coinImage = "images/coin";
  private static final String potionImage = "images/potion.png";
  private static final String spaceshipImage = "images/spaceship";
  private static final String oxygenTankImage = "images/oxygen-tank.png";

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
      default:
        playerImage = getImage(EAST);
        break;
    }
    playerImage.setPreserveRatio(true);
    playerImage.setFitHeight(playerHeight);
    Point2D p = player.getTile().getCenter();
    playerImage.setX(p.getX() - 15);
    playerImage.setY(p.getY() - 55);
    root.getChildren().add(playerImage);
  }

  public void draw() {
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

  public void drawPolygonBlock(int row, int col) {
    Tile tile = currentRoom.getTile(row, col);
    Color color = Color.BLACK;
    double height = 0;

    if (tile instanceof AccessibleTile) {
      AccessibleTile a = (AccessibleTile) tile;
      color = ACCESSIBLE_COLOUR;
      height = floorHeight;
    } else if (tile instanceof InaccessibleTile) {
      height = wallHeight;
      color = INACCESSIBLE_COLOUR;
    }
    if (tile instanceof Portal) {
      height = doorHeight;
      color = DTColor;
    }
    PolygonBlock poly = new PolygonBlock(col, row, height, color);
    tile.setTilePolygon(poly);

    ImageView gameObject = null;
    if (tile instanceof AccessibleTile) {
      AccessibleTile at = (AccessibleTile) tile;
      if (at.hasItem()) {
        gameObject = getItemImage(at);

      } else if (at.hasChallenge()) {
        gameObject = getChallengeImage(at);
      }
    }
    root.getChildren().addAll(poly.getPolygons());
    if (gameObject != null) {
      root.getChildren().add(gameObject);
    }
    if (player.getTile().equals(tile)) {
      drawPlayer();
    }
  }


  public ImageView getItemImage(AccessibleTile tile) {
    Item item = tile.getItem();
    ImageView itemImage = null;
    Point2D c = tile.getCenter();
    if (item instanceof Diffuser) {
      String direction = "";
      if (item.getDirection() == Direction.NORTH || item.getDirection() == Direction.SOUTH) {
        direction = "1.png";
      } else {
        direction = "2.png";
      }
      itemImage = getImage(diffuserImage + direction);
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 14);
      itemImage.setY(c.getY() - 22);
    } else if (item instanceof Coin) {
      String direction = "";
      if (item.getDirection() == Direction.NORTH || item.getDirection() == Direction.SOUTH) {
        direction = "1.png";
      } else {
        direction = "2.png";
      }
      itemImage = getImage((coinImage + direction));
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 13);
      itemImage.setY(c.getY() - 22);
    } else if (item instanceof BoltCutter) {
      itemImage = getImage((boltCutterImage + getObjectDirection(item.getDirection())));
      itemImage.setFitHeight(20);
      itemImage.setX(c.getX() - 14);
      itemImage.setY(c.getY() - 12);
    } else if (item instanceof Potion) {
      itemImage = getImage((potionImage));
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 5);
      itemImage.setY(c.getY() - 25);
    } else if (item instanceof OxygenTank) {
      itemImage = getImage((oxygenTankImage));
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 5);
      itemImage.setY(c.getY() - 25);
    } else if (item instanceof SpaceShip) {
      itemImage = getImage((spaceshipImage + getObjectDirection(item.getDirection())));
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 5);
      itemImage.setY(c.getY() - 25);
    }
    return itemImage;

  }

  public ImageView getChallengeImage(AccessibleTile tile) {
    Item challenge = tile.getChallenge();
    ImageView itemImage = null;
    Point2D c = tile.getCenter();
    if (challenge instanceof Bomb) {
      itemImage = getImage((bombImage + getObjectDirection(challenge.getDirection())));
      itemImage.setFitHeight(35);
      itemImage.setX(c.getX() - 20);
      itemImage.setY(c.getY() - 22);
    } else if (challenge instanceof VendingMachine) {
      itemImage = getImage((vendingMachineImage + getObjectDirection(challenge.getDirection())));
      itemImage.setFitHeight(80);
      itemImage.setX(c.getX() - 30);
      itemImage.setY(c.getY() - 65);
    } else if (challenge instanceof Alien) {
      itemImage = getImage((alienImage + getObjectDirection(challenge.getDirection())));
      itemImage.setFitHeight(80);
      itemImage.setX(c.getX() - 30);
      itemImage.setY(c.getY() - 65);
    }
    return itemImage;

  }

  public String getObjectDirection(Direction direction) {
    switch (direction) {
      case NORTH:
        return "N.png";
      case SOUTH:
        return "S.png";
      case WEST:
        return "E.png";
      case EAST:
        return "W.png";
      default:
        return null;
    }
  }

  public ImageView getImage(String imageName) {
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

  public void newRoom() {
    currentRoom = player.getRoom();
    root.getChildren().clear();
    draw();
  }

}