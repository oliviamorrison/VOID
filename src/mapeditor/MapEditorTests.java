package mapeditor;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * MapEditor Test Class holds the test suites for the MapEditor class.
 * 
 * @author oliviamorrison
 *
 */
public class MapEditorTests {


    /**
     * This tests that the board is set up correctly which rooms in all 9 spaces.
     */
    @Test
    public void testBoardSetUp(){
        MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();

        //board and item grid should be initialized
        GridPane board = mapEditor.getBoardGrid();
        assertNotNull(mapEditor.getNodeByRowColumnIndex(0,0,board));
    }

    /**
     * This tests that the map editor sets up the item spaces correctly.
     */
    @Test
    public void testItemSetUp(){
        MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();

        mapEditor.initItemSpaces(true);
        GridPane itemGrid = mapEditor.getItemGrid();

        assertNotNull(mapEditor.getNodeByRowColumnIndex(0,0,itemGrid));
    }

    /**
     * This tests that the map editor sets up the mapItem spaces correctly.
     */
    @Test
    public void testMapItem(){
        Image img = new Image(getClass().getResourceAsStream("cutters.png"));
       MapItem i = new MapItem("cutters.png", img);

       assertEquals("cutters.png", i.getImageName());
       assertEquals(img, i.getImage());

    }
    
    
    /**
     * This tests that the map editor checks if the spaceShip room correctly.
     */
    @Test
    public void testIsInSpaceShipRoom() {
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
    
    /**
     * This tests that the map editor sets up the items in t correctly
     */
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
    
    /**
     * This tests that the map editor creates the XML file and game successfully.
     */
    @Test
    public void createGameXML() {
    		MapEditor mapEditor = new MapEditor();
        mapEditor.setUp();
        mapEditor.initaliseItems();
        assertTrue(mapEditor.createGame());
        
    }
    
    /**
     * This tests that the map editor finds first empty tile successfully.
     */
    	  @Test
    	  public void testFindFirstEmptyTile() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getBoardGrid());
          assertTrue(node instanceof GridPane);
          GridPane room1 = (GridPane) node;
    	      
    	      assertNotNull(mapEditor.findFirstEmptyTile(room1));
    	  }
    	  
    	  /**
    	    * This tests that the map editor resets the image view on a tile successfully.
    	  */
    	  @Test
    	  public void testResetImageViewTile() {
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
    	  
    	  /**
   	    * This tests that the map editor resets the image view on a image space successfully.
   	  */
    	  @Test
    	  public void testResetImageViewImageSpace() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      mapEditor.initItemSpaces(true);
              
          Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getItemGrid());
          assertTrue(node instanceof ItemSpace);
          ItemSpace itemSpace = (ItemSpace) node;
          
          ImageView first = itemSpace.getImageView();
          
          itemSpace.resetImageView();
          
          ImageView second = itemSpace.getImageView();
          
          assertNotEquals(first,second);
    	      
    	  }
    	  
    	  /**
   	    * This tests that the map editor uses hasItem, getItem and setItem successfully
   	    * on an itemSpace.
   	  */
    	  @Test
    	  public void testItemSpace() {
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
    	  
    	  
    	  
    	/**
    	 * This method tests if the noItemsInItemGrid works successfully.
    	 */
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
    	  
    	/**
    	 * This method tests if the mapEditor finds the first empty item space correctly.
    	 */
    	  @Test
    	  public void testEmptyItemSpace() {
    		  MapEditor mapEditor = new MapEditor();
    	      mapEditor.setUp();
    	      
    	      mapEditor.initItemSpaces(true);
    	      Node node = mapEditor.getNodeByRowColumnIndex(0,0, mapEditor.getItemGrid());
          assertTrue(node instanceof ItemSpace);
          ItemSpace itemSpace = (ItemSpace) node;
    	      assertEquals(mapEditor.findFirstEmptyItem(),itemSpace);
    		  
    	  }
    	  
//    	  /**
//      	* This method tests if the noItemsInItemGrid works successfully.
//      */
//    	  @Test
//    	  public void testOptionGrid() {
//    		  MapEditor mapEditor = new MapEditor();
//    	      mapEditor.setUp();
//    	      mapEditor.initItemSpaces(true);
//    	      
////    	      mapEditor.setOptions(mapEditor.getItemGrid(),true);
//    	      
//    	  }
    	
}
