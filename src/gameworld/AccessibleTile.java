package gameworld;

public class AccessibleTile extends Tile {
    private Token token;
    private Bomb bomb;
    private boolean player = false;

    public AccessibleTile(Room room, int x, int y) {
        super(room, x, y);
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

    public void setBomb(Bomb b) {
        this.bomb = b;
    }

    public Bomb getBomb() {
        return this.bomb;
    }

    public boolean hasBomb() {
        return this.bomb != null;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public boolean hasPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "X";
    }
}
