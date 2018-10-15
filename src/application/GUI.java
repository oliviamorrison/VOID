package application;

import gameworld.Game;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import persistence.XMLParser;
import renderer.Renderer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class GUI extends Application implements EventHandler<KeyEvent>{
  public static final int WINDOW_WIDTH = 1000;
  public static final int WINDOW_HEIGHT = 750;
  private HashMap<String, ToggleButton> inventoryButtons = new HashMap<String, ToggleButton>();
  private GridPane game;
  private FlowPane inventory;
  private GridPane healthBar;
  private AnchorPane options;
  private Renderer renderer;
  private static Game currentGame;
  private Timer timer;

  private Stage window;
  private Scene startScene, gameScene;

  private Label health;
  private ProgressBar pBar;

  @Override
  public void start(Stage stage) {
    window = stage;

    // display the start menu first
    window.setScene(createStartScene(stage));
    window.setResizable(false);
    window.setTitle("Void");
    window.show();
  }

  private Scene createStartScene(Stage stage) {
    // title
    Image image = null;
    try {
      image = new Image(new FileInputStream("images/title.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    ImageView imageView = new ImageView(image);

    // new game
    Button newGame = new Button("New Game");
    //TODO storyline????
    newGame.setOnAction(Event -> startNewGame(stage));

    // load
    Button load = new Button("Load Game");
    load.setOnAction(e -> window.setScene(createGameScene(stage)));

    // edit map
    Button editMap = new Button("Edit Map");
    // TODO link up map editor gui
    // editMap.setOnAction(e -> window.setScene(createGameScene(stage)));

    // quit
    Button quit = new Button("Quit");
    quit.setOnMouseClicked(mouseEvent -> {
      confirmExit();
    });


    // buttons laid out in vertical column
    VBox buttons = new VBox(20);
    buttons.getChildren().addAll(imageView, newGame, load, editMap, quit);
    buttons.setAlignment(Pos.CENTER);


    // create the Game Scene
    startScene = new Scene(buttons, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);
    buttons.setBackground(new Background(new BackgroundFill(Color.rgb(38,38,38), CornerRadii.EMPTY, Insets.EMPTY)));
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
    loadGame.setOnAction(Event -> {
      loadFile(stage);
      window.setScene(createGameScene(stage));
    });
    saveGame.setOnAction(Event -> saveFile(stage));

    // help
    Label help = new Label("Help");
    help.setOnMouseClicked(mouseEvent -> {
      displayHelp();
    });
    Menu helpMenu = new Menu("", help);
    menuBar.getMenus().add(helpMenu);

    // quit
    Label quit = new Label("Quit");
    quit.setOnMouseClicked(mouseEvent -> {
      confirmExit();
    });
    Menu quitGame = new Menu("", quit);
    menuBar.getMenus().add(quitGame);


    // disables key control
    menuBar.setFocusTraversable(false);

    // initialise the game panes
    this.game = setGame(stage);
    this.healthBar = setHealthBar();
    this.inventory = setInventory();
    this.options = setOptions();

    updateInventory();

    FlowPane stack = new FlowPane();
    stack.getChildren().addAll(healthBar,inventory, options);

    stack.setHgap(4);
    stack.setPrefWrapLength(WINDOW_WIDTH * 0.3); // preferred width allows for two columns

    HBox hb = new HBox();
    hb.getChildren().add(stack);

    GridPane grid = new GridPane();

    grid.add(game, 0, 1);
    grid.add(hb, 1, 1);
    grid.add(hBox, 0, 0);

    setWindowRatio();

    // set the size of the window
    grid.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

    // create the Game Scene
    gameScene = new Scene(grid);
    gameScene.setOnKeyPressed(this);
    return gameScene;
  }


  public void loadFile(Stage stage) {
    FileChooser chooser = new FileChooser();
    configureFileChooser(chooser);
    chooser.setTitle("Open Game XML File");
    File file = chooser.showOpenDialog(stage);

    if (file != null) {
      try {
        currentGame = XMLParser.parseGame(file);
      } catch (XMLParser.ParseError parseError) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("File Error");
        alert.setContentText("Please load a valid XML file");
        alert.showAndWait();
      }
      setGame(stage);
    }
  }

  public void saveFile(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser);

    //Show save file dialog
    File file = fileChooser.showSaveDialog(stage);

    if (file != null && !file.getName().equals("easy.xml")) {
      try {
        XMLParser.saveFile(file, currentGame);
      } catch (ParserConfigurationException | TransformerException e) {
        e.printStackTrace();
      }
    }
    else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Unable to save over default game files");
      alert.setContentText("Unable to save over default game files. Please save using a different file name");
      alert.showAndWait();
    }
  }

  /**
   * A class to configure the loading and saving of files to open in the current directory,
   * and only load/save files in XML format
   *
   * @param fileChooser
   */
  private static void configureFileChooser(final FileChooser fileChooser) {
    fileChooser.setTitle("Open XML file");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(extFilter);
  }


  private void startNewGame(Stage stage) {
    try {
      currentGame = XMLParser.parseGame(new File("data/easy.xml"));
      window.setScene(createGameScene(stage));
    } catch (XMLParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }


  public void setWindowRatio() {
    //set ratios
    this.game.setPrefSize(WINDOW_WIDTH * 0.7, WINDOW_HEIGHT);
    this.healthBar.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT *  0.2);
    this.inventory.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT * 0.4);
    this.options.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT * 0.4);
  }

  public GridPane setGame(Stage stage) {
    if (currentGame == null) {
      loadFile(stage);
    }
    renderer = new Renderer(currentGame);
    GridPane grid = new GridPane();

    Image image = null;
    try {
      image = new Image(new FileInputStream("images/background.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    BackgroundImage myBI= new BackgroundImage(image,BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    grid.setBackground(new Background(myBI));
    grid.add(renderer.getRoot(), 0, 1);
    renderer.getRoot().setTranslateX(30);
    renderer.getRoot().setTranslateY(230);

    /////////////////////////////////////////////////////////Here Annisha
    pBar = new ProgressBar(currentGame.getPlayer().getHealth()/100);
    Task task = taskCreator(100);
    pBar.progressProperty().unbind();
    pBar.progressProperty().bind(task.progressProperty());
    new Thread(task).start();
    /////////////////////////////////////////////////////////
    return grid;
  }




  public GridPane setHealthBar() {
    GridPane grid = new GridPane();
    grid.setStyle("-fx-background-color: red;");
    grid.add(pBar,0,0);
    return grid;
  }

//  private Task taskCreator(int seconds){
//    return new Task() {
//      @Override
//      protected Object call() throws Exception {
//        for(int i=0; i<=seconds;i++){
//          Thread.sleep(1000);
//          updateProgress(seconds-i, seconds);
//          if(seconds-i == 0){
//            System.out.println("Finish");
//          }
//        }
//        return true;
//      }
//    };
//  }
private Task taskCreator(int health){
  return new Task() {
    @Override
    protected Object call() throws Exception {
      for(int i = currentGame.getPlayer().getHealth(); i > 0; i = currentGame.getPlayer().getHealth()){
        Thread.sleep(1000);
        updateProgress(currentGame.getPlayer().getHealth(), health);
        currentGame.getPlayer().loseHealth();
      }
      System.out.println("Finish");
      //TODO: game needs to end
      return true;
    }
  };
}




  public FlowPane setInventory() {
    FlowPane flow = new FlowPane();
    flow.setStyle("-fx-background-color: white;");

    flow.setPadding(new Insets(8, 1, 1, 8));
    flow.setVgap(4);
    flow.setHgap(4);
    flow.setPrefWrapLength(WINDOW_WIDTH * 0.15); // preferred width allows for two columns

    ToggleButton btn;
    ToggleGroup group = new ToggleGroup();

    ArrayList<String> availableItems = new ArrayList<>();
    availableItems.add("GoldenCoin");
    availableItems.add("MagicPotion");
    availableItems.add("BombDiffuser");
    availableItems.add("RedBoltCutter");

    for(String item : availableItems) {
      btn = new ToggleButton();
      btn.setFocusTraversable(false); // disables key control

      // use images to represent buttons
      Image image = null;
      try {
        image = new Image(new FileInputStream("images/" + item + ".png"));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      ImageView imageView = new ImageView(image);
      imageView.setFitHeight(80);
      imageView.setFitWidth(80);
      btn.setGraphic(imageView);
      btn.setToggleGroup(group);
      btn.setPrefSize(WINDOW_WIDTH * 0.14, WINDOW_HEIGHT * 0.5 * 0.23);
      flow.getChildren().add(btn);
      inventoryButtons.put(item, btn);
    }

    // enable button listeners
    inventoryButtons.get("GoldenCoin").setOnAction(Event -> {
      currentGame.useVendingMachine();
      updateInventory();
      renderer.draw();
    });
    inventoryButtons.get("RedBoltCutter").setOnAction(Event -> {
      currentGame.unlockVendingMachine();
      updateInventory();
      renderer.draw();
    });
    inventoryButtons.get("MagicPotion").setOnAction(Event -> {
      currentGame.bribeGuard();
      updateInventory();
      renderer.draw();
    });
    inventoryButtons.get("BombDiffuser").setOnAction(Event -> {
      currentGame.diffuseBomb();
      updateInventory();
      renderer.draw();
    });

    return flow;
  }


  public AnchorPane setOptions() {
    AnchorPane options = new AnchorPane();
    Button pickupButton = new Button("Pick Up");
    Button dropButton = new Button("Drop");


    // disables key control
    pickupButton.setFocusTraversable(false);
    dropButton.setFocusTraversable(false);


    // enable button listeners
    pickupButton.setOnAction(Event -> {
      currentGame.pickUpItem();
      updateInventory();
      renderer.draw();
    });
    dropButton.setOnAction(Event -> {
      currentGame.dropItem();
      updateInventory();
      renderer.draw();
    });


    HBox hb = new HBox();
    hb.setPadding(new Insets(20, 0, 20, 20));
    hb.setSpacing(10);
    hb.getChildren().addAll(pickupButton, dropButton);
    options.setStyle("-fx-background-color: green;");

    options.getChildren().add(hb);
    return options;
  }


  public void confirmExit() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Quit Game");
    alert.setHeaderText("Are you sure?");

    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == ButtonType.OK) {
      System.exit(0); // exit application
    } else {
      //do nothing..
    }
  }

  @Override
  public void handle(KeyEvent event) {
    int dx = 0;
    int dy = 0;

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
      case A:
        currentGame.rotateRoomAnticlockwise();
        break;
      case D:
        currentGame.rotateRoomClockwise();
        break;
      case Z:
        currentGame.pickUpItem();
        break;
      case X:
        currentGame.dropItem();
        break;
      case N:
        currentGame.diffuseBomb();
        break;
      case C:
        currentGame.unlockVendingMachine();
        break;
      case V:
        currentGame.useVendingMachine();
        break;
      case SPACE:
        currentGame.teleport();
        renderer.newRoom();
        break;
      case B:
        currentGame.bribeGuard();
        break;
      default:

    }
    if (!(dx == 0 && dy == 0)) {

      currentGame.movePlayer(dx, dy);

      if (currentGame.checkForAntidote()) {
        System.out.println("Winner winner");
        System.exit(0);
      }
      currentGame.checkForHealthPack();
    }

    renderer.draw();
    updateInventory();

  }


    public void displayHelp() {
        // blur the GUI
        game.setEffect(new GaussianBlur());
        inventory.setEffect(new GaussianBlur());
        options.setEffect(new GaussianBlur());

        VBox pauseRoot = new VBox(5);
        pauseRoot.setPrefSize(500,200);

        pauseRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseRoot.setAlignment(Pos.BOTTOM_CENTER);
        pauseRoot.setPadding(new Insets(20));

        Image image = null;
        try {
            image = new Image(new FileInputStream("images/controls.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);

        Button resume = new Button("Resume");
        pauseRoot.getChildren().addAll(imageView, resume);

        Stage helpDialog = new Stage(StageStyle.TRANSPARENT);
        helpDialog.initOwner(window);
        helpDialog.initModality(Modality.APPLICATION_MODAL);
        helpDialog.setScene(new Scene(pauseRoot, Color.TRANSPARENT));

        resume.setOnAction(event -> {
            game.setEffect(null);
            inventory.setEffect(null);
            options.setEffect(null);
            helpDialog.hide();
        });

        helpDialog.show();

  }

  public void updateInventory() {
    for (Map.Entry<String, ToggleButton> buttons : inventoryButtons.entrySet()) {
      String item = buttons.getKey();

      ToggleButton button = buttons.getValue();
      if(!currentGame.getPlayer().hasSpecificItem(item)) {
        button.setDisable(true);
      } else {
        button.setDisable(false);

      }
    }
  }


  public static void main(String[] args) {
    Application.launch(args);
  }

}