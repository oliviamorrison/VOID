package mapeditor;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

class TilePane extends Pane {
    private MapItem mapItem;
    private ImageView imageView;
    private boolean accessible;
    private GridPane room;
    private boolean door;
    private String direction;
    private boolean challenge;
    private MapEditor mapEditor;

    public TilePane(boolean a, GridPane r, boolean d, String dir, boolean c, MapEditor m) {
        imageView = new ImageView();
        accessible = a;
        room = r;
        door = d;
        direction = dir;
        challenge = c;
        mapEditor = m;

        setOnMouseClicked(e -> {
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

    public String getDirection() {
        return direction;
    }

    public boolean isDoor() {
        return door;
    }

    public GridPane getRoom() {
        return room;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean b) {
        accessible = b;
    }

    public void resetImageView() {
        this.getChildren().remove(imageView);
        imageView = new ImageView();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public MapItem getMapItem() {
        return mapItem;
    }

    public boolean hasMapItem() {
        return mapItem != null;
    }

    public void setMapItem(MapItem mapItem) {
        this.mapItem = mapItem;

        if (mapItem != null) {
            imageView = new ImageView(mapItem.getImage());
            this.getChildren().add(imageView);
        }
    }
}
