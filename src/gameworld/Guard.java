package gameworld;


public class Guard extends Challenge {

  public Guard(String direction) {
    setDirection(direction);
  }

  @Override
  public String toString() {
    return "Guard";
  }

}
