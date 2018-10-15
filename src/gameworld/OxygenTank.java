package gameworld;

public class OxygenTank extends Item {

  public OxygenTank(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The medical health pack";
  }

  @Override
  public String getDescription() {
    return "It's a medical health pack. It'll make you feel better.";
  }

}
