package application;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import gameworld.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import persistence.XMLParser;
import renderer.Renderer;

public class GUI extends Application implements EventHandler<KeyEvent> {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;
	private GridPane game;
	private FlowPane inventory;
	private AnchorPane options;
	private GridPane map;
	private Renderer renderer;
	private static Game currentGame;

	private Stage window;
	private Scene startScene, gameScene;


	@Override public void start(Stage stage) {
	    window = stage;

        // display the start menu first
        window.setScene(createStartScene(stage));
        window.setTitle("An Adventure Game");
        window.show();
	}

    private Scene createStartScene(Stage stage) {

	    // title
        Image image = null;
        try {
            image = new Image(new FileInputStream("src/application/title.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);



        // new game
        Button newGame = new Button("New Game");
        //TODO create new instance of a game from a default xml file
        //TODO storyline????
        newGame.setOnAction(Event -> startNewGame(stage));

        // load
        Button load = new Button("Load Game");
        load.setOnAction(e -> window.setScene(createGameScene(stage)));

        // edit map
        Button editMap = new Button("Edit Map");
        //TODO link up map editor gui
        editMap.setOnAction(e -> window.setScene(createGameScene(stage)));

        // quit
        Button quit = new Button("Quit");
        quit.setOnMouseClicked(mouseEvent->{ confirmExit(); });


        // buttons laid out in vertical column
        VBox buttons = new VBox(20);
        buttons.getChildren().addAll(imageView, newGame, load, editMap, quit);
        buttons.setAlignment(Pos.CENTER);


        // create the Game Scene
        startScene = new Scene(buttons, WINDOW_WIDTH, WINDOW_HEIGHT);
        startScene.setOnKeyPressed(this);
        return startScene;

    }


    public Scene createGameScene(Stage stage) {
	    // create the menu bar
        MenuBar menuBar = new MenuBar();
        HBox hBox = new HBox(menuBar);
        menuBar.setPrefWidth(WINDOW_WIDTH);

        // menu bar
        Menu file = new Menu("File");
        menuBar.getMenus().add(file);

        // file
        MenuItem newGame = new MenuItem("New Game");
        MenuItem editMap = new MenuItem("Edit Map");
        MenuItem loadGame = new MenuItem("Load Game");
        MenuItem saveGame = new MenuItem("Save Game");
        file.getItems().addAll(newGame, editMap, loadGame, saveGame);

        newGame.setOnAction(Event -> startNewGame(stage));
        loadGame.setOnAction(Event -> loadFile(stage));
        saveGame.setOnAction(Event -> saveFile(stage));

        // quit
        Label quit = new Label("Quit");
        quit.setOnMouseClicked(mouseEvent->{ confirmExit(); });
        Menu quitGame = new Menu("", quit);
        menuBar.getMenus().add(quitGame);

        // help
        Label help = new Label("Help");
        help.setOnMouseClicked(mouseEvent->{ confirmExit(); });
        Menu helpMenu = new Menu("", help);
        menuBar.getMenus().add(helpMenu);

        // initialise the game panes
        this.game = setGame(stage);
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

        // Create the Game Scene
        gameScene = new Scene(grid);
        gameScene.setOnKeyPressed(this);
        return gameScene;
    }

	private void startNewGame(Stage stage) {
    try {
      currentGame = XMLParser.parseGame(new File("data/gameworld.xml"));
      window.setScene(createGameScene(stage));
    } catch (XMLParser.ParseError parseError) {
      parseError.printStackTrace();
    }
   // setGame(stage);
	}


	public void loadFile(Stage stage) {
		FileChooser chooser = new FileChooser();
		configureFileChooser(chooser);
		chooser.setTitle("Open Game XML File");
		File file = chooser.showOpenDialog(stage);

		if(file != null) {
      try {
        currentGame = XMLParser.parseGame(file);
      } catch (XMLParser.ParseError parseError) {
        //TODO: Get user to load  a new file

      }
      setGame(stage);
		}
	}

	public void saveFile(Stage stage){
		FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);

		//Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			XMLParser.saveFile(file, currentGame);
		}
	}

	/**
	 * A class to configure the loading and saving of files to open in the current directory,
	 * and only load/save files in XML format
	 * @param fileChooser
	 */
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Open XML file");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
	}


	public void setWindowRatio(){
		//set ratios
		this.game.setPrefSize(WINDOW_WIDTH*0.7, WINDOW_HEIGHT);
		this.inventory.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.5);
		this.options.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.1);
		this.map.setPrefSize(WINDOW_WIDTH*0.3,WINDOW_HEIGHT*0.4);
	}

	public GridPane setGame(Stage stage) {
		if(currentGame == null) {
			System.out.println("Load a game or start a new game first!");
			//TODO: For now until we can get a start menu
			loadFile(stage);
		}
		renderer = new Renderer(currentGame);
		GridPane grid = new GridPane();
		grid.add(renderer.getRoot(), 0, 1);
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
			btn.setFocusTraversable(false); // disables key control
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

        // disables key control
        pickupButton.setFocusTraversable(false);
        dropButton.setFocusTraversable(false);
        diffuseButton.setFocusTraversable(false);
        unlockButton.setFocusTraversable(false);

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

	public void confirmExit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quit Game");
		alert.setHeaderText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			Platform.exit(); // exit application
		} else  {
			//do nothing..
		}
	}

	public void displayHelp() {

    }

	@Override
	public void handle(KeyEvent event) {
		int dx = 0;
		int dy = 0;
		//TODO: Should we give the keyboard inputs to game or handle that in the GUI class?
//		currentGame.startTurn(event.getCode().getName());
		//testing
		switch (event.getCode()) {
			case UP:
				dx = -1;
				break;
			case LEFT:
				dy = -1;
				break;
			case DOWN:
				dx = 1;
				break;
			case RIGHT:
				dy = 1;
				break;
			case SPACE:
				renderer.rotate();
				break;
			case P:
				currentGame.pickUpItem();
				break;
			case D:
				currentGame.dropItem();
				break;
			case F:
				currentGame.diffuseBomb();
				break;
			case U:
				currentGame.unlockVendingMachine();
				break;
			case V:
				currentGame.useVendingMachine();
				break;
			case R:
				currentGame.moveRoom();
				break;
			case B:
				currentGame.bribeGuard();
				break;
			default:

		}
		currentGame.getPlayer().moveTile(dx, dy);
		renderer.redraw();
	}





    public static void main(String[] args) {
		Application.launch(args);
	}

}