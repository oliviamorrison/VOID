package gameworld;

public class Antidote extends Item {

  public Antidote(int row, int col, String direction) {
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
    return "Antidote";
  }

}
