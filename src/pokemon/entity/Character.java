package pokemon.entity;

import pokemon.Game;
import pokemon.Settings;
import pokemon.gfx.sprites.Sprite;
import pokemon.level.Tile;
import pokemon.util.Vector;

public class Character extends Entity {
    public static final double TILE_MOVE_TIME = 0.35;

    protected double dx, dy;
    protected double moveTime = 0;
    protected boolean currentlyMoving = false;
    protected boolean hasMoved = false;
    
    public Character(double tx, double ty, Sprite sprite) {
        super(tx, ty, sprite);
    }

    public void move(double dx, double dy) {
        if (!currentlyMoving) {
            currentlyMoving = true;
            this.dx = dx * Settings.SCALED_TILE_SIZE / TILE_MOVE_TIME;
            this.dy = dy * Settings.SCALED_TILE_SIZE / TILE_MOVE_TIME;
            this.tilePos = Vector.add(tilePos, dx, dy);
            this.hasMoved = true;
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
        Tile underneath = Game.getInstance().map.tileUnder(this);
        if (!currentlyMoving && underneath != null) {
            underneath.action(this);
        }
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
