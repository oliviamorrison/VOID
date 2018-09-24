package gameworld;

public class InaccessibleTile extends Tile{

    public InaccessibleTile(Room room){
        super(room);
    }
    @Override
    public String toString(){
        return "X";
    }
}
