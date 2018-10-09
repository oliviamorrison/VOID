package application;

import gameworld.Game;
import persistence.XMLParser;

public class Application {

    public static void main(String[] args){
        Game game = XMLParser.loadFile();
        if(game!=null){
            game.startGame();
        }
//        XMLParser.saveFile(game);
    }
}
