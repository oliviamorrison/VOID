package gameworld;

public class Antidote extends Item {

  public Antidote(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The poison antidote";
  }

  @Override
  public String getDescription() {
    return "It's a small bottle with a powerful punch. It could even save a life.";
  }

}
