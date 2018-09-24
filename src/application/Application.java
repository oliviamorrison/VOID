package application;

import gameworld.Game;
import persistence.XMLReader;

public class Application {



    public static void main(String[] args){
        Game game = XMLReader.parseGame();
        if(game!=null){
            game.startGame();
        }
    }
}
