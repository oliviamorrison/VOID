package gameworld;

public class OxygenTank extends Item {

  public OxygenTank(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The full oxygen tank";
  }

  @Override
  public String getDescription() {
    return "It's a full oxygen tank. It'll help you breathe.";
  }

}
