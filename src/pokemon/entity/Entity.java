package pokemon.entity;

import pokemon.Settings;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;
import pokemon.util.Vector;

public abstract class Entity {
    protected Vector worldPos, tilePos, screenPos;
    protected Sprite sprite;

    public Entity(double tx, double ty, Sprite sprite) {
        this.tilePos = new Vector(tx, ty);
        this.worldPos = new Vector(tx * Settings.TILE_SIZE, ty * Settings.TILE_SIZE);
        this.screenPos = Vector.mul(tilePos, Settings.TILE_SIZE);
        this.sprite = sprite;
    }

    public abstract void tick(double delta);

    public void render(Screen screen) {
        // screen.prepareRender(screenPos.getX() + Map.offsetX, screenPos.getY() + Map.offsetY, sprite, 2);
    }

    public Vector getTilePos() {
        return tilePos;
    }

    public void setTilePos(Vector tilePos) {
        this.tilePos = tilePos;
    }

    public Vector getWorldPos() {
        return worldPos;
    }

    public void setWorldPos(Vector worldPos) {
        this.worldPos = worldPos;
    }

    public Vector getScreenPos() {
        return screenPos;
    }

    public void setScreenPos(Vector screenPos) {
        this.screenPos = screenPos;
    }

    public Sprite getSprite() {
        return sprite;
    }
}