package application;

import gameworld.Game;
import javafx.animation.Animation;
import javafx.animation.Transition;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import persistence.XmlParser;
import renderer.Renderer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

//TODO win/lose dialog

public class GUI extends Application implements EventHandler<KeyEvent>{
  public static final int WINDOW_WIDTH = 1000;
  public static final int WINDOW_HEIGHT = 750;

  // GUI components
  private GridPane game;
  private FlowPane inventory;
  private GridPane healthBar;
  private GridPane options;
  private GridPane screen;
  private Text screenMessage;
  private HashMap<String, ToggleButton> inventoryButtons = new HashMap<>();
  private Stage window;
  private Scene startScene, gameScene, levelsScene;
  private ProgressBar pBar;
  private Boolean pause = false;

  // Game components
  private Renderer renderer;
  private static Game currentGame;

  @Override
  public void start(Stage stage) {
    window = stage;

    window.setOnCloseRequest(e -> {
      e.consume();
      confirmExit();
    });

    // display the start menu first
    window.setScene(createStartScene(stage));
    window.setResizable(false);
    window.setTitle("Void");
    window.show();
  }

  @Override
  public void handle(KeyEvent event) {
    int dx = 0;
    int dy = 0;
    String str = "";

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
        str = currentGame.pickUpItem();
        break;
      case X:
        str = currentGame.dropItem();
        break;
      case N:
        str = currentGame.diffuseBomb();
        break;
      case C:
        str = currentGame.unlockVendingMachine();
        break;
      case V:
        str = currentGame.useVendingMachine();
        break;
      case SPACE:
        currentGame.teleport();
        renderer.newRoom();
        break;
      case B:
        str = currentGame.bribeGuard();
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
    updateScreen(str);

  }

  /**
   * Constructs the initial start menu screen
   * @param stage the primary stage constructed by the platform
   * @return the resulting start scene
   */
  private Scene createStartScene(Stage stage) {
    // title
    Image titleImage = null;
    try {
      titleImage = new Image(new FileInputStream("images/title.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    ImageView titleIcon = new ImageView(titleImage);

    // new game
    Button newGame = new Button();
    newGame.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image newImage = null;
    try {
      newImage = new Image(new FileInputStream("images/new.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView newGameIcon = new ImageView(newImage);
    newGame.setGraphic(newGameIcon);
    newGame.setOnAction(Event -> window.setScene(createLevelsScreen(stage)));

    // load
    Button load = new Button();
    load.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image loadImage = null;
    try {
      loadImage = new Image(new FileInputStream("images/load.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView loadIcon = new ImageView(loadImage);
    load.setGraphic(loadIcon);
    // only load a new game if a file was successfully chosen
    load.setOnAction(Event -> {
      if(loadFile(stage)) {
        window.setScene(createGameScene(stage));
      }
    });

    // edit map
    // TODO link up map editor gui
    Button editMap = new Button();
    editMap.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image editImage = null;
    try {
      editImage = new Image(new FileInputStream("images/editmap.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView editIcon = new ImageView(editImage);
    editMap.setGraphic(editIcon);
    // editMap.setOnAction(e -> Application.launch(MapEditor.class);

    // quit
    Button quit = new Button();
    quit.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image quitImage = null;
    try {
      quitImage = new Image(new FileInputStream("images/quit.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView quitIcon = new ImageView(quitImage);
    quit.setGraphic(quitIcon);
    quit.setOnMouseClicked(mouseEvent -> {
      confirmExit();
    });

    // buttons laid out in vertical column
    VBox buttons = new VBox(10);
    buttons.getChildren().addAll(titleIcon, newGame, load, editMap, quit);
    buttons.setAlignment(Pos.CENTER);

    // create the Start Scene
    startScene = new Scene(buttons, WINDOW_WIDTH, WINDOW_HEIGHT);
    buttons.setBackground(new Background(new BackgroundFill(Color.rgb(38,38,38), CornerRadii.EMPTY, Insets.EMPTY)));
    startScene.setOnKeyPressed(this);
    return startScene;
  }

  public Scene createLevelsScreen (Stage stage) {
    // title
    Image titleImage = null;
    try {
      titleImage = new Image(new FileInputStream("images/selectTitle.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    ImageView titleIcon = new ImageView(titleImage);

    // easy
    Button easy = new Button();
    easy.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image newImage = null;
    try {
      newImage = new Image(new FileInputStream("images/easy.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView newGameIcon = new ImageView(newImage);
    easy.setGraphic(newGameIcon);
    easy.setOnAction(Event -> startNewEasyGame(stage));

    // medium
    Button med = new Button();
    med.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image medImage = null;
    try {
      medImage = new Image(new FileInputStream("images/med.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView medIcon = new ImageView(medImage);
    med.setGraphic(medIcon);
    med.setOnAction(Event -> startNewMedGame(stage));

    // hard
    Button hard = new Button();
    hard.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image hardImage = null;
    try {
      hardImage = new Image(new FileInputStream("images/hard.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView hardIcon = new ImageView(hardImage);
    hard.setGraphic(hardIcon);
    hard.setOnAction(Event -> startNewHardGame(stage));

    // buttons laid out in horizontal row
    HBox buttons = new HBox(10);
    buttons.getChildren().addAll(easy, med, hard);
    buttons.setAlignment(Pos.CENTER);

    VBox levels = new VBox(60);
    levels.getChildren().addAll(titleIcon, buttons);
    levels.setAlignment(Pos.CENTER);

    levelsScene = new Scene(levels, WINDOW_WIDTH, WINDOW_HEIGHT);
    levels.setBackground(new Background(new BackgroundFill(Color.rgb(38,38,38), CornerRadii.EMPTY, Insets.EMPTY)));

    return levelsScene;

  }

  /**
   * Constructs the game screen by building each game GUI component one at a
   * time. Saves all of the game components (panes) as fields so they can be easily
   * be referenced when updating the game state.
   *
   * @param stage the primary stage constructed by the platform
   * @return the resulting game scene
   */
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

    newGame.setOnAction(Event -> window.setScene(levelsScene));
    loadGame.setOnAction(Event -> {
      if(loadFile(stage)) {
        window.setScene(createGameScene(stage));
      }
    });

    saveGame.setOnAction(Event -> saveFile(stage));

    // help
    Label help = new Label("Help");
    help.setOnMouseClicked(mouseEvent -> {
      pause = true;
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
    this.healthBar = setOxygenBar();
    this.inventory = setInventory();
    this.options = setOptions();
    String startMsg = "> Navigate through this unit to the safety " +
            "of your ship. Hurry Commander, time is of the essence!";
    this.screen = setScreen(startMsg);

    updateInventory();

    FlowPane stack = new FlowPane();
    stack.getChildren().addAll(healthBar,inventory, options, screen);

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


  /**
   * Attempts to read in a valid XML file
   * @param stage the primary stage constructed by the platform
   * @return true if a file was successfully loaded or false otherwise
   */
  public boolean loadFile(Stage stage) {
    FileChooser chooser = new FileChooser();
    configureFileChooser(chooser);
    chooser.setTitle("Open Game XML File");
    File file = chooser.showOpenDialog(stage);

    if (file == null) return false; // file loading failed

    try {
      currentGame = XmlParser.parseGame(file);
    } catch (XmlParser.ParseError parseError) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("File Error");
      alert.setContentText("Please load a valid XML file");
      alert.showAndWait();
      System.out.println(parseError.getMessage());
    }
    setGame(stage);
    return true;
  }

  /**
   * Attempts to write a current game state to an XML file
   * @param stage the primary stage constructed by the platform
   */
  public void saveFile(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser);

    //Show save file dialog
    File file = fileChooser.showSaveDialog(stage);

    if (file != null && !file.getName().equals("easy.xml") && !file.getName().equals("medium.xml") && !file.getName().equals("hard.xml")) {
      try {
        XmlParser.saveFile(file, currentGame);
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


  /**
   * Constructs a new EASY game based on a default XML file
   * @param stage the primary stage constructed by the platform
   */
  private void startNewEasyGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/easy.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Constructs a new MEDIUM game based on a default XML file
   * @param stage the primary stage constructed by the platform
   */
  private void startNewMedGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/medium.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Constructs a new HARD game based on a default XML file
   * @param stage the primary stage constructed by the platform
   */
  private void startNewHardGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/hard.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Configures the size of each game GUI pane
   *
   */
  public void setWindowRatio() {
    this.game.setPrefSize(WINDOW_WIDTH * 0.7, WINDOW_HEIGHT);
    this.healthBar.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT *  0.1);
    this.inventory.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT * 0.3);
    this.options.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT * 0.26);
    this.screen.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT * 0.33);
  }

  /**
   * Constructs the Game pane. This is where the rendering of the
   * actual game occurs so construct a new Renderer here.
   * @param stage the primary stage constructed by the platform
   * @return the resulting game pane
   */
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

    BackgroundImage myBI = new BackgroundImage(image,BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    grid.setBackground(new Background(myBI));
    grid.add(renderer.getRoot(), 0, 1);
    renderer.getRoot().setTranslateX(30);
    renderer.getRoot().setTranslateY(230);

    /////////////////////////////////////////////////////////Here Annisha
    pBar = new ProgressBar(currentGame.getPlayer().getHealth()/100);
    Task task = oxygenCounter(100);
    pBar.progressProperty().unbind();
    pBar.progressProperty().bind(task.progressProperty());
    new Thread(task).start();
    /////////////////////////////////////////////////////////


    return grid;
  }

  /**
   * Constructs the HealthBar pane. This is where the health bar of the
   * player is displayed
   * @return the resulting pane holding the health bar
   */
  public GridPane setOxygenBar() {
    GridPane healthBar = new GridPane();

    healthBar.add(pBar,0,0);
    healthBar.setStyle("-fx-border-width:5px;-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    pBar.setPrefSize((WINDOW_WIDTH * 0.3) - 30, (WINDOW_HEIGHT * 0.1) - 30);
    pBar.setStyle("-fx-accent: #00a57d;");
    healthBar.setAlignment(Pos.CENTER);
    return healthBar;
  }

  /**
   * Updates the length of the health bar based on time. Each second represents
   * a drop in the health bar.
   * @param health the players health
   * @return a new task to keep track of time passing
   */
  private Task oxygenCounter(int health){
    return new Task() {
      @Override
      protected Object call() throws Exception {
        for(int i = currentGame.getPlayer().getHealth(); i > 0; i = currentGame.getPlayer().getHealth()){
          Thread.sleep(1000);
          updateProgress(currentGame.getPlayer().getHealth(), health);
          if (!pause) currentGame.getPlayer().loseHealth();
        }
       //END GAME
        return true;
      }
    };
  }



  /**
   * Constructs the Inventory pane. This is where the inventory of the
   * player is displayed. The player can only hold one item at a time,
   * so only one item is toggled on at any given moment. Users may click
   * on the buttons to use the item if it is in the player's possession
   *
   * @return the resulting pane holding the inventory
   */
  public FlowPane setInventory() {
    FlowPane inventory = new FlowPane();
    inventory.setStyle("-fx-border-width:5px;-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");


    inventory.setVgap(10);
    inventory.setHgap(10);
    inventory.setPrefWrapLength(WINDOW_WIDTH * 0.1); // preferred width allows for two columns

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
      btn.setPrefWidth((WINDOW_WIDTH * 0.15) - 20);
      inventory.getChildren().add(btn);
      inventoryButtons.put(item, btn);
    }

    // enable button listeners
    inventoryButtons.get("GoldenCoin").setOnAction(Event -> {
      String str = currentGame.useVendingMachine();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("RedBoltCutter").setOnAction(Event -> {
      String str = currentGame.unlockVendingMachine();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("MagicPotion").setOnAction(Event -> {
      String str = currentGame.bribeGuard();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("BombDiffuser").setOnAction(Event -> {
      String str = currentGame.diffuseBomb();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });

    inventory.setAlignment(Pos.CENTER);

    return inventory;
  }

  /**
   * Constructs the Options pane. This is where the two options of picking
   * up/dropping items is made available to the user through the use of
   * buttons
   *
   * @return the resulting pane holding the pickup/drop buttons
   */
  public GridPane setOptions() {
    GridPane options = new GridPane();
    options.setStyle("-fx-border-width:5px;-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    Button pickupButton = new Button();
    Button dropButton = new Button();

    // disables key control
    pickupButton.setFocusTraversable(false);
    dropButton.setFocusTraversable(false);


    // enable button listeners and add images for buttons
    pickupButton.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image pickupImage = null;
    try {
      pickupImage = new Image(new FileInputStream("images/pickup.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView pickupIcon = new ImageView(pickupImage);
    pickupButton.setGraphic(pickupIcon);
    pickupButton.setOnAction(Event -> {
      String str = currentGame.pickUpItem();
      updateScreen(str);
      updateInventory();
      renderer.draw();
    });

    dropButton.setStyle("-fx-background-color: rgba(0,0,0,0);");
    Image dropImage = null;
    try {
      dropImage = new Image(new FileInputStream("images/drop.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView dropIcon = new ImageView(dropImage);
    dropButton.setGraphic(dropIcon);
    dropButton.setOnAction(Event -> {
      String str = currentGame.dropItem();
      updateScreen(str);
      updateInventory();
      renderer.draw();
    });

    // buttons laid out in vertical column
    VBox buttons = new VBox(10);
    buttons.getChildren().addAll(pickupButton, dropButton);
    options.getChildren().add(buttons);
    buttons.setTranslateX(15);
    buttons.setTranslateY(10);
    return options;
  }

  /**
   * Constructs the Screen pane. This is where textual feedback is given
   * to the user following a given action.
   *
   * @return the resulting pane holding the screen
   */
  private GridPane setScreen(String str) {
    GridPane screen = new GridPane();
    screen.setStyle("-fx-border-width:5px;-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    Text text = new Text();
    this.screenMessage = text;

    // format the displayed text
    text.setFont(Font.font ("Andale Mono", 20));
    text.setFill(Color.WHITE);
    VBox root = new VBox(text);
    root.setTranslateX(20);
    root.setTranslateY(20);
    root.setAlignment(Pos.CENTER);
    screen.getChildren().add(root);

    updateScreen(str);
    return screen;
  }


  /**
   * Displays a popup control menu to assist the user with keyboard control
   */
  public void displayHelp() {
    // blur the GUI
    game.setEffect(new GaussianBlur());
    healthBar.setEffect(new GaussianBlur());
    inventory.setEffect(new GaussianBlur());
    options.setEffect(new GaussianBlur());
    screen.setEffect(new GaussianBlur());

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
      healthBar.setEffect(null);
      inventory.setEffect(null);
      options.setEffect(null);
      screen.setEffect(null);
      helpDialog.hide();
      pause = false;
    });

    helpDialog.show();

  }

  /**
   * Displays a dialog box to confirm exit from the program
   *
   */
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

  /**
   * Updates the Inventory pane given the state of the player's
   * inventory.
   */
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

  /**
   * Updates the Screen pane given the action taken by the player
   *
   * @param msg the message to be displayed on the Screen pane
   */
  public void updateScreen(String msg) {

    // line break at every 20th character
    String parsedStr = msg.replaceAll("(.{20})", "$1-\n");

    final Animation animation = new Transition() {
      {
        setCycleDuration(Duration.millis(600));
      }

      protected void interpolate(double frac) {
        final int length = parsedStr.length();
        final int n = Math.round(length * (float) frac);
        screenMessage.setText(parsedStr.substring(0, n));
      }
    };
    animation.play();
  }



  public static void main(String[] args) {
    Application.launch(args);
  }

}