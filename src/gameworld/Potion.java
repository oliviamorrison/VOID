package gameworld;

public class Potion extends Item {

  public Potion(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The cold beer";
  }

  @Override
  public String getDescription() {
    return "It's an ice-cold beer, perfect after a long day at work";
  }


}
