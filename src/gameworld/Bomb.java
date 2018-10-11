package gameworld;

public class Bomb extends Challenge {

  private String direction;

  public Bomb(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return "Bomb";
  }

}
