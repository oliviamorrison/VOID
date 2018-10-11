package mapeditor;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MapEditor extends Application {

    private final int BOARD_SIZE = 800;
    private final int ITEM_WIDTH = 200;
    private final int ITEM_HEIGHT = 800;

    private Tile selectedTile;
    private ItemSpace selectedItem;

    private GridPane boardGrid;
    private GridPane mainPane;
    private GridPane itemGrid;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the grid
        boardGrid = initBoard();

        // Set the dimensions of the grid
        boardGrid.setPrefSize(BOARD_SIZE, BOARD_SIZE);

        itemGrid = initItem();
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


    private GridPane initItem(){
        GridPane items = new GridPane();

        int rows = 2;
        int cols = 4;

        double itemWidth = ITEM_WIDTH / rows;
        double itemHeight = ITEM_HEIGHT / cols;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                MapEditor.ItemSpace tile = new MapEditor.ItemSpace(i, j);

                // Set each 'Tile' the width and height
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
                if(selectedTile!=null && selectedTile.getItem()!=null){
                    Item i = selectedTile.getItem();
                    ItemSpace itemSpace = findFirstEmptyItem();
                    //swap items
                    String name = i.getImageName();
                    itemSpace.setItem(new Item(name, new Image(getClass().getResourceAsStream(name),100, 100, false, false)));
                    selectedTile.setItem(null);
                    selectedTile.resetImageView();
                }
            }
        });

        dropButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(selectedItem!=null && selectedItem.getItem()!=null){
                    Item i = selectedItem.getItem();

                    //if there is nothing in the selected tile
                    if(selectedTile.getItem()==null) {
                        //swap items
                        String name = i.getImageName();
                        selectedTile.setItem(new Item(name, new Image(getClass().getResourceAsStream(name),20, 20, false, false)));
                        selectedItem.setItem(null);
                        selectedItem.resetImageView();
                    }
                }
            }
        });

        items.add(pickupButton,0,4);
        items.add(dropButton,1,4);
        items.add(makeGame, 2,4);

        return items;
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

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("player.png",new Image(getClass().getResourceAsStream("player.png"),17, 17, false, false)));
            }
        }

        //room 2
        node = getNodeByRowColumnIndex(0,1, boardGrid);
        if(node instanceof GridPane) {
            GridPane room2 = (GridPane) node;
            node = getNodeByRowColumnIndex(9, 5, room2);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("unlit-bomb.png",new Image(getClass().getResourceAsStream("unlit-bomb.png"),17, 17, false, false)));
            }
        }

        //room 5
        node = getNodeByRowColumnIndex(1,1, boardGrid);
        if(node instanceof GridPane) {
            GridPane room5 = (GridPane) node;
            node = getNodeByRowColumnIndex(7, 2, room5);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("cutters.png",new Image(getClass().getResourceAsStream("cutters.png"),17, 17, false, false)));
            }
        }

        //room 6
        node = getNodeByRowColumnIndex(1,2, boardGrid);
        if(node instanceof GridPane) {
            GridPane room6 = (GridPane) node;
            node = getNodeByRowColumnIndex(2, 7, room6);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("vending-machine.png",new Image(getClass().getResourceAsStream("vending-machine.png"),17, 17, false, false)));
            }
        }

        //room 7
        node = getNodeByRowColumnIndex(2,0, boardGrid);
        if(node instanceof GridPane) {
            GridPane room7 = (GridPane) node;
            node = getNodeByRowColumnIndex(5, 5, room7);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("antidote.png",new Image(getClass().getResourceAsStream("antidote.png"),17, 17, false, false)));
            }
        }

        //room 8
        node = getNodeByRowColumnIndex(2,1, boardGrid);
        if(node instanceof GridPane) {
            GridPane room8 = (GridPane) node;
            node = getNodeByRowColumnIndex(5,0, room8);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("guard.png",new Image(getClass().getResourceAsStream("guard.png"),17, 17, false, false)));
            }
        }

        //room 9
        node = getNodeByRowColumnIndex(2,2, boardGrid);
        if(node instanceof GridPane) {
            GridPane room9 = (GridPane) node;
            node = getNodeByRowColumnIndex(5, 5, room9);

            if(node instanceof Tile){
                Tile i = (Tile) node;
                i.setItem(new Item("two-coins.png",new Image(getClass().getResourceAsStream("two-coins.png"),17, 17, false, false)));
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

                MapEditor.Tile tile = new MapEditor.Tile(i, j, accessible);
                // Set each 'Tile' the width and height
                tile.setPrefSize(tileWidth, tileHeight);

                if(accessible) {
                    tile.setStyle("-fx-padding: 0;" +
                            "-fx-border-style: solid inside;" +
                            "-fx-border-width: 0.5;" +
                            "-fx-border-color: black;");
                } else if(door){
                    tile.setStyle("-fx-padding: 0;" +
                            "-fx-background-color: white");
                }
                //wall
                else {
                    tile.setStyle("-fx-padding: 0;" +
                            "-fx-background-color: lightgray");
                }
                // Add node on j column and i row
                room.add(tile, j, i);
            }
        }



        // Return the GridPane
        return room;
    }

    public ItemSpace findFirstEmptyItem(){
        ObservableList<Node> childrens = itemGrid.getChildren();

        for (Node node : childrens) {
            if(node instanceof ItemSpace) {
                ItemSpace i = (ItemSpace) node;
                if(!i.hasItem()) return i;
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

    class Tile extends Pane {
        private int positionX;
        private int positionY;
        private Item item;
        private ImageView imageView;
        private boolean accessible;

        public Tile(int x, int y, boolean a) {
            positionX = x;
            positionY = y;
            imageView = new ImageView();
            accessible = a;

            if(accessible) {
                setOnMouseClicked(e -> {
                    if (selectedTile != null) {
                        selectedTile.setStyle("-fx-padding: 0;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 0.5;" +
                                "-fx-border-color: black;");
                    }

                    selectedTile = this;
                    this.setStyle("-fx-padding: 0;" +
                            "-fx-border-style: solid inside;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-color: red;");
                });
            }
        }

        public boolean getAccessible(){
            return accessible;
        }

        public void resetImageView(){
            this.getChildren().remove(imageView);
            imageView = new ImageView();
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;

            if(item!=null) {
                imageView = new ImageView(item.getImage());
                this.getChildren().add(imageView);
            }
        }
    }

    class ItemSpace extends Pane {
        private int positionX;
        private int positionY;
        private Item item;
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

        public Item getItem() {
            return item;
        }

        public void resetImageView(){
            this.getChildren().remove(imageView);
            imageView = new ImageView();
        }

        public void setItem(Item item) {
            this.item = item;

            if(item!=null) {
                imageView = new ImageView(item.getImage());

                this.getChildren().add(imageView);
            }
        }

        public boolean hasItem(){
            return item!=null;
        }
    }

    public void createGame(){

    }

}
