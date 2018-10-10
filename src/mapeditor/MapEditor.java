package mapeditor;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        itemGrid.setStyle("-fx-background-color: lightpink");

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
                        "-fx-border-color: green;");

                // Add node on j column and i row
                items.add(tile, j, i);
            }
        }

        Node n = getNodeByRowColumnIndex(0,0,items);

        if(n instanceof ItemSpace){
            ItemSpace i = (ItemSpace) n;
            i.setItem(new Item("green-key.png",new Image(getClass().getResourceAsStream("green-key.png"),100, 100, false, false)));
        }

        items = setOptions(items);
        // Return the GridPane
        return items;
    }

    public GridPane setOptions(GridPane items) {

        Button pickupButton = new Button("Pick Up");
        Button dropButton = new Button("Drop");

        pickupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(selectedTile!=null && selectedTile.getItem()!=null){
                    Item i = selectedTile.getItem();
                    ItemSpace itemSpace = findFirstEmptyItem();
                    //swap items
                    String name = i.getImageName();
                    itemSpace.setItem(new Item(name, new Image(getClass().getResourceAsStream("green-key.png"),100, 100, false, false)));
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
                        selectedTile.setItem(new Item(name, new Image(getClass().getResourceAsStream("green-key.png"),20, 20, false, false)));
                        selectedItem.setItem(null);
                        selectedItem.resetImageView();
                    }
                }
            }
        });

        items.add(pickupButton,0,4);
        items.add(dropButton,1,4);

        return items;
    }

    private GridPane initBoard() {
        GridPane board = new GridPane();

        int roomNum = 3;

        for (int i = 0; i < roomNum; i++) {
            for (int j = 0; j < roomNum; j++) {

                GridPane room = initRoom();

                room.setStyle("-fx-padding: 0;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-color: black;");

                // Add node on j column and i row
                board.add(room, j, i);
            }
        }
        // Return the GridPane
        return board;
    }

    private GridPane initRoom() {
        GridPane room = new GridPane();


        int roomNum = 3;
        double roomWidth = BOARD_SIZE / roomNum;
        double roomHeight = BOARD_SIZE / roomNum;

        int tileNum = 10;
        double tileWidth = roomWidth / tileNum;
        double tileHeight = roomHeight / tileNum;

        for (int i = 0; i < tileNum; i++) {
            for (int j = 0; j < tileNum; j++) {
                MapEditor.Tile tile = new MapEditor.Tile(i, j);
                // Set each 'Tile' the width and height
                tile.setPrefSize(tileWidth, tileHeight);

                tile.setStyle("-fx-padding: 0;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-color: black;");

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

        public Tile(int x, int y) {
            positionX = x;
            positionY = y;
            imageView = new ImageView();
            setOnMouseClicked(e -> {
                if(selectedTile!=null) {
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
                System.out.println(positionX + " " + positionY);
            });
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
                System.out.println(positionX + " " + positionY);
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

}
