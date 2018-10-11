package gameworld;


public class Guard extends Challenge {

  public Guard(int x, int y, String direction) {

    super(x, y);
    setDirection(direction);
    
  }

  @Override
  public String toString() {
    return "Guard";
  }

}
