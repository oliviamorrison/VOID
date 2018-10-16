package mapeditor;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * ItemSpace represents each tile on the board.
 * @author oliviamorrison
 *
 */
class ItemSpace extends Pane {
    private MapItem mapItem;
    private ImageView imageView;
    private MapEditor mapEditor;

    /**
     * Constructor which creates a new ItemSpace with a given map editor.
     * 
     * @param m
     */
    public ItemSpace(MapEditor m) {
        imageView = new ImageView();
        mapEditor = m;
        setOnMouseClicked(e -> {
            if (mapEditor.selectedItem != null) {
                mapEditor.selectedItem.setStyle("-fx-padding: 0;"
                        + "-fx-border-style: solid inside;"
                        + "-fx-border-width: 0.5;"
                        + "-fx-border-color: black;");
            }

            mapEditor.selectedItem = this;
            this.setStyle("-fx-padding: 0;"
                    + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 2;"
                    + "-fx-border-color: red;");
        });
    }

    /**
     * This method returns the map item associated with this item space.
     * 
     * @return MapItem
     */
    public MapItem getMapItem() {
        return mapItem;
    }

    /**
     * This method resets the image view of the itemSpace to empty.
     */
    public void resetImageView() {
        this.getChildren().remove(imageView);
        imageView = new ImageView();
    }

    /**
     * This method returns the image view associated with this image space.
     * 
     * @return ImageView
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * This method sets the map item of this item space.
     * @param mapItem
     */
    public void setMapItem(MapItem mapItem) {
        this.mapItem = mapItem;

        if (mapItem != null) {
            imageView = new ImageView(mapItem.getImage());

            this.getChildren().add(imageView);
        }
    }

    /**
     * This method returns if this item space has an item in it.
     * 
     * @return boolean
     */
    public boolean hasItem() {
        return mapItem != null;
    }
}