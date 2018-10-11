package gameworld;


public class Guard extends Challenge {

  private String direction;

  public Guard(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }


  @Override
  public String toString() {
    return "Guard";
  }

}
