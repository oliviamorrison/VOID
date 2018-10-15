package gameworld;

public class Diffuser extends Item {

  public Diffuser(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The bomb diffuser";
  }

  @Override
  public String getDescription() {
    return "It's a funny-looking suitcase. Which button do I press to work it?";
  }

}
