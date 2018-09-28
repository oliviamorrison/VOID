package application;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;
	private GridPane game;
	private FlowPane inventory;
	private AnchorPane options;
	private GridPane map;


	@Override public void start(Stage stage) {
		
		// create the menu bar
		MenuBar menuBar = new MenuBar();
		HBox hBox = new HBox(menuBar);
		menuBar.setPrefWidth(WINDOW_WIDTH);
	
		// menu bar items
		Menu newGame = new Menu("New Game");
		menuBar.getMenus().add(newGame);
		
		Menu quitGame = new Menu("Quit");
		menuBar.getMenus().add(quitGame);

		// initialise the game panes
		this.game = setGame();
		this.inventory = setInventory();
		this.options = setOptions();
		this.map = setMap();

		FlowPane stack = new FlowPane();
		stack.getChildren().addAll(inventory, options, map);

		stack.setHgap(4);
		stack.setPrefWrapLength(WINDOW_WIDTH*0.3); // preferred width allows for two columns

		HBox hb = new HBox();
		hb.getChildren().add(stack);
		
		GridPane grid = new GridPane();

		grid.add(game, 0, 1);
		grid.add(hb, 1, 1);
		grid.add(hBox, 0, 0);
		
		setWindowRatio();

		// Set the size of the window
		grid.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);		
		
		// Create the Scene
		Scene scene = new Scene(grid);
		
		// Add the scene to the Stage
		stage.setScene(scene);
		// Set the title of the Stage
		stage.setTitle("An Adventure Game");
		// Display the Stage
		stage.show();		
	}

	public void setWindowRatio(){
        //set ratios
        this.game.setPrefSize(WINDOW_WIDTH*0.7, WINDOW_HEIGHT);
        this.inventory.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.5);
        this.options.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.1);
        this.map.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.4);
    }

	public GridPane setGame() {
		GridPane grid = new GridPane();
		Text name = new Text("game");
		grid.add(name, 0, 0);
		grid.setStyle("-fx-background-color: red;");
	
		return grid;
	}

	public FlowPane setInventory() {
		FlowPane flow = new FlowPane();
		flow.setStyle("-fx-background-color: blue;");
		
		flow.setPadding(new Insets(8,1,1,8));
		flow.setVgap(4);
		flow.setHgap(4);
		flow.setPrefWrapLength(WINDOW_WIDTH*0.15); // preferred width allows for two columns

        ToggleButton btn;
        ToggleGroup group = new ToggleGroup();

        String[] images = new String[]{"key.png","green-key.png", "red-key.png","unlit-bomb.png","two-coins.png","two-coins.png"};

        for (int i = 0; i < 6; i++) {

            btn = new ToggleButton();
            Image image = new Image(getClass().getResourceAsStream(images[i]));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            btn.setGraphic(imageView);
            btn.setToggleGroup(group);
            btn.setPrefSize(WINDOW_WIDTH*0.14, WINDOW_HEIGHT*0.5*0.23);
            flow.getChildren().add(btn);

        }

		return flow;

	}

	public AnchorPane setOptions() {
		AnchorPane options = new AnchorPane();
		Button pickupButton = new Button("Pick Up");
		Button dropButton = new Button("Drop");
        Button diffuseButton = new Button("Diffuse");
        Button unlockButton = new Button("Unlock");

		HBox hb = new HBox();
		hb.setPadding(new Insets( 20,0,20,20));
		hb.setSpacing(10);
		hb.getChildren().addAll(pickupButton, dropButton, diffuseButton, unlockButton);
		options.setStyle("-fx-background-color: green;");

		options.getChildren().add(hb);
		return options;
	}

	public GridPane setMap() {
		GridPane grid = new GridPane();
		Text name = new Text("map");
		grid.add(name, 0, 0);
		grid.setStyle("-fx-background-color: orange;");
		grid.setPrefWidth(170);
		return grid;
	}


	public static void main(String[] args) { 
		Application.launch(args); 
	}

}