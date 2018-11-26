package gameworld;

/**
 * This class represents a challenge that must be completed by
 * the player to progress through the game.
 *
 * @author Latrell Whata 300417220
 */
public abstract class ChallengeItem extends Item {

  // determines if the challenge can be navigated onto with the player
  private boolean navigable = false;

  /**
   * This constructor creates a challenge.
   *
   * @param row       the row value of the tile the ChallengeItem is located
   * @param col       the column value of the tile the ChallengeItem is located
   * @param direction the direction the ChallengeItem is facing
   */
  public ChallengeItem(int row, int col, String direction) {
    super(row, col, direction);
  }

  /**
   * This method is a getter to determine if the ChallengeItem is navigable.
   *
   * @return whether or not the challengeItem is navigable
   */
  public boolean isNavigable() {
    return navigable;
  }

  /**
   * This method is a setter to set the ChallengeItem as navigable.
   *
   * @param navigable the new value determining if it is navigable.
   */
  public void setNavigable(boolean navigable) {
    this.navigable = navigable;
  }

}
