package pokemon.entity;

import pokemon.Settings;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;
import pokemon.level.Map;
import pokemon.util.Vector;

import java.util.Set;

public class Entity {
    public static final double TILE_MOVE_TIME = 0.35;

    protected Vector worldPos, tilePos, screenPos;
    protected Sprite sprite;

    protected double dx, dy;
    protected double moveTime = 0;
    protected boolean currentlyMoving = false;

    public Entity(double tx, double ty, Sprite sprite) {
        this.tilePos = new Vector(tx, ty);
        this.worldPos = new Vector(tx * Settings.SCALED_TILE_SIZE, ty * Settings.SCALED_TILE_SIZE);
        this.screenPos = Vector.mul(tilePos, Settings.SCALED_TILE_SIZE);
        this.sprite = sprite;

        screenPos = Vector.sub(screenPos, 0, sprite.height - Settings.SCALED_TILE_SIZE);
    }

    public void move(double dx, double dy) {
        if (!currentlyMoving) {
            currentlyMoving = true;
            this.dx = dx * Settings.SCALED_TILE_SIZE / TILE_MOVE_TIME;
            this.dy = dy * Settings.SCALED_TILE_SIZE / TILE_MOVE_TIME;
            this.tilePos = Vector.add(tilePos, dx, dy);
        }
    }
    
    public void processMovement(double delta) {
        if (currentlyMoving) {
            moveTime += delta;
            worldPos = Vector.add(worldPos, dx * delta, dy * delta);
            screenPos = Vector.sub(worldPos, 0, sprite.height - Settings.SCALED_TILE_SIZE);
        }
        if (moveTime > TILE_MOVE_TIME) {
            moveTime = 0;
            currentlyMoving = false;
            this.dx = 0;
            this.dy = 0;
            this.worldPos = Vector.mul(tilePos, Settings.SCALED_TILE_SIZE);
            this.screenPos = Vector.sub(Vector.mul(tilePos, Settings.SCALED_TILE_SIZE), 0, sprite.height - Settings.SCALED_TILE_SIZE);
        }
    }

    public void tick(double delta) {
        processMovement(delta);
    }

    public void render(Screen screen) {
        screen.render(screenPos.getX() + Map.offsetX, screenPos.getY() + Map.offsetY, sprite);
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