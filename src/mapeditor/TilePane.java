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
   * A method to get the direction.
   * @return the direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * A method to get if it is a door.
   * @return the direction
   */
  public boolean isDoor() {
    return door;
  }

  /**
   * A method to get room.
   * @return the room
   */
  public GridPane getRoom() {
    return room;
  }

  /**
   * A method returns if the tile is accessible.
   * @return the accessible
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
   * A method to get the image view.
   * @return the imageView
   */
  public ImageView getImageView() {
    return imageView;
  }

  /**
   * A method to getMapItem from tile.
   * @return the mapItem
   */
  public MapItem getMapItem() {
    return mapItem;
  }

  /**
   * A method to return if the tile has a map tile.
   * @return the if the map has an item
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
