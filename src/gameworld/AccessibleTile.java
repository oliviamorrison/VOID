package gameworld;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

/**
 * This class represents a tile within a Room that a player can
 * move onto or an item/challenge can be located.
 */
public class AccessibleTile extends Tile {

  private Item item;
  private ChallengeItem challenge;
  private boolean player = false;

  public AccessibleTile(int x, int y) {
    super(x, y);
  }

  public Item getItem() {
    return this.item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public boolean hasItem() {
    return this.item != null;
  }

  public void setPlayer(boolean player) {
    this.player = player;
  }

  public boolean hasPlayer() {
    return player;
  }

  public ChallengeItem getChallenge() {
    return this.challenge;
  }

  public void setChallenge(ChallengeItem challenge) {
    this.challenge = challenge;
  }

  public boolean hasChallenge() {
    return this.challenge != null;
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

  @Override
  public String toString() {
    return " ";
  }

}
