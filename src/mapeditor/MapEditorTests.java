package mapeditor;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import mapeditor.MapEditor.ItemSpace;
import mapeditor.MapEditor.TilePane;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    	  
    	  @Test
    	  public void testResetImageView() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getBoardGrid());
          assertTrue(node instanceof GridPane);
          GridPane room1 = (GridPane) node;
          node = mapEditor.getNodeByRowColumnIndex(1, 1, room1);
          assertTrue(node instanceof TilePane);
          TilePane tile = (TilePane) node;
          
          ImageView first = tile.getImageView();
          
          tile.resetImageView();
          
          ImageView second = tile.getImageView();
          
          assertNotEquals(first,second);
    	      
    	  }
    	  
    	  @Test
    	  public void testImageSpace() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      mapEditor.initItemSpaces(true);
            
          Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getItemGrid());
          assertTrue(node instanceof ItemSpace);
          ItemSpace itemSpace = (ItemSpace) node;
          
          assertNull(itemSpace.getMapItem());
    	      
          itemSpace.setMapItem(new MapItem("antidote.png",
                  new Image(getClass().getResourceAsStream("antidote.png"),
                          17, 17, false, false)));
          MapItem first = itemSpace.getMapItem();
          assertNotNull(first);
          
          assertTrue(itemSpace.hasItem());
          
          itemSpace.setMapItem(null);
          assertNull(itemSpace.getMapItem());
          
          assertFalse(itemSpace.hasItem());
    	  }
    	  
    	  @Test
    	  public void testNoItemSpace() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      mapEditor.initItemSpaces(true);
    	      
    	      assertTrue(mapEditor.noItemsInItemGrid());
    	      
    	      Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getItemGrid());
          assertTrue(node instanceof ItemSpace);
          ItemSpace itemSpace = (ItemSpace) node;
              
          assertNull(itemSpace.getMapItem());
        	      
          itemSpace.setMapItem(new MapItem("antidote.png",
                 new Image(getClass().getResourceAsStream("antidote.png"),
                          17, 17, false, false)));
          
          assertFalse(mapEditor.noItemsInItemGrid());
    	      
    	  }
}
