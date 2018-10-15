package gameworld;

public class SpaceShip extends Item {

  public SpaceShip(int row, int col, String direction) {
    super(row, col, direction);
  }

  @Override
  public String getName() {
    return "The amazing spaceship";
  }

  @Override
  public String getDescription() {
    return "It's an amazing spaceship. It'll help me to escape.";
  }

  @Override
  public String toString() {
    return "Spaceship";
  }

}
