package mapeditor;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * TilePane represents each tile on the board.
 *
 * @author oliviamorrison
 */
class TilePane extends Pane {
  private MapItem mapItem;
  private ImageView imageView;
  private boolean accessible;
  private GridPane room;
  private boolean door;
  private String direction;
  private boolean challenge;
  private MapEditor mapEditor;

  /**
   * Constructor to create a TilePane.
   *
   * @param a   boolean if the tile is accessible.
   * @param r   GridPane of the room which the tile is in.
   * @param d   boolean if the tile is a door.
   * @param dir String if the tile is a door what direction is it in.
   * @param c   boolean if the tile has a challenge on in.
   * @param m   MapEditor which the tile belongs to.
   */
  public TilePane(boolean a, GridPane r, boolean d, String dir, boolean c, MapEditor m) {
    imageView = new ImageView();
    accessible = a;
    room = r;
    door = d;
    direction = dir;
    challenge = c;
    mapEditor = m;

    setOnMouseClicked(e -> {
      //if the tile can be selected
      if (accessible && !challenge) {
        if (mapEditor.selectedTilePane != null) {
          mapEditor.selectedTilePane.setStyle("-fx-padding: 0;"
              + "-fx-border-style: solid inside;"
              + "-fx-border-width: 0.5;"
              + "-fx-border-color: black;");
        }

        mapEditor.selectedTilePane = this;
        this.setStyle("-fx-padding: 0;"
            + "-fx-border-style: solid inside;"
            + "-fx-border-width: 2;"
            + "-fx-border-color: red;");
      }
    });

  }

  /**
   * This method returns the direction of the door.
   *
   * @return String direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * This method returns if the tile is a door or not.
   *
   * @return boolean
   */
  public boolean isDoor() {
    return door;
  }

  /**
   * This method returns the room which the tile is in.
   *
   * @return GridPane
   */
  public GridPane getRoom() {
    return room;
  }

  /**
   * This method returns if the tile is accessible or not.
   *
   * @return boolean
   */
  public boolean isAccessible() {
    return accessible;
  }

  /**
   * This method sets if the tile is accessible.
   *
   * @param b boolean
   */
  public void setAccessible(boolean b) {
    accessible = b;
  }

  /**
   * This method resets the image view of the tile to an empty image.
   */
  public void resetImageView() {
    this.getChildren().remove(imageView);
    imageView = new ImageView();
  }


  /**
   * This method returns the image view of the tile.
   *
   * @return ImageView
   */
  public ImageView getImageView() {
    return imageView;
  }

  /**
   * This method returns the map item (if any) in the tile.
   *
   * @return MapItem
   */
  public MapItem getMapItem() {
    return mapItem;
  }

  /**
   * This method returns if the tile has a map item in it.
   *
   * @return boolean
   */
  public boolean hasMapItem() {
    return mapItem != null;
  }

  /**
   * This method sets the map item field to show the given map item.
   *
   * @param mapItem map item to be set
   */
  public void setMapItem(MapItem mapItem) {
    this.mapItem = mapItem;

    //if there isn't already something in this map item.
    if (mapItem != null) {
      imageView = new ImageView(mapItem.getImage());
      this.getChildren().add(imageView);
    }
  }
}
