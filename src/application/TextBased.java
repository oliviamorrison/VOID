package application;

import gameworld.Game;
import persistence.XMLParser;

import java.io.File;

public class TextBased {

  public static void main(String[] args) throws XMLParser.ParseError {

    Game game = XMLParser.parseGame(new File("data/gameworld.xml"));

    assert game != null;
    game.startGame();

  }

}
