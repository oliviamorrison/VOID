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

/**
 * This is the Renderer class which helps render the game into
 * the GUI.
 *
 * @author James Del Puerto 300375073
 */
public class Renderer {

  //Tile heights.
  private static final double doorHeight = 0;
  private static final double floorHeight = 0;
  private static final double wallHeight = 2;

  //Tile colors.
  private static final Color ACCESSIBLE_COLOUR = Color.rgb(120, 120, 120);
  private static final Color INACCESSIBLE_COLOUR = Color.rgb(90, 90, 90);
  private static final Color DTColor = Color.rgb(161, 176, 201);

  //Player height.
  private static final double playerHeight = 60;

  //Player Orientation.
  private static final String NORTH = "images/north.png";
  private static final String SOUTH = "images/south.png";
  private static final String WEST = "images/west.png";
  private static final String EAST = "images/east.png";

  //Portal.
  private static final String portalImage = "images/portal.png";

  //Challenges.
  private static final String bombImage = "images/bomb";
  private static final String vendingMachineImage = "images/vending-machine";
  private static final String chainedVendingMachineImage = "images/chained-vending-machine";
  private static final String alienImage = "images/alien";

  //Items.
  private static final String diffuserImage = "images/diffuser";
  private static final String boltCutterImage = "images/bolt-cutter";
  private static final String coinImage = "images/coin";
  private static final String potionImage = "images/potion.png";
  private static final String spaceshipImage = "images/spaceship";
  private static final String oxygenTankImage = "images/oxygen-tank.png";

  //Games fields.
  private Player player;
  private Room currentRoom;
  private Group root;

  /**
   * This is the constructor which takes in the game parameter
   * so it knows what to render.
   * @param game the current game that is being played
   */
  public Renderer(Game game) {
    player = game.getPlayer();
    currentRoom = player.getRoom();
    root = new Group();
    draw();
  }

  /**
   * Returns the root which contains all the images, shapes and polygons
   * that will be drawn into the GUI.
   * @return A group of all images and polygons
   */
  public Group getRoot() {
    return root;
  }

  /**
   * Draws the player by adding the player image (in respect of the
   * player's direction) into the root. Method also determines the position
   * of the image in the graphic pane.
   */
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

  /**
   * This draws the whole game by iterating through each tile in
   * the game and drawing the tiles as well objects on top of those tiles.
   */
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

  /**
   * Draws the tile (PolygonBlock) for each tile and stores it in the
   * tile's field. Creates a PolygonBlock and adds it to the root as
   * calling the drawObject() method.
   * @param row The row position of the current tile that is to be populated
   *            with a PolygonBlock
   * @param col The col position of the current tile that is to be populated
   *            with a PolygonBlock
   */
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
    drawObjects(tile, poly);
  }

  /**
   * Draws the objects on top of the the tile given and the position
   * of object is determined by the position of the tile's PolygonBlock.
   * @param tile The tile that an object is to be drawn on top of
   * @param poly The tile's PolygonBlock
   */
  public void drawObjects(Tile tile, PolygonBlock poly){
    ImageView gameObject = null;
    if(tile instanceof Portal){
      AccessibleTile at = (AccessibleTile) tile;
      gameObject = getImage(portalImage);
      Point2D c = at.getCenter();
      gameObject.setPreserveRatio(false);
      gameObject.setFitHeight(22);
      gameObject.setFitWidth(45);
      gameObject.setX(c.getX() - 20);
      gameObject.setY(c.getY() - 13);
    } else if (tile instanceof AccessibleTile) {
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

  /**
   * Gets the ImageView of an Item of a given tile that has an item
   * on top of it.
   * @param tile The tile that has an item on top of it
   * @return The ImageView of the image of the item on top of the tile
   */
  public ImageView getItemImage(AccessibleTile tile) {
    Item item = tile.getItem();
    ImageView itemImage = null;
    Point2D c = tile.getCenter();
    String direction = "";
    if (item.getDirection() == Direction.NORTH || item.getDirection() == Direction.SOUTH) {
      direction = "1.png";
    } else {
      direction = "2.png";
    }
    if (item instanceof Diffuser) {
      itemImage = getImage(diffuserImage + direction);
      itemImage.setFitHeight(30);
      itemImage.setX(c.getX() - 14);
      itemImage.setY(c.getY() - 22);
    } else if (item instanceof Coin) {
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
      itemImage.setX(c.getX() - 12);
      itemImage.setY(c.getY() - 25);
    } else if (item instanceof OxygenTank) {
      itemImage = getImage((oxygenTankImage));
      itemImage.setFitHeight(50);
      itemImage.setX(c.getX() - 5);
      itemImage.setY(c.getY() - 45);
    } else if (item instanceof SpaceShip) {
      itemImage = getImage((spaceshipImage + getObjectDirection(item.getDirection())));
      itemImage.setFitHeight(40);
      itemImage.setX(c.getX() - 20);
      itemImage.setY(c.getY() - 30);
    }
    return itemImage;
  }

  /**
   * Gets the ImageView of a Challenge of a given tile that has a challenge
   * on top of it.
   * @param tile The tile that has an item on top of it
   * @return The ImageView of the image of the item on top of the tile
   */
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
      if (((VendingMachine) challenge).isUnlocked()) {
        itemImage = getImage((vendingMachineImage + getObjectDirection(challenge.getDirection())));
      } else {
        itemImage = getImage((chainedVendingMachineImage
            + getObjectDirection(challenge.getDirection())));
      }
      itemImage.setFitHeight(80);
      itemImage.setX(c.getX() - 30);
      itemImage.setY(c.getY() - 65);
    } else if (challenge instanceof Alien) {
      itemImage = getImage((alienImage + getObjectDirection(challenge.getDirection())));
      itemImage.setFitHeight(80);
      itemImage.setX(c.getX() - 20);
      itemImage.setY(c.getY() - 70);
    }
    return itemImage;
  }

  /**
   * Gets the last suffix of an image path in respect of the direction given.
   * @param direction The direction of the image it is facing.
   * @return The last suffix (letters) of the image's path
   */
  public String getObjectDirection(Direction direction) {
    switch (direction) {
      case NORTH:
        return "N.png";
      case SOUTH:
        return "S.png";
      case WEST:
        return "E.png";
      default:
        return "W.png";
    }
  }

  /**
   * Takes in a image path which is used to create an Image which is then
   * used to create an ImageView which contains the image of the object that is
   * to be drawn in respect of the image path.
   * @param imageName The image path of the image to be drawn
   * @return The ImageView of the image to be drawn
   */
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

  /**
   * Clears the root and updates the currentRoom that the renderer
   * will be rendering when the player has moved from one room to another.
   * Draws the new room.
   */
  public void newRoom() {
    currentRoom = player.getRoom();
    root.getChildren().clear();
    draw();
  }

}