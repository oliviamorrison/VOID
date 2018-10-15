package gameworld;

public class Coin extends Item {
  public Coin(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Golden Coin";
  }

  @Override
  public String getDescription() {
    return "It's a shiny, gold coin. It could probably buy something nice to drink.";
  }

  @Override
  public String toString() {
    return "Coin";
  }

}
