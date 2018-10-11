package gameworld;

public class Bomb extends Challenge {

  public Bomb(int x, int y, String direction) {

    super(x, y);
    setDirection(direction);

  }

  @Override
  public String toString() {
    return "Bomb";
  }

}
