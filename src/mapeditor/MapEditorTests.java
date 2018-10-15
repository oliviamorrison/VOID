package mapeditor;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import mapeditor.MapEditor.ItemSpace;
import mapeditor.MapEditor.TilePane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
    
    @Test
    public void testAntidoteRoom() {
    		MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();
        
        TilePane tile = null;
        
        //put antidote in room
        Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getBoardGrid());
        assertTrue(node instanceof GridPane);
        GridPane room1 = (GridPane) node;
        node = mapEditor.getNodeByRowColumnIndex(1, 1, room1);

        assertTrue(node instanceof TilePane);
        tile = (TilePane) node;
        tile.setMapItem(new MapItem("antidote.png",
                   new Image(getClass().getResourceAsStream("antidote.png"),
                           17, 17, false, false)));
          
        
        assertNotNull(tile);
        assertTrue(mapEditor.isInAntidoteRoom(tile));
        
        
    }
    
    @Test
    public void testInitaliseItems() {
    		MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();
        mapEditor.initaliseItems();
        
        TilePane tile = null;
        
        Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getBoardGrid());
        assertTrue(node instanceof GridPane);
        GridPane room1 = (GridPane) node;
        node = mapEditor.getNodeByRowColumnIndex(1, 1, room1);

        assertTrue(node instanceof TilePane);
        tile = (TilePane) node;
        assertEquals(tile.getMapItem().getImageName(),"player.png");
        
    }
    
    @Test
    public void createGameXML() {
    		MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();
        mapEditor.initaliseItems();
        assertTrue(mapEditor.createGame());
        
    }
    
//    @Test
//    public void testSetMapItem() {
//    		MapEditor mapEditor = new MapEditor();
//        mapEditor.setUp();
//        mapEditor.initaliseItems();
//        
//        ItemSpace tile = null;
//        
//        Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getO());
//        assertTrue(node instanceof GridPane);
//        GridPane room1 = (GridPane) node;
//        node = mapEditor.getNodeByRowColumnIndex(1, 1, room1);
//
//        assertTrue(node instanceof ItemSpace);
//        tile = (ItemSpace) node;
//        
//    }
    	  @Test
    	  public void testFindFirstEmptyTile() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getBoardGrid());
          assertTrue(node instanceof GridPane);
          GridPane room1 = (GridPane) node;
    	      
    	      assertNotNull(mapEditor.findFirstEmptyTile(room1));
    	  }
}
