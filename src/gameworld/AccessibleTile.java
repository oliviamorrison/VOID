package gameworld;

public class AccessibleTile extends Tile{
    Token token;

    public AccessibleTile(Room room){
        super(room);
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
    public Token getToken() {
    	return this.token;
    }
    
    public boolean hasToken() {
    	return this.token != null;
    }
}
