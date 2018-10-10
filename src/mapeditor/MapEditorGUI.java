package mapeditor;

import gameworld.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;

public class MapEditorGUI extends Application{
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private GridPane grid;
    private Game game;


    @Override
    public void start(Stage stage) {

        grid = new GridPane();
        grid.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setUpMenu();
        setUpMap();

        grid.setStyle("-fx-background-color: blue;");

        // Create the Scene
        Scene scene = new Scene(grid);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Map Editor");
        // Display the Stage
        stage.show();

    }

    public void setUpMap(){

        GridPane startRoom = new GridPane();
        startRoom.add(addStartRoom(), 0, 0);
        startRoom.setStyle("-fx-background-color: darkorange;");
        startRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(startRoom, 0, 0);

        GridPane yellowKeyRoom = new GridPane();
        yellowKeyRoom.add(addYellowKeyRoom(), 0, 0);
        yellowKeyRoom.setStyle("-fx-background-color: yellow;");
        yellowKeyRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(yellowKeyRoom, 1, 0);

        GridPane blankRoom1 = new GridPane();
        blankRoom1.add(addBlankRoom(), 0, 0);
        blankRoom1.setStyle("-fx-background-color: gray;");
        blankRoom1.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(blankRoom1, 2, 0);

        GridPane greenKeyRoom = new GridPane();
        greenKeyRoom.add(addGreenKeyRoom(), 0, 0);
        greenKeyRoom.setStyle("-fx-background-color: green;");
        greenKeyRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(greenKeyRoom, 0, 1);

        GridPane blankRoom2 = new GridPane();
        blankRoom2.add(addBlankRoom(), 0, 0);
        blankRoom2.setStyle("-fx-background-color: gray;");
        blankRoom2.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(blankRoom2, 1, 1);

        GridPane redKeyRoom = new GridPane();
        redKeyRoom.add(addRedKeyRoom(), 0, 0);
        redKeyRoom.setStyle("-fx-background-color: red;");
        redKeyRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(redKeyRoom, 2, 1);

        GridPane blankRoom3 = new GridPane();
        blankRoom3.add(addBlankRoom(), 0, 0);
        blankRoom3.setStyle("-fx-background-color: gray;");
        blankRoom3.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(blankRoom3, 0, 2);

        GridPane bombRoom = new GridPane();
        bombRoom.add(addBombRoom(), 0, 0);
        bombRoom.setStyle("-fx-background-color: lightpink;");
        bombRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(bombRoom, 1, 2);

        GridPane endRoom = new GridPane();
        endRoom.add(addEndRoom(), 0, 0);
        endRoom.setStyle("-fx-background-color: cadetblue;");
        endRoom.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        grid.add(endRoom, 2, 2);

    }

    public void setUpMenu(){
        // create the menu bar
        MenuBar menuBar = new MenuBar();
        HBox hBox = new HBox(menuBar);
//        menuBar.setPrefWidth(WINDOW_WIDTH);

        // menu bar items
        Menu newGame = new Menu("Play Game");
        menuBar.getMenus().add(newGame);
        Menu quitGame = new Menu("Quit");
        menuBar.getMenus().add(quitGame);

        grid.add(hBox, 0, 0);
    }

    public GridPane addBlankRoom(){
        GridPane blankRoom = new GridPane();
        blankRoom.setStyle("-fx-background-color: lightpink;");
        return blankRoom;
    }

    public HBox addRedKeyRoom(){
        HBox keyRoom = new HBox();
        Image image = new Image(getClass().getResourceAsStream("red-key.png"));
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitHeight(WINDOW_HEIGHT/3*0.3);
        iv1.setFitWidth(WINDOW_WIDTH/3*0.3);

        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        keyRoom.getChildren().add(iv1);
        return keyRoom;
    }

    public GridPane addGreenKeyRoom(){
        GridPane keyRoom = new GridPane();
        Image image = new Image(getClass().getResourceAsStream("green-key.png"));
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitHeight(WINDOW_HEIGHT/3*0.3);
        iv1.setFitWidth(WINDOW_WIDTH/3*0.3);

        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        keyRoom.getChildren().add(iv1);
        return keyRoom;
    }

    public HBox addYellowKeyRoom(){
        HBox keyRoom = new HBox();
        Image image = new Image(getClass().getResourceAsStream("key.png"));
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitHeight(WINDOW_HEIGHT/3*0.3);
        iv1.setFitWidth(WINDOW_WIDTH/3*0.3);

        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        keyRoom.getChildren().add(iv1);
        return keyRoom;
    }

    public HBox addBombRoom(){
        HBox bombRoom = new HBox();
        Image image = new Image(getClass().getResourceAsStream("unlit-bomb.png"));
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitHeight(WINDOW_HEIGHT/3*0.3);
        iv1.setFitWidth(WINDOW_WIDTH/3*0.3);

        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        bombRoom.getChildren().add(iv1);
        return bombRoom;
    }

    public HBox addEndRoom(){
        HBox endRoom = new HBox();
        return endRoom;
    }

    public HBox addStartRoom(){
        HBox startRoom = new HBox();

        return startRoom;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
