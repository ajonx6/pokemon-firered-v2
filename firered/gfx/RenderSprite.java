package firered.gfx;

import firered.gfx.sprites.Sprite;

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