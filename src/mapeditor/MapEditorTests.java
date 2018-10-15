package mapeditor;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MapEditorTests {


    @Test
    public void testBoardSetUp(){
        MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();

        //board and item grid should be initialized
        GridPane board = mapEditor.getBoardGrid();
        assertNotNull(mapEditor.getNodeByRowColumnIndex(0,0,board));
    }

    @Test
    public void testItemSetUp(){
        MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();

        mapEditor.initItemSpaces(true);
        GridPane itemGrid = mapEditor.getItemGrid();

        assertNotNull(mapEditor.getNodeByRowColumnIndex(0,0,itemGrid));
    }

    @Test
    public void testMapItem(){
        Image img = new Image(getClass().getResourceAsStream("cutters.png"));
       MapItem i = new MapItem("cutters.png", img);

       assertEquals("cutters.png", i.getImageName());
       assertEquals(img, i.getImage());

    }

}
