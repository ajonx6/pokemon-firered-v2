package pokemon.entity;

import pokemon.Settings;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;
import pokemon.util.Vector;

import java.util.Set;

public class Entity {
    private Vector worldPos, tilePos, screenPos;
    private Sprite sprite;

    public Entity(double wx, double wy, Sprite sprite) {
        this.worldPos = new Vector(wx, wy);
        this.tilePos = Vector.div(worldPos, Settings.SCALED_TILE_SIZE);
        this.screenPos = Vector.mul(tilePos, Settings.SCALED_TILE_SIZE);
        this.sprite = sprite;
        
        screenPos = Vector.sub(screenPos, 0, sprite.height - Settings.SCALED_TILE_SIZE);
    }
    
    public void move(double dx, double dy) {
        screenPos = Vector.add(screenPos, dx, dy);
    }
    
    public void render(Screen screen) {
        screen.render(screenPos.getX(), screenPos.getY(), sprite);
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