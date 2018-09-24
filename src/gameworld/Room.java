package gameworld;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Room {
    private Tile[][] tiles;
    private final int ROOMSIZE = 10;

    private static final Point TOP = new Point(0,5);
    private static final Point BOTTOM = new Point(9,5);
    private static final Point LEFT = new Point(5,0);
    private static final Point RIGHT = new Point(5,9);


    public Room(HashMap<String, DoorTile> doors){
        //may need to change this depending on XML
        this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
        //For now until we can load in an XML file
        for(int i = 0; i < ROOMSIZE; i++){
            for(int j = 0; j < ROOMSIZE; j++){
                tiles[i][j] = new AccessibleTile(this);
            }
        }
        //TODO: Change walls to be inaccessible, and add doors depending on direction in room
        //This is just to test if door checking works
        DoorTile door = new DoorTile(null, this);
        tiles[0][0] = new DoorTile(door, this);



    }

    public Room(List<DoorTile> doors) {


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

        //if the newCoordinates are inbounds and the tile is not inaccessible
        if(newX<10 && newY<10 && newX >=0 && newY >=0 && !(tiles[newX][newY] instanceof InaccessibleTile)){
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

    public Tile getTile(int x, int y){
        return tiles[x][y];
    }

}
