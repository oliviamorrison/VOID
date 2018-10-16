package mapeditor;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class ItemSpace extends Pane {
    private MapItem mapItem;
    private ImageView imageView;
    private MapEditor mapEditor;

    public ItemSpace(int x, int y, MapEditor m) {
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

    public MapItem getMapItem() {
        return mapItem;
    }

    public void resetImageView() {
        this.getChildren().remove(imageView);
        imageView = new ImageView();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setMapItem(MapItem mapItem) {
        this.mapItem = mapItem;

        if (mapItem != null) {
            imageView = new ImageView(mapItem.getImage());

            this.getChildren().add(imageView);
        }
    }

    public boolean hasItem() {
        return mapItem != null;
    }
}