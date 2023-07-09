package firered.entity;

import firered.Settings;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.map.MapManager;
import firered.map.Tile;
import firered.util.Vector;

public class Character extends Entity {
    public static final double TILE_MOVE_TIME = 0.35;

    protected double dx, dy;
    protected double moveTime = 0;
    protected boolean currentlyMoving = false;
    protected boolean hasMoved = false;
    protected Vector destinationTile = null;
    
    public Character(double tx, double ty, Sprite sprite) {
        super(tx, ty, sprite);

        screenPos = Vector.sub(screenPos, 0, sprite.height - Settings.TILE_SIZE - 1);
    }

    public void move(int dx, int dy) {
        Vector dest = Vector.add(tilePos, dx, dy);
        if (MapManager.collisionAt(dest)) return;
        if (!currentlyMoving) {
            currentlyMoving = true;
            this.dx = dx * Settings.TILE_SIZE / TILE_MOVE_TIME;
            this.dy = dy * Settings.TILE_SIZE / TILE_MOVE_TIME;
            this.destinationTile = dest;
            this.hasMoved = true;
        }
    }

    public void processMovement(double delta) {
        if (currentlyMoving) {
            moveTime += delta;
            worldPos = Vector.add(worldPos, dx * delta, dy * delta);
            screenPos = Vector.sub(worldPos, 0, sprite.height - Settings.TILE_SIZE - 1);
        }
        if (moveTime > TILE_MOVE_TIME) {
            moveTime = 0;
            currentlyMoving = false;
            this.dx = 0;
            this.dy = 0;
            this.tilePos.set(destinationTile);
            this.destinationTile = null;
            this.worldPos = Vector.mul(tilePos, Settings.TILE_SIZE);
            this.screenPos = Vector.sub(Vector.mul(tilePos, Settings.TILE_SIZE), 0, sprite.height - Settings.TILE_SIZE - 1);
        }
    }

    public void tick(double delta) {
        processMovement(delta);
        Tile underneath = MapManager.tileUnder(this);
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
    
    public void render(Screen screen) {
        screen.prepareRender(screenPos.getX() + MapManager.offsetX, screenPos.getY() + MapManager.offsetY, sprite, Screen.NPCS);
    }
}
