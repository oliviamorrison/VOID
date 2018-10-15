package gameworld;

public class Potion extends Item {

  public Potion(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The Magic Potion";
  }

  @Override
  public String getDescription() {
    return "It's a magical potion. It could be useful to someone else.";
  }

  @Override
  public String toString() {
    return "Potion";
  }

}
