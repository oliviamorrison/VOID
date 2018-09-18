package gameworld;

public class AccessibleTile extends Tile{
    Token token;

    public AccessibleTile(Room room){
        super(room);
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
