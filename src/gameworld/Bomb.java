package gameworld;

public class Bomb extends Challenge {

  public Bomb(String direction) {
    setDirection(direction);
  }

  @Override
  public String toString() {
    return "Bomb";
  }

}
