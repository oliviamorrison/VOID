package gameworld;

public class HealthPack extends Item {

  public HealthPack(int row, int col, String direction) {
    super(row, col, direction);
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
    return "HealthPack";
  }

}
