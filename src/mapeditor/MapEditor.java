package mapeditor;

import gameworld.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import persistence.XMLParser;

import java.io.File;

public class MapEditor extends Application {

  private final int BOARD_SIZE = 800;
  private final int ITEM_WIDTH = 200;
  private final int ITEM_HEIGHT = 800;

  private TilePane selectedTilePane;
  private ItemSpace selectedItem;

  private GridPane boardGrid;
  private GridPane mainPane;
  private GridPane itemGrid;

  @Override
  public void start(Stage primaryStage) throws Exception {

    setUp();

    itemGrid = initItemSpaces();
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

  public GridPane getBoardGrid() {
    return boardGrid;
  }

  public GridPane getItemGrid() {
    return itemGrid;
  }

  public void setUp(){
    // Initialize the grid
    boardGrid = initBoard();

    // Set the dimensions of the grid
    boardGrid.setPrefSize(BOARD_SIZE, BOARD_SIZE);


  }


  public GridPane initItemSpaces(){
    GridPane items = new GridPane();

    int rows = 2;
    int cols = 4;

    double itemWidth = ITEM_WIDTH / rows;
    double itemHeight = ITEM_HEIGHT / cols;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        MapEditor.ItemSpace tile = new MapEditor.ItemSpace(i, j);

        // Set each 'TilePane' the width and height
        tile.setPrefSize(itemWidth, itemHeight);

        tile.setStyle("-fx-padding: 0;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 0.5;" +
            "-fx-border-color: black;");

        // Add node on j column and i row
        items.add(tile, j, i);
      }
    }

    items = setOptions(items);
    // Return the GridPane
    return items;
  }

  public GridPane setOptions(GridPane items) {

    Button pickupButton = new Button("Pick Up");
    Button dropButton = new Button("Drop");
    Button makeGame = new Button("Make Game");

    pickupButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        if(selectedTilePane !=null && selectedTilePane.getMapItem()!=null){
          MapItem i = selectedTilePane.getMapItem();
          ItemSpace itemSpace = findFirstEmptyItem();
          //swap items
          String name = i.getImageName();
          itemSpace.setMapItem(new MapItem(name, new Image(getClass().getResourceAsStream(name),100, 100, false, false)));
          selectedTilePane.setMapItem(null);
          selectedTilePane.resetImageView();
        }
      }
    });

    dropButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        if(selectedItem!=null && selectedItem.getMapItem()!=null){
          MapItem i = selectedItem.getMapItem();

          //if there is nothing in the selected tile
          if(selectedTilePane.getMapItem()==null && !isInAntidoteRoom(selectedTilePane)) {
            //swap items
            String name = i.getImageName();

            if(name.equals("player.png") || name.equals("diffuser.png")) {
              String otherName;
              if(name.equals("player.png"))otherName = "diffuser.png";
              else otherName = "player.png";

              GridPane room = selectedTilePane.getRoom();
              TilePane moveTo = findFirstEmptyTile(room);
              TilePane moveFrom = findItemInBoard(otherName);

              moveTo.setMapItem(new MapItem(otherName,new Image(getClass().getResourceAsStream(otherName),20, 20, false, false)));
              moveFrom.setMapItem(null);
              moveFrom.resetImageView();
            }

            selectedTilePane.setMapItem(new MapItem(name, new Image(getClass().getResourceAsStream(name),20, 20, false, false)));
            selectedItem.setMapItem(null);
            selectedItem.resetImageView();
          }
        }
      }
    });

    makeGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        createGame();
      }
    });

    items.add(pickupButton,0,4);
    items.add(dropButton,1,4);
    items.add(makeGame, 2,4);

    return items;
  }

  private boolean isInAntidoteRoom(TilePane find){
    TilePane t = findItemInBoard("antidote.png");
    return t.getRoom() == find.getRoom();
  }

  private TilePane findItemInBoard(String name){
    for (int i = 0; i <3; i++) {
      for (int j = 0; j <3; j++) {
        Node n = getNodeByRowColumnIndex(i,j,boardGrid);
        if(n instanceof GridPane){
          GridPane room = (GridPane) n;

          for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
              Node tn = getNodeByRowColumnIndex(k,l,room);

              if(tn instanceof TilePane){
                TilePane t = (TilePane) tn;
                if(t.hasMapItem()){

                  MapItem item = t.getMapItem();

                  if(item.getImageName().equals(name)) return t;
                }

              }

            }
          }

        }
      }
    }

    return null;
  }

  private GridPane initBoard() {
    GridPane board = new GridPane();

    int roomNum = 3;

    for (int i = 0; i < roomNum; i++) {
      for (int j = 0; j < roomNum; j++) {

        //null rooms
        if(i==0&&j==2 || i==1&&j==0){
          board.add(initNullRoom(),j,i);
        }
        else {
          GridPane room = initRoom(i,j);

          room.setStyle("-fx-padding: 0;" +
              "-fx-border-style: solid inside;" +
              "-fx-border-width: 0.5;" +
              "-fx-border-color: black;");

          // Add node on j column and i row
          board.add(room, j, i);
        }
      }
    }

    // Return the GridPane
    return board;
  }


  //hard coded items
  public void initaliseItems(){

    //room 1
    Node node = getNodeByRowColumnIndex(0,0, boardGrid);
    if(node instanceof GridPane) {
      GridPane room1 = (GridPane) node;
      node = getNodeByRowColumnIndex(1, 1, room1);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("player.png",new Image(getClass().getResourceAsStream("player.png"),17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(1,2,room1);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("diffuser.png", new Image(getClass().getResourceAsStream("diffuser.png"),17,17,false,false)));

      }

    }

    //room 2
    node = getNodeByRowColumnIndex(0,1, boardGrid);
    if(node instanceof GridPane) {
      GridPane room2 = (GridPane) node;
      node = getNodeByRowColumnIndex(9, 5, room2);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("unlit-bomb.png",new Image(getClass().getResourceAsStream("unlit-bomb.png"),17, 17, false, false)));
      }
    }

    //room 5
    node = getNodeByRowColumnIndex(1,1, boardGrid);
    if(node instanceof GridPane) {
      GridPane room5 = (GridPane) node;
      node = getNodeByRowColumnIndex(7, 2, room5);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("cutters.png",new Image(getClass().getResourceAsStream("cutters.png"),17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(2,5,room5);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("healthpack.png", new Image(getClass().getResourceAsStream("healthpack.png"),17,17,false,false)));
      }

    }

    //room 6
    node = getNodeByRowColumnIndex(1,2, boardGrid);
    if(node instanceof GridPane) {
      GridPane room6 = (GridPane) node;
      node = getNodeByRowColumnIndex(2, 7, room6);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("vending-machine.png",new Image(getClass().getResourceAsStream("vending-machine.png"),17, 17, false, false)));
      }
    }

    //room 7
    node = getNodeByRowColumnIndex(2,0, boardGrid);
    if(node instanceof GridPane) {
      GridPane room7 = (GridPane) node;
      node = getNodeByRowColumnIndex(5, 5, room7);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("antidote.png",new Image(getClass().getResourceAsStream("antidote.png"),17, 17, false, false)));
        i.setAccessible(false);
      }
    }

    //room 8
    node = getNodeByRowColumnIndex(2,1, boardGrid);
    if(node instanceof GridPane) {
      GridPane room8 = (GridPane) node;
      node = getNodeByRowColumnIndex(5,0, room8);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("guard.png",new Image(getClass().getResourceAsStream("guard.png"),17, 17, false, false)));
      }
    }

    //room 9
    node = getNodeByRowColumnIndex(2,2, boardGrid);
    if(node instanceof GridPane) {
      GridPane room9 = (GridPane) node;
      node = getNodeByRowColumnIndex(5, 5, room9);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("two-coins.png",new Image(getClass().getResourceAsStream("two-coins.png"),17, 17, false, false)));
      }

      node = getNodeByRowColumnIndex(7,2,room9);

      if(node instanceof TilePane){
        TilePane i = (TilePane) node;
        i.setMapItem(new MapItem("healthpack.png", new Image(getClass().getResourceAsStream("healthpack.png"),17,17,false,false)));
      }

    }

  }

  private GridPane initNullRoom(){
    GridPane room = new GridPane();
    int roomNum = 3;
    double roomWidth = BOARD_SIZE / roomNum;
    double roomHeight = BOARD_SIZE / roomNum;

    room.setStyle("-fx-background-color: gray");
    return room;
  }

  private GridPane initRoom(int row, int col) {
    GridPane room = new GridPane();


    int roomNum = 3;
    double roomWidth = BOARD_SIZE / roomNum;
    double roomHeight = BOARD_SIZE / roomNum;

    int tileNum = 10;
    double tileWidth = roomWidth / tileNum;
    double tileHeight = roomHeight / tileNum;

    for (int i = 0; i < tileNum; i++) {
      for (int j = 0; j < tileNum; j++) {

        boolean accessible = true;
        boolean door = false;

        if(i==0||j==0||i==9||j==9){
          accessible = false;
          if(j==5 || i==5){

            //hard coded null rooms
            if(row==2 && col ==0 && i==0|| row==0 && col ==0 && i==9 || col==1 && row==1 && j==0)  door = false;
            else if(row==0 && col == 1 && j==9 || row==1 && col==2 && i==0) door =false;

            else if(row==0 && i==0 || row==2 && i==9) door = false;
            else if(col==0 && j==0 || col==2 && j==9) door = false;

            else door = true;
          }
        }

        TilePane tilePane = new TilePane(i, j, accessible, room);
        // Set each 'TilePane' the width and height
        tilePane.setPrefSize(tileWidth, tileHeight);

        if(accessible) {
          tilePane.setStyle("-fx-padding: 0;" +
              "-fx-border-style: solid inside;" +
              "-fx-border-width: 0.5;" +
              "-fx-border-color: black;");
        } else if(door){
          tilePane.setStyle("-fx-padding: 0;" +
              "-fx-background-color: white");
        }
        //wall
        else {
          tilePane.setStyle("-fx-padding: 0;" +
              "-fx-background-color: lightgray");
        }
        // Add node on j column and i row
        room.add(tilePane, j, i);
      }
    }



    // Return the GridPane
    return room;
  }

  public ItemSpace findFirstEmptyItem(){
    ObservableList<Node> children = itemGrid.getChildren();

    for (Node node : children) {
      if(node instanceof ItemSpace) {
        ItemSpace i = (ItemSpace) node;
        if(!i.hasItem()) return i;
      }
    }

    return null;
  }

  public TilePane findFirstEmptyTile(GridPane room){
    ObservableList<Node> children = room.getChildren();

    for (Node node : children) {
      if(node instanceof TilePane) {
        TilePane i = (TilePane) node;
        if(!i.hasMapItem() && i.isAccessible()) return i;
      }
    }

    return null;
  }

  public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
    Node result = null;
    ObservableList<Node> childrens = gridPane.getChildren();

    for (Node node : childrens) {
      if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
        result = node;
        break;
      }
    }

    return result;
  }

  class TilePane extends Pane {
    private int positionX;
    private int positionY;
    private MapItem mapItem;
    private ImageView imageView;
    private boolean accessible;
    private GridPane room;

    public TilePane(int x, int y, boolean a, GridPane r) {
      positionX = x;
      positionY = y;
      imageView = new ImageView();
      accessible = a;
      room = r;

        setOnMouseClicked(e -> {
          if(accessible) {
            if (selectedTilePane != null) {
              selectedTilePane.setStyle("-fx-padding: 0;" +
                      "-fx-border-style: solid inside;" +
                      "-fx-border-width: 0.5;" +
                      "-fx-border-color: black;");
            }

            selectedTilePane = this;
            this.setStyle("-fx-padding: 0;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-color: red;");
          }
        });

    }

    public GridPane getRoom() {
      return room;
    }

    public boolean isAccessible(){
      return accessible;
    }

    public void setAccessible(boolean b){
      accessible = b;
    }

    public void resetImageView(){
      this.getChildren().remove(imageView);
      imageView = new ImageView();
    }

    public MapItem getMapItem() {
      return mapItem;
    }

    public boolean hasMapItem(){
      return mapItem!=null;
    }

    public void setMapItem(MapItem mapItem) {
      this.mapItem = mapItem;

      if(mapItem !=null) {
        imageView = new ImageView(mapItem.getImage());
        this.getChildren().add(imageView);
      }
    }
  }

  class ItemSpace extends Pane {
    private int positionX;
    private int positionY;
    private MapItem mapItem;
    private ImageView imageView;

    public ItemSpace(int x, int y) {
      positionX = x;
      positionY = y;
      imageView = new ImageView();
      setOnMouseClicked(e -> {
        if(selectedItem!=null) {
          selectedItem.setStyle("-fx-padding: 0;" +
              "-fx-border-style: solid inside;" +
              "-fx-border-width: 0.5;" +
              "-fx-border-color: black;");
        }

        selectedItem = this;
        this.setStyle("-fx-padding: 0;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-color: red;");
      });
    }

    public MapItem getMapItem() {
      return mapItem;
    }

    public void resetImageView(){
      this.getChildren().remove(imageView);
      imageView = new ImageView();
    }

    public void setMapItem(MapItem mapItem) {
      this.mapItem = mapItem;

      if(mapItem !=null) {
        imageView = new ImageView(mapItem.getImage());

        this.getChildren().add(imageView);
      }
    }

    public boolean hasItem(){
      return mapItem !=null;
    }
  }

  public void createGame(){
    Room[][] board = new Room[3][3];

    //Iterate through rooms
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){

        Node roomNode = getNodeByRowColumnIndex(i,j,boardGrid);
        Room room = new Room();
        board[i][j] = room;

        if(roomNode instanceof GridPane){
          GridPane roomGrid = (GridPane) roomNode;


          for(int k=0; k<10; k++){
            for(int l=0; l<10;l++){

              Node t = getNodeByRowColumnIndex(k,l,roomGrid);

              if(t instanceof TilePane){

                TilePane tilePane = (TilePane) t;
                if(tilePane.isAccessible()){
                  AccessibleTile tile = new AccessibleTile(k, l);
                  MapItem mapItem = tilePane.getMapItem();
                  if(mapItem!=null){
                    Item item = null;
                    ChallengeItem challenge = null;

                    switch(mapItem.getImageName()){
                      case "antidote.png":
                        item = new Antidote(k, l);
                        break;
                      case "cutters.png":
                        item = new BoltCutter(k, l);
                        break;
                      case "diffuser.png":
                        item = new Diffuser(k, l);
                        break;
                      case "two-coins.png":
                        item = new Coin(k, l);
                        break;
                      case "healthpack.png":
                        item = new HealthPack(k, l);
                        break;
                      case "guard.png":
                        challenge = new Guard(k,l);
                        break;
                      case "unlit-bomb.png":
                        challenge = new Bomb(k,l);
                        break;
                      case "vending-machine.png":
                        challenge = new VendingMachine(k,l);
                        break;
                    }

                    tile.setItem(item); //TODO: Do we need to check if item & challenge is null before setting it
                    tile.setChallenge(challenge);

                    room.setTile(tile, k,l);
                  }
                }
                else{
                  room.setTile(new InaccessibleTile(k, l),k,l);
                }
              }

            }
          }

        }

      }
    }

    //HARDCODED FOR NOW TO TEST ROOMS ARE LOADED
    // TODO: Decide which default direction player should be created with (currently NORTH)
    Player player = new Player(board[0][0], (AccessibleTile) board[0][0].getTile(8,8), 100, Direction.NORTH);

    Game game = new Game(board, player);

    XMLParser.saveFile(new File("data/testMapEditor.xml"), game);
  }

}
