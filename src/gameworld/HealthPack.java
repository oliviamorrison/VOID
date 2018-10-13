package gameworld;

public class HealthPack extends Item {

  public HealthPack(int row, int col) {
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
    return "Health pack";
  }

}
