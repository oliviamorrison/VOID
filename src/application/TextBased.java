package application;

import gameworld.Game;
import persistence.XMLParser;

import java.io.File;

public class TextBased {

    public static void main(String[] args){
        Game game = XMLParser.parseGame(new File("data/gameworld.xml"));
        if(game!=null){
            game.startGame();
        }
//        XMLParser.saveFile(game);
    }
}
