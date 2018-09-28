package mapeditor;

import gameworld.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
        setUpMenu();

        grid = new GridPane();
        grid.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);

        // Create the Scene
        Scene scene = new Scene(grid);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Map Editor");
        // Display the Stage
        stage.show();

    }

    public void setUpMenu(){
        // create the menu bar
        MenuBar menuBar = new MenuBar();
        HBox hBox = new HBox(menuBar);
        menuBar.setPrefWidth(WINDOW_WIDTH);

        // menu bar items
        Menu newGame = new Menu("Play Game");
        menuBar.getMenus().add(newGame);
        Menu quitGame = new Menu("Quit");
        menuBar.getMenus().add(quitGame);

        grid.add(hBox, 0, 0);
    }

    public void addKeyRoom(int x, int y){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: lightpink;");
        grid.add(hBox,x,y);
    }

    public void addBombRoom(int x, int y){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: darkorange;");
        grid.add(hBox,x,y);
    }

    public void addGoalRoom(int x, int y){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: cadetblue;");
        grid.add(hBox,x,y);
    }

    public void addStartRoom(int x, int y){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: indianred;");
        grid.add(hBox,x,y);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
