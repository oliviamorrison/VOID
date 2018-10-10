package mapeditor;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MapEditorTest extends Application {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private GridPane houseGrid;

    @Override
    public void start(Stage stage) {
        houseGrid = new GridPane();
        houseGrid.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);

        setUpRooms();
        Scene scene = new Scene(houseGrid);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Map Editor");
        // Display the Stage
        stage.show();
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

    public void setUpRooms(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                GridPane g = setUpEmptyRoom();
                g.setPrefSize(WINDOW_WIDTH/3,WINDOW_HEIGHT/3);
                g.setStyle("-fx-padding: 0;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: black;");
                houseGrid.add(g,i,j);
            }
        }
    }

    public GridPane setUpEmptyRoom(){
        GridPane room = new GridPane();
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                HBox box = new HBox();
                box.setPrefSize(WINDOW_WIDTH/3/10,WINDOW_HEIGHT/3/10);
                box.setStyle("-fx-padding: 10;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-color: black;");
                room.add(box, i, j);
            }
        }

        return room;
    }
}
