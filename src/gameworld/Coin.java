package gameworld;

public class Coin extends Item {
  public Coin(int row, int col) {
    super(row, col);
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String toString() {
    return "Coin";
  }

}
