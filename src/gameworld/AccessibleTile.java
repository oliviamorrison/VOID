package gameworld;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

/**
 * This class represents a tile within a Room that a player can
 * move onto or an item/challenge can be located.
 *
 * @author Latrell Whata 300417220
 */
public class AccessibleTile extends Tile {

  private Item item;
  private ChallengeItem challenge;
  private boolean player = false;

  /**
   * This constructor creates a new AccessibleTile.
   *
   * @param row the row value of the tile within the room
   * @param col the column value of the tile within the room
   */
  public AccessibleTile(int row, int col) {
    super(row, col);
  }

  /**
   * This method checks if the accessible tile is able to navigated onto.
   *
   * @return whether or not a player can navigate onto this tile.
   */
  public boolean checkNavigable() {

    if (hasChallenge()) {
      return challenge.isNavigable();
    }

    return true;

  }

  /**
   * This method finds the centre point of the accessible tile.
   *
   * @return the centre of the tile as a Point2D
   */
  public Point2D getCenter() {

    Polygon p = super.getTilePolygon().getPolygons().get(0);

    double minX = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    double minY = Double.MAX_VALUE;
    double maxY = Double.MIN_VALUE;

    for (int i = 0; i < p.getPoints().size() - 1; i += 2) {

      minX = Math.min(minX, p.getPoints().get(i));
      maxX = Math.max(maxX, p.getPoints().get(i));
      minY = Math.min(minY, p.getPoints().get(i + 1));
      maxY = Math.max(maxY, p.getPoints().get(i + 1));

    }

    double centerX = minX + ((maxX - minX) / 2);
    double centerY = minY + ((maxY - minY) / 2);

    return new Point2D(centerX, centerY);

  }

  /**
   * This method is a getter for the tile item.
   *
   * @return the tile item
   */
  public Item getItem() {
    return this.item;
  }


  /**
   * This method is a setter for the tile item.
   *
   * @param item the new item on the tile
   */
  public void setItem(Item item) {
    this.item = item;
  }

  /**
   * This method checks if the tile has an item.
   *
   * @return whether or not the tile has an item
   */
  public boolean hasItem() {
    return this.item != null;
  }

  /**
   * This method is a setter for the tile player.
   *
   * @param player whether or not the player is on the tile
   */
  public void setPlayer(boolean player) {
    this.player = player;
  }

  /**
   * This method checks if the tile has the player.
   *
   * @return whether or not the tile has the player
   */
  public boolean hasPlayer() {
    return player;
  }

  /**
   * This method is a getter for the tile challenge.
   *
   * @return the tile challenge
   */
  public ChallengeItem getChallenge() {
    return this.challenge;
  }

  /**
   * This method is a setter for the tile challenge.
   *
   * @param challenge the new challenge for the tile.
   */
  public void setChallenge(ChallengeItem challenge) {
    this.challenge = challenge;
  }

  /**
   * This method checks if the tile has a challenge.
   *
   * @return whether or not the tile has a challenge
   */
  public boolean hasChallenge() {
    return this.challenge != null;
  }

}
