package application;

import gameworld.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import mapeditor.MapEditor;
import persistence.XmlParser;
import renderer.Renderer;


/**
 * This class represents the graphical user interface, which is responsible for
 * managing the functionality of the game including starting new games, loading
 * and saving games, event handling and the management of various application windows.
 *
 * @author Annisha Akosah 300399598
 */

public class Gui extends Application implements EventHandler<KeyEvent> {
  public static final int WINDOW_WIDTH = 1000;
  public static final int WINDOW_HEIGHT = 750;
  public static final String BUTTON_STYLE = "-fx-background-color: rgba(0,0,0,0);";
  public static final Color BACKGROUND_COLOUR = Color.rgb(38,38,38);

  // GUI components
  HashMap<String, ToggleButton> inventoryButtons = new HashMap<>();
  private GridPane game;
  private FlowPane inventory;
  private GridPane oxygenBar;
  private GridPane options;
  private GridPane screen;
  private Text screenMessage;
  private Stage window;
  private Scene startScene;
  private Scene gameScene;
  private Scene levelsScene;
  private Scene winLoseScene;
  private ProgressBar progressBar;
  private Boolean pause = false;
  private AudioClip audio;

  // Game components
  private Renderer renderer;
  private static Game currentGame;

  /**
   * Gets the current primary Stage.
   * @return the main application window
   */
  public Stage getWindow() {
    return window;
  }

  /**
   * Gets an ImageView based on a specified file name.
   * @param imageName the file name for the image
   * @return the resulting ImageView object
   */
  public ImageView getImage(String imageName) {
    Image image = null;
    try {
      image = new Image(new FileInputStream("images/" + imageName));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ImageView imageView = new ImageView(image);
    //imageView.setPreserveRatio(true);
    return imageView;
  }

  /**
   * Turns off the music
   */
  public void muteAudio() {
    audio.stop();
  }

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
        str = currentGame.befriendAlien();
        break;
      default:

    }
    if (!(dx == 0 && dy == 0)) {
      currentGame.movePlayer(dx, dy);

      // player wins when they find space ship
      if (currentGame.checkForSpaceship()) {
        window.setScene(createWinLoseScene("win"));
      }
      currentGame.checkForSpaceship();
      str = currentGame.checkForOxygenTank();
    }

    // player loses when they run out of oxygen
    if (currentGame.getPlayer().getOxygen() == 0) {
      window.setScene(createWinLoseScene("lose"));
    }
    renderer.draw();
    updateInventory();
    updateScreen(str);

  }

  /**
   * Constructs the initial start menu screen.
   * @param stage the primary stage constructed by the platform
   * @return the resulting start scene
   */
  public Scene createStartScene(Stage stage) {
    // new game
    Button newGame = new Button();
    newGame.setStyle(BUTTON_STYLE);
    ImageView newGameIcon = getImage("new.png");
    newGame.setGraphic(newGameIcon);
    newGame.setOnAction(event -> window.setScene(createLevelsScreen(stage)));

    // load
    Button load = new Button();
    load.setStyle(BUTTON_STYLE);
    ImageView loadIcon = getImage("load.png");
    load.setGraphic(loadIcon);
    // only load a new game if a file was successfully chosen
    load.setOnAction(event -> {
      if (loadFile(stage)) {
        window.setScene(createGameScene(stage));
      }
    });

    // edit map
    Button editMap = new Button();
    editMap.setStyle(BUTTON_STYLE);
    ImageView editIcon = getImage("editmap.png");
    editMap.setGraphic(editIcon);
    editMap.setOnAction(e -> {
      try {
        new MapEditor().start(stage);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    });

    // quit
    Button quit = new Button();
    quit.setStyle(BUTTON_STYLE);
    ImageView quitIcon = getImage("quit.png");
    quit.setGraphic(quitIcon);
    quit.setOnMouseClicked(mouseEvent -> {
      confirmExit();
    });

    // title
    ImageView titleIcon = getImage("title.png");

    // buttons laid out in vertical column
    VBox buttons = new VBox(10);
    buttons.getChildren().addAll(titleIcon, newGame, load, editMap, quit);
    buttons.setAlignment(Pos.CENTER);

    // create the Start Scene
    startScene = new Scene(buttons, WINDOW_WIDTH, WINDOW_HEIGHT);
    buttons.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOUR,
            CornerRadii.EMPTY, Insets.EMPTY)));
    startScene.setOnKeyPressed(this);
    return startScene;
  }


  /**
   * Constructs the Levels screen which gives users the ability to select
   * a given difficulty level when starting a new game.
   *
   * @param stage the primary stage constructed by the platform
   * @return the resulting levels scene
   */
  public Scene createLevelsScreen(Stage stage) {
    // easy
    Button easy = new Button();
    easy.setStyle(BUTTON_STYLE);
    ImageView newGameIcon = getImage("easy.png");
    easy.setGraphic(newGameIcon);
    easy.setOnAction(event -> startNewEasyGame(stage));

    // medium
    Button med = new Button();
    med.setStyle(BUTTON_STYLE);
    ImageView medIcon = getImage("med.png");
    med.setGraphic(medIcon);
    med.setOnAction(event -> startNewMedGame(stage));

    // hard
    Button hard = new Button();
    hard.setStyle(BUTTON_STYLE);
    ImageView hardIcon = getImage("hard.png");
    hard.setGraphic(hardIcon);
    hard.setOnAction(event -> startNewHardGame(stage));

    // buttons laid out in horizontal row
    HBox buttons = new HBox(10);
    buttons.getChildren().addAll(easy, med, hard);
    buttons.setAlignment(Pos.CENTER);

    // title
    ImageView titleIcon = getImage("selectTitle.png");

    VBox levels = new VBox(60);
    levels.getChildren().addAll(titleIcon, buttons);
    levels.setAlignment(Pos.CENTER);

    levelsScene = new Scene(levels, WINDOW_WIDTH, WINDOW_HEIGHT);
    levels.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOUR,
            CornerRadii.EMPTY, Insets.EMPTY)));

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

    newGame.setOnAction(event -> window.setScene(levelsScene));

    editMap.setOnAction(e -> {
      try {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setContentText("Proceed to edit map?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          new MapEditor().start(stage);
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    });

    loadGame.setOnAction(event -> {
      if (loadFile(stage)) {
        window.setScene(createGameScene(stage));
      }
    });

    saveGame.setOnAction(event -> saveFile(stage));

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
    quit.setOnMouseClicked(mouseEvent -> confirmExit());
    Menu quitGame = new Menu("", quit);
    menuBar.getMenus().add(quitGame);

    // disables key control
    menuBar.setFocusTraversable(false);

    // initialise the game panes
    this.game = setGame(stage);
    this.oxygenBar = setOxygenBar();
    this.inventory = setInventory();
    this.options = setOptions();
    String startMsg = "Navigate through this unit to the safety "
            + "of your ship. Hurry Commander, time is of the essence!";
    this.screen = setScreen(startMsg);

    updateInventory();

    FlowPane stack = new FlowPane();
    stack.getChildren().addAll(oxygenBar,inventory, options, screen);

    stack.setHgap(4);
    stack.setPrefWrapLength(WINDOW_WIDTH * 0.3); // preferred width allows for two columns

    HBox hb = new HBox();
    hb.getChildren().add(stack);

    GridPane grid = new GridPane();
    HBox box = new HBox(menuBar);

    grid.add(game, 0, 1);
    grid.add(hb, 1, 1);
    grid.add(box, 0, 0);

    setWindowRatio();

    // set the size of the window
    grid.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

    // create the Game Scene
    gameScene = new Scene(grid);
    gameScene.setOnKeyPressed(this);
    return gameScene;
  }

  /**
   * Constructs a screen displaying a win or lose message. Give the user
   * the option to play a new game (returning to the start screen), or quit.
   *
   * @param status a string denoting whether the user won or lost the game
   * @return the resulting win/lose scene
   */
  public Scene createWinLoseScene(String status) {
    // play again
    Button play = new Button();
    play.setStyle(BUTTON_STYLE);
    ImageView newGameIcon = getImage("play.png");
    play.setGraphic(newGameIcon);
    play.setOnAction(event -> window.setScene(startScene));

    // quit
    Button quit = new Button();
    quit.setStyle(BUTTON_STYLE);
    ImageView hardIcon = getImage("quit.png");
    quit.setGraphic(hardIcon);
    quit.setOnAction(event -> confirmExit());

    // buttons laid out in horizontal row
    HBox buttons = new HBox(10);
    buttons.getChildren().addAll(play, quit);
    buttons.setAlignment(Pos.CENTER);

    ImageView titleIcon = getImage(status + ".png");

    VBox options = new VBox(90);
    options.getChildren().addAll(titleIcon, buttons);
    options.setAlignment(Pos.CENTER);

    winLoseScene = new Scene(options, WINDOW_WIDTH, WINDOW_HEIGHT);
    options.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOUR,
            CornerRadii.EMPTY, Insets.EMPTY)));

    return winLoseScene;

  }


  /**
   * Attempts to read in a valid XML file.
   * @param stage the primary stage constructed by the platform
   * @return true if a file was successfully loaded or false otherwise
   */
  public boolean loadFile(Stage stage) {
    FileChooser chooser = new FileChooser();
    configureFileChooser(chooser);
    chooser.setTitle("Open Game XML File");
    File file = chooser.showOpenDialog(stage);

    if (file == null) {
      return false; // file loading failed
    }

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
   * Attempts to write a current game state to an XML file.
   * @param stage the primary stage constructed by the platform
   */
  public void saveFile(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser);

    //Show save file dialog
    File file = fileChooser.showSaveDialog(stage);

    if (file != null && !file.getName().equals("easy.xml") && !file.getName().equals("medium.xml")
            && !file.getName().equals("hard.xml")) {
      try {
        XmlParser.saveFile(file, currentGame);
      } catch (ParserConfigurationException | TransformerException e) {
        e.printStackTrace();
      }
    } else if (file != null) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Unable to save over default game files");
      alert.setContentText("Unable to save over default game files. "
          + "Please save using a different file name");
      alert.showAndWait();
    }
  }

  /**
   * A class to configure the loading and saving of files to open in the current directory,
   * and only load/save files in XML format.
   *
   * @param fileChooser file chooser
   */
  public static void configureFileChooser(final FileChooser fileChooser) {
    fileChooser.setTitle("Open XML file");
    fileChooser.setInitialDirectory(new File("data/."));

    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(extFilter);
  }


  /**
   * Constructs a new EASY game based on a default XML file.
   * @param stage the primary stage constructed by the platform
   */
  public void startNewEasyGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/easy.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Constructs a new MEDIUM game based on a default XML file.
   * @param stage the primary stage constructed by the platform
   */
  public void startNewMedGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/medium.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Constructs a new HARD game based on a default XML file.
   * @param stage the primary stage constructed by the platform
   */
  public void startNewHardGame(Stage stage) {
    try {
      currentGame = XmlParser.parseGame(new File("data/hard.xml"));
      window.setScene(createGameScene(stage));
    } catch (XmlParser.ParseError parseError) {
      parseError.printStackTrace();
    }
  }

  /**
   * Configures the size of each game GUI pane.
   *
   */
  public void setWindowRatio() {
    this.game.setPrefSize(WINDOW_WIDTH * 0.7, WINDOW_HEIGHT);
    this.oxygenBar.setPrefSize(WINDOW_WIDTH * 0.3, WINDOW_HEIGHT *  0.1);
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

    // add the background image to the games scene
    BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.REPEAT,
            BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    grid.setBackground(new Background(background));
    grid.add(renderer.getRoot(), 0, 1);
    renderer.getRoot().setTranslateX(30);
    renderer.getRoot().setTranslateY(230);

    // construct a new progress bar to show decreasing oxygen level
    progressBar = new ProgressBar(currentGame.getPlayer().getOxygen() / 100);
    Task task = oxygenCounter(100);
    progressBar.progressProperty().unbind();
    progressBar.progressProperty().bind(task.progressProperty());
    new Thread(task).start();

    // add music
    if (audio != null) {
      audio.stop();
    }
    String path = "music/space.wav";
    audio = new AudioClip(Paths.get(path).toUri().toString());
    audio.setCycleCount(10);
    audio.play();

    return grid;
  }

  /**
   * Constructs the OxygenBar pane. This is where the health bar of the
   * player is displayed
   * @return the resulting pane holding the health bar
   */
  public GridPane setOxygenBar() {
    GridPane oxygenBar = new GridPane();
    oxygenBar.add(progressBar,0,0);
    oxygenBar.setStyle("-fx-border-width:5px;"
        + "-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    progressBar.setPrefSize((WINDOW_WIDTH * 0.3) - 30, (WINDOW_HEIGHT * 0.1) - 30);
    progressBar.setStyle("-fx-accent: #00a57d;");
    oxygenBar.setAlignment(Pos.CENTER);
    return oxygenBar;
  }

  /**
   * Updates the length of the health bar based on time. Each second represents
   * a drop in the health bar.
   * @param oxygen the players health
   * @return a new task to keep track of time passing
   */
  private Task oxygenCounter(int oxygen) {
    return new Task() {
      @Override
      protected Object call() throws Exception {
        int oxygen = currentGame.getPlayer().getOxygen();
        for (int i = oxygen; i > 0; i = oxygen) {
          Thread.sleep(1000); // CHANGE FOR DIFFERENT TIMERS
          updateProgress(currentGame.getPlayer().getOxygen(), oxygen);
          if (!pause) {
            currentGame.getPlayer().loseOxygen();
          }
        }
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
    inventory.setStyle("-fx-border-width:5px;"
        + "-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");


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

    for (String item : availableItems) {
      btn = new ToggleButton();
      btn.setFocusTraversable(false); // disables key control

      // use images to represent buttons
      ImageView imageView = getImage(item + ".png");
      imageView.setFitHeight(80);
      imageView.setFitWidth(80);
      btn.setGraphic(imageView);
      btn.setToggleGroup(group);
      btn.setPrefWidth((WINDOW_WIDTH * 0.15) - 20);
      inventory.getChildren().add(btn);
      inventoryButtons.put(item, btn);
    }

    // enable button listeners
    inventoryButtons.get("GoldenCoin").setOnAction(event -> {
      String str = currentGame.useVendingMachine();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("RedBoltCutter").setOnAction(event -> {
      String str = currentGame.unlockVendingMachine();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("MagicPotion").setOnAction(event -> {
      String str = currentGame.befriendAlien();
      updateInventory();
      updateScreen(str);
      renderer.draw();
    });
    inventoryButtons.get("BombDiffuser").setOnAction(event -> {
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
   * buttons.
   *
   * @return the resulting pane holding the pickup/drop buttons
   */
  public GridPane setOptions() {
    GridPane options = new GridPane();
    options.setStyle("-fx-border-width:5px;"
        + "-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    Button pickupButton = new Button();
    Button dropButton = new Button();

    // disables key control
    pickupButton.setFocusTraversable(false);
    dropButton.setFocusTraversable(false);

    // enable button listeners and add images for buttons
    pickupButton.setStyle(BUTTON_STYLE);
    ImageView pickupIcon = getImage("pickup.png");
    pickupButton.setGraphic(pickupIcon);
    pickupButton.setOnAction(event -> {
      String str = currentGame.pickUpItem();
      updateScreen(str);
      updateInventory();
      renderer.draw();
    });

    dropButton.setStyle(BUTTON_STYLE);
    ImageView dropIcon = getImage("drop.png");
    dropButton.setGraphic(dropIcon);
    dropButton.setOnAction(event -> {
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
    screen.setStyle("-fx-border-width:5px;"
        + "-fx-border-color:rgb(38,38,38);-fx-background-color: rgb(45,45,45);");
    Text text = new Text();
    this.screenMessage = text;

    // format the displayed text
    text.setFont(Font.font("Andale Mono", 20));
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
   * Displays a popup control menu to assist the user with keyboard control.
   */
  public void displayHelp() {
    // blur the GUI
    game.setEffect(new GaussianBlur());
    oxygenBar.setEffect(new GaussianBlur());
    inventory.setEffect(new GaussianBlur());
    options.setEffect(new GaussianBlur());
    screen.setEffect(new GaussianBlur());

    VBox pauseRoot = new VBox(5);
    pauseRoot.setPrefSize(500,200);

    pauseRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
    pauseRoot.setAlignment(Pos.BOTTOM_CENTER);
    pauseRoot.setPadding(new Insets(20));

    ImageView imageView = getImage("controls.png");

    Button resume = new Button("Resume");
    pauseRoot.getChildren().addAll(imageView, resume);

    Stage helpDialog = new Stage(StageStyle.TRANSPARENT);
    helpDialog.initOwner(window);
    helpDialog.initModality(Modality.APPLICATION_MODAL);
    helpDialog.setScene(new Scene(pauseRoot, Color.TRANSPARENT));

    resume.setOnAction(event -> {
      game.setEffect(null);
      oxygenBar.setEffect(null);
      inventory.setEffect(null);
      options.setEffect(null);
      screen.setEffect(null);
      helpDialog.hide();
      pause = false;
    });

    helpDialog.show();

  }

  /**
   * Displays a dialog box to confirm exit from the program.
   *
   */
  public void confirmExit() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Quit Game");
    alert.setHeaderText("Are you sure?");

    ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
    ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

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
      if (!currentGame.getPlayer().hasSpecificItem(item)) {
        button.setDisable(true);
      } else {
        button.setDisable(false);
      }
    }
  }

  /**
   * Updates the Screen pane to display textual feedback based on
   * an action taken by the player (if there was one).
   *
   * @param msg the message to be displayed on the Screen pane
   */
  public void updateScreen(String msg) {
    if (msg.equals("")) {
      return;
    }

    // line break at every 20th character
    String str = "> " + msg;
    String parsedStr = str.replaceAll("(.{20})", "$1-\n");

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