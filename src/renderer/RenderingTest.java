package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gameworld.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.XMLParser;
import renderer.Renderer;

public class RenderingTest {

    private Game game;
    private Room[][] board;
    private Player player;
    private Renderer renderer;

    /**
     * This method sets up a test scenario to ease testing.
     *
     * @throws XMLParser.ParseError xml parsing error
     */
    @BeforeEach
    public void setUp() throws XMLParser.ParseError {

        game = XMLParser.parseGame(new File("data/gameworldTestData.xml"));

        if (game != null) {
            board = game.getBoard();
            player = game.getPlayer();
            renderer = new Renderer(game);
        }

    }

    @Test void getObjectDirectionTest(){
        assertEquals("N.png", renderer.getObjectDirection(Direction.NORTH));
        assertEquals("S.png", renderer.getObjectDirection(Direction.SOUTH));
        assertEquals("W.png", renderer.getObjectDirection(Direction.WEST));
        assertEquals("E.png", renderer.getObjectDirection(Direction.EAST));
    }

}

