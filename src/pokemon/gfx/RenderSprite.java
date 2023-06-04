package pokemon.gfx;

import pokemon.gfx.sprites.Sprite;
import pokemon.util.Vector;

public class RenderSprite {
    private double x, y;
    private Sprite sprite;

    public RenderSprite(double x, double y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Sprite getSprite() {
        return sprite;
    }
}