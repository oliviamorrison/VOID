package mapeditor;

import gameworld.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import persistence.XmlParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


/**
 * MapEditor class displays a stand alone application allowing one to
 * create and edit map files. This should display the map being edited in a top-down graphical
 * fashion, allowing the user to add and remove items.
 *
 * @author oliviamorrison
 *
 */
public class MapEditor extends Application {

  private final static int boardSize = 800;
  private final static int itemWidth = 200;
  private final static int itemHeight = 800;

  /**
 * The tilePane which is currently selected.
 */
public TilePane selectedTilePane;

/**
 * The ItemSpace which is currently selected.
 */
public ItemSpace selectedItem;

  private GridPane boardGrid;
  private GridPane mainPane;
  private GridPane itemGrid;

  @Override
  public void start(Stage primaryStage) throws Exception {

    setUp();

    itemGrid = initItemSpaces(false);
    itemGrid.setStyle("-fx-background-color: lightblue");

    initaliseItems();

    // Use a StackPane to display the Image and the Grid
    mainPane = new GridPane();
    mainPane.add(boardGrid, 0,0);
    mainPane.add(itemGrid,1,0);

    primaryStage.setScene(new Scene(mainPane));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * This method returns the current board grid. Used for testing purposes.
 * @return GridPane of the boardGrid
 */
  public GridPane getBoardGrid() {
    return boardGrid;
  }

  /**
   * This method returns the current item grid. Used for testing purposes.
 * @return GridPane of the itemGrid
 */
  public GridPane getItemGrid() {
    return itemGrid;
  }

  /**
 * This method sets up the board with rooms in it and sets the preferable size.
 *
 */
  public void setUp() {

    // Initialize the grid
    boardGrid = initBoard();

    // Set the dimensions of the grid
    boardGrid.setPrefSize(boardSize, boardSize);


  }


  /**
   * This method initialized item spaces on the right side of the panel.
   *
 * @param test boolean which represents if a test is calling the method
 * @return the current state of the item spaces GridPane
 */
public GridPane initItemSpaces(boolean test) {
    GridPane items = new GridPane();

    int rows = 2;
    int cols = 4;

    double itemWidth = this.itemWidth / rows;
    double itemHeight = this.itemHeight / cols;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        ItemSpace tile = new ItemSpace(i, j,this);

        // Set each 'TilePane' the width and height
        tile.setPrefSize(itemWidth, itemHeight);

        tile.setStyle("-fx-padding: 0;"
            + "-fx-border-style: solid inside;"
            + "-fx-border-width: 0.5;"
            + "-fx-border-color: black;");

        // Add node on j column and i row
        items.add(tile, j, i);
      }
    }

    if (!test) {
      items = setOptions(items, false);
    } else {
      itemGrid = items;
    }
    // Return the GridPane
    return items;
  }

  /**
   * This method adds the option buttons on the end of the item spaces.
   *
 * @param items GridPane of the current state of the items gridPane
 * @param test boolean which represents if a test is calling the method
 * @return GridPane of the itemSpaces plus the buttons
 */
public GridPane setOptions(GridPane items, boolean test ) {

    Button pickupButton = new Button("Pick Up");
    Button dropButton = new Button("Drop");
    Button makeGame = new Button("Make Game");

    pickupButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        if (selectedTilePane != null && selectedTilePane.getMapItem() != null) {
          MapItem i = selectedTilePane.getMapItem();
          ItemSpace itemSpace = findFirstEmptyItem();
          //swap items
          String name = i.getImageName();
          itemSpace.setMapItem(new MapItem(name, new Image(getClass().getResourceAsStream(name),
                  100, 100, false, false)));
          selectedTilePane.setMapItem(null);
          selectedTilePane.resetImageView();
        }
      }
    });

    dropButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        if (selectedItem != null && selectedItem.getMapItem() != null) {
          MapItem i = selectedItem.getMapItem();

          //if there is nothing in the selected tile
          if (selectedTilePane.getMapItem() == null && !isInAntidoteRoom(selectedTilePane)) {
            //swap items
            String name = i.getImageName();

            if (name.equals("player.png") || name.equals("diffuser.png")) {
              String otherName;
              if (name.equals("player.png")) {
                otherName = "diffuser.png";
              } else {
                otherName = "player.png";
              }

              GridPane room = selectedTilePane.getRoom();
              TilePane moveTo = findFirstEmptyTile(room);
              TilePane moveFrom = findItemInBoard(otherName);

              moveTo.setMapItem(new MapItem(otherName,
                      new Image(getClass().getResourceAsStream(otherName),
                      20, 20, false, false)));
              moveFrom.setMapItem(null);
              moveFrom.resetImageView();
            }

            selectedTilePane.setMapItem(new MapItem(name,
                    new Image(getClass().getResourceAsStream(name),
                    20, 20, false, false)));
            selectedItem.setMapItem(null);
            selectedItem.resetImageView();
          }
        }
      }
    });

    makeGame.setOnAction(event -> {
      if (noItemsInItemGrid()) {
        createGame();
      } else {
    	  	//if there are still items in the item spaces panel
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("You cannot make a game until all items are placed on the board");

        alert.showAndWait();
      }
    });

    if(!test) {
    		items.add(pickupButton,0,4);
    		items.add(dropButton,1,4);
    		items.add(makeGame, 2,4);
    }

    return items;
  }

  /**
   * This method returns whether the item spaces is empty or not.
   *
 * @return boolean of if there are items in the item grid
 */
public boolean noItemsInItemGrid() {
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 4; j++) {
        Node n = getNodeByRowColumnIndex(i, j, itemGrid);
        if (n instanceof ItemSpace) {
          ItemSpace itemSpace = (ItemSpace) n;
          if (itemSpace.hasItem()) {
            return false;
          }
        }
      }
    }

    return true;

  }

  /**
   *  This method checks if tile which is passed is in the same room as the spaceship.
   *
 * @param find TilePane to check if the spaceship is in this room
 * @return boolean which represents if the spaceship is in the room
 */
public boolean isInAntidoteRoom(TilePane find) {
    TilePane t = findItemInBoard("antidote.png");
    return t.getRoom() == find.getRoom();
  }

  /**
   * This method finds a given item in the board and returns which tile it is on.
   *
 * @param name a string which represents the name of the item which is being looked for.
 * @return TilePane the tile which the item is on.
 */
private TilePane findItemInBoard(String name) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        Node n = getNodeByRowColumnIndex(i,j,boardGrid);
        if (n instanceof GridPane) {
          GridPane room = (GridPane) n;

          for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
              Node tn = getNodeByRowColumnIndex(k,l,room);

              if (tn instanceof TilePane) {
                TilePane t = (TilePane) tn;
                if (t.hasMapItem()) {

                  MapItem item = t.getMapItem();

                  if (item.getImageName().equals(name)) {
                    return t;
                  }
                }

              }

            }
          }

        }
      }
    }

    return null;
  }

  /**
   * This method initializes the board to have 9 rooms.
 * @return GridPane the full initialized board
 */
private GridPane initBoard() {
    GridPane board = new GridPane();

    int roomNum = 3;

    for (int i = 0; i < roomNum; i++) {
      for (int j = 0; j < roomNum; j++) {

        //null rooms
        if (i == 0 && j == 2 || i == 1 && j == 0) {
          board.add(initNullRoom(),j,i);
        } else {
          GridPane room = initRoom(i,j);

          room.setStyle("-fx-padding: 0;"
              + "-fx-border-style: solid inside;"
              + "-fx-border-width: 0.5;"
              + "-fx-border-color: black;");

          // Add node on j column and i row
          board.add(room, j, i);
        }
      }
    }

    // Return the GridPane
    return board;
  }



  /**
 * This method sets all of the items in their default tiles. Therefore initializing the items.
 */
public void initaliseItems() {

    //room 1
    Node node = getNodeByRowColumnIndex(0,0, boardGrid);
    if (node instanceof GridPane) {
      GridPane room1 = (GridPane) node;
      node = getNodeByRowColumnIndex(1, 1, room1);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("player.png",
                new Image(getClass().getResourceAsStream("player.png"),
                        17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(1,2,room1);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("diffuser.png",
                new Image(getClass().getResourceAsStream("diffuser.png"),
                        17,17,false,false)));

      }

    }

    //room 2
    node = getNodeByRowColumnIndex(0,1, boardGrid);
    if (node instanceof GridPane) {
      GridPane room2 = (GridPane) node;
      node = getNodeByRowColumnIndex(8, 5, room2);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("unlit-bomb.png",
                new Image(getClass().getResourceAsStream("unlit-bomb.png"),
                        17, 17, false, false)));
      }
    }

    //room 5
    node = getNodeByRowColumnIndex(1,1, boardGrid);
    if (node instanceof GridPane) {
      GridPane room5 = (GridPane) node;
      node = getNodeByRowColumnIndex(7, 2, room5);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("cutters.png",
                new Image(getClass().getResourceAsStream("cutters.png"),
                        17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(2,5,room5);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("healthpack.png",
                new Image(getClass().getResourceAsStream("healthpack.png"),
                        17,17,false,false)));
      }

    }

    //room 6
    node = getNodeByRowColumnIndex(1,2, boardGrid);
    if (node instanceof GridPane) {
      GridPane room6 = (GridPane) node;
      node = getNodeByRowColumnIndex(2, 7, room6);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("vending-machine.png",
                new Image(getClass().getResourceAsStream("vending-machine.png"),
                        17, 17, false, false)));
      }
    }

    //room 7
    node = getNodeByRowColumnIndex(2,0, boardGrid);
    if (node instanceof GridPane) {
      GridPane room7 = (GridPane) node;
      node = getNodeByRowColumnIndex(5, 5, room7);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("antidote.png",
                new Image(getClass().getResourceAsStream("antidote.png"),
                        17, 17, false, false)));
        i.setAccessible(false);
      }
    }

    //room 8
    node = getNodeByRowColumnIndex(2,1, boardGrid);
    if (node instanceof GridPane) {
      GridPane room8 = (GridPane) node;
      node = getNodeByRowColumnIndex(5,1, room8);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("guard.png",
                new Image(getClass().getResourceAsStream("guard.png"),
                        17, 17, false, false)));
      }
    }

    //room 9
    node = getNodeByRowColumnIndex(2,2, boardGrid);
    if (node instanceof GridPane) {
      GridPane room9 = (GridPane) node;
      node = getNodeByRowColumnIndex(5, 5, room9);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("two-coins.png",
                new Image(getClass().getResourceAsStream("two-coins.png"),
                        17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(7,2,room9);

      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("healthpack.png",
                new Image(getClass().getResourceAsStream("healthpack.png"),
                        17,17,false,false)));
      }

    }

  }

  /**
   * This method initializes a null room which does not have tiles in it.
 * @return GridPane a null room.
 */
private GridPane initNullRoom() {
    GridPane room = new GridPane();

    room.setStyle("-fx-background-color: gray");
    return room;
  }

  /**
   * This method creates a GridPane which initializes all of the rooms with
   * 100 tiles in each room.
   *
 * @param row
 * @param col
 * @return GridPane the grid of all tiles in the room
 */
private GridPane initRoom(int row, int col) {
    GridPane room = new GridPane();


    int roomNum = 3;
    double roomWidth = boardSize / roomNum;
    double roomHeight = boardSize / roomNum;

    int tileNum = 10;
    double tileWidth = roomWidth / tileNum;
    double tileHeight = roomHeight / tileNum;

    for (int i = 0; i < tileNum; i++) {
      for (int j = 0; j < tileNum; j++) {

        boolean accessible = true;
        boolean door = false;
        String direction = "";

        //if tile is on the outside perimeter
        if (i == 0 || j == 0 || i == 9 || j == 9) {
          //it is a wall
          accessible = false;
          //if either is 5 then it is a door
          if (j == 5 || i == 5) {

            //if its on the perimeter of the board its not a door
            if (row == 2 && col == 0 && i == 0 || row == 0 && col == 0 && i == 9
                    || col == 1 && row == 1 && j == 0) {
              door = false;
            } else if (row == 0 && col == 1 && j == 9 || row == 1 && col == 2 && i == 0) {
              door = false;
            } else if (row == 0 && i == 0 || row == 2 && i == 9) {
              door = false;
            } else if (col == 0 && j == 0 || col == 2 && j == 9) {
              door = false;
            } else {
              door = true;
              if (i == 0){
                direction = "NORTH";
              } else if (i == 9){
                direction = "SOUTH";
              } else if (j == 0){
                direction  = "WEST";
              } else {
                direction = "EAST";
              }
            }
          }
        }

        boolean challenge = false;
        if ((row == 0 && col == 1 && i == 8 && j == 5) || (row == 2 && col == 1 && i == 5 && j == 1)) {
          challenge = true;
        }


        TilePane tilePane = new TilePane(accessible, room, door, direction, challenge, this);
        // Set each 'TilePane' the width and height
        tilePane.setPrefSize(tileWidth, tileHeight);

        if (accessible) {
          tilePane.setStyle("-fx-padding: 0;"
              + "-fx-border-style: solid inside;"
              + "-fx-border-width: 0.5;"
              + "-fx-border-color: black;");
        } else if (door) {
          tilePane.setStyle("-fx-padding: 0;"
              + "-fx-background-color: white");
        } else {
          tilePane.setStyle("-fx-padding: 0;"
              + "-fx-background-color: lightgray");
        }
        // Add node on j column and i row
        room.add(tilePane, j, i);
      }
    }



    // Return the GridPane
    return room;
  }

  /**
   * This method finds and returns the first empty ItemSpace.
   *
 * @return ItemSpace
 */
public ItemSpace findFirstEmptyItem() {
    ObservableList<Node> children = itemGrid.getChildren();

    for (Node node : children) {
      if (node instanceof ItemSpace) {
        ItemSpace i = (ItemSpace) node;
        if (!i.hasItem()) {
          return i;
        }
      }
    }

    return null;
  }

  /**
   * This method finds and returns the first empty tile in a given room.
   *
 * @param room
 * @return TilePane
 */
public TilePane findFirstEmptyTile(GridPane room) {
    ObservableList<Node> children = room.getChildren();

    for (Node node : children) {
      if (node instanceof TilePane) {
        TilePane i = (TilePane) node;
        if (!i.hasMapItem() && i.isAccessible()) {
          return i;
        }
      }
    }

    return null;
  }

  /**
   * This method returns the node at the given row and column of the given gridPane.
   *
 * @param row
 * @param column
 * @param gridPane
 * @return Node
 */
public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
    Node result = null;
    ObservableList<Node> childrens = gridPane.getChildren();

    for (Node node : childrens) {
      if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
        result = node;
        break;
      }
    }

    return result;
  }

  /**
   * This method creates a game from the current state of the board and creates an XML file
   * from that game to be able to run from the application window.
   *
 * @return boolean to check if create game was successfully loaded.
 */
public boolean createGame() {
    Room[][] board = new Room[3][3];
    Player player = null;

    //Iterate through rooms
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        Node roomNode = getNodeByRowColumnIndex(i,j,boardGrid);
        Room room = new Room(i,j);
        board[i][j] = room;
        List<String> doors = new ArrayList<>();

        if (roomNode instanceof GridPane) {
          GridPane roomGrid = (GridPane) roomNode;


          for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10;l++) {

              Node t = getNodeByRowColumnIndex(k,l,roomGrid);

              if (t instanceof TilePane) {

                TilePane tilePane = (TilePane) t;
                if (tilePane.isAccessible()) {
                  AccessibleTile tile = new AccessibleTile(k, l);
                  MapItem mapItem = tilePane.getMapItem();
                  if (mapItem != null) {
                    Item item = null;
                    ChallengeItem challenge = null;

                    switch (mapItem.getImageName()) {
                      case "antidote.png":
                        item = new SpaceShip(k, l, "NORTH");
                        break;
                      case "cutters.png":
                        item = new BoltCutter(k, l, "NORTH");
                        break;
                      case "diffuser.png":
                        item = new Diffuser(k, l, "NORTH");
                        break;
                      case "two-coins.png":
                        item = new Coin(k, l, "NORTH");
                        break;
                      case "healthpack.png":
                        item = new OxygenTank(k, l, "NORTH");
                        break;
                      case "guard.png":
                        challenge = new Alien(k,l, "NORTH");
                        break;
                      case "unlit-bomb.png":
                        challenge = new Bomb(k,l, "NORTH");
                        break;
                      case "vending-machine.png":
                        challenge = new VendingMachine(k,l, "WESTind");
                        break;
                      case "player.png":
                        player = new Player(room,tile,100,"NORTH");
                      default:
                        continue;
                    }

                    if (mapItem.getImageName() != null) {
                      tile.setItem(item);
                      tile.setChallenge(challenge);
                      System.out.println(mapItem.getImageName());
                    }

                    room.setTile(tile, k,l);
                  }
                } else {
                  //if it is a door add its direction
                  if (tilePane.isDoor()) {
                    doors.add(tilePane.getDirection());
                  }

                  room.setTile(new InaccessibleTile(k, l),k,l);
                }
              }

            }
          }

          room.getDoors().addAll(doors);

        }

      }
    }

    Game game = new Game(board, player);

    try {
      XmlParser.saveFile(new File("data/testMapEditor.xml"), game);
      return true;
    } catch (ParserConfigurationException | TransformerException e) {
      e.printStackTrace();
      return false;
    }
  }

}
