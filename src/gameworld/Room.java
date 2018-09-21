package gameworld;

public class Room {
    private Tile[][] tiles;
    private final int ROOMSIZE = 10;


    public Room(){
        //may need to change this depending on XML
        this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
    }

    public boolean checkActiveBomb(){
        for(int i=0; i< ROOMSIZE; i++){
            for (int j = 0; j < ROOMSIZE; j++) {
                if(tiles[i][j] instanceof  AccessibleTile){
                    AccessibleTile tile = (AccessibleTile) tiles[i][j];
                    if(tile.hasToken() && tile.getToken() instanceof Bomb ){
                        //Assumes only one bomb in each room
                        return ((Bomb) tile.getToken()).isActive;
                    }
                }
            }
        }

        return false;
    }



    public Tile moveTile(Tile t, int dx, int dy){
        int[] coords = getCoordsofTile(t);

        int x = coords[0];
        int y = coords[1];

        int newX = x+dx;
        int newY = y+dy;

        //if the newCoordinates are inbounds and the tile is not inacessible
        if(newX<11 && newY<11 && !(tiles[newX][newY] instanceof InaccessibleTile)){
            return tiles[newX][newY];
        }

        return null;
    }

    public int[] getCoordsofTile(Tile t){

        for(int i=0; i<ROOMSIZE; i++){
            for(int j=0; j<ROOMSIZE; j++){

                //returns coordinates of the tile
                if(tiles[i][j].equals(t)) return new int[]{i,j};
            }
        }

        return null;
    }

}
