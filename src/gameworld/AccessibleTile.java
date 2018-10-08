package gameworld;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class AccessibleTile extends Tile {
    private Item item;
    private Bomb bomb;
    private boolean player = false;

    public AccessibleTile(Room room, int x, int y) {
        super(room, x, y);
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean hasToken() {
        return this.item != null;
    }

    public void setBomb(Bomb b) {
        this.bomb = b;
    }

    public Bomb getBomb() {
        return this.bomb;
    }

    public boolean hasBomb() {
        return this.bomb != null;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public boolean hasPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "X";
    }

    public Point2D getCenter() {
        Polygon p = super.getTilePolygon();
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for(int i = 0; i < p.getPoints().size() - 1; i+=2 ) {
            minX = Math.min(minX, p.getPoints().get(i));
            maxX = Math.max(maxX, p.getPoints().get(i));
            minY = Math.min(minY, p.getPoints().get(i+1));
            maxY = Math.max(maxY, p.getPoints().get(i+1));
        }
        double centerX = minX + ((maxX - minX) / 2);
        double centerY = minY + ((maxY - minY)/2);

        return new Point2D(centerX, centerY);
    }
}
