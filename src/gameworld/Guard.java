package gameworld;

public class Guard implements Challenge {
    private String direction;

    public Guard(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    @Override
  public void interact() {

  }

  @Override
  public String toString(){
    return "guard";
  }

}
