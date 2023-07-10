package firered.entity;

import firered.Settings;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.map.MapManager;
import firered.map.Tile;
import firered.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Character extends Entity {
	public static final double TILE_MOVE_TIME = 0.35;

	public boolean currentlyMoving = false;
	public Direction facing = Direction.DOWN;
	public Map<String, Sprite> sprites = new HashMap<>();

	protected double dx, dy;
	protected double moveTime = 0;
	protected boolean hasMoved = false;
	protected Vector destinationTile = null;

	public Character(double tx, double ty, Map<String, Sprite> sprites) {
		super(tx, ty, sprites.get("down"));
		this.sprites.putAll(sprites);

		screenPos = Vector.sub(screenPos, 0, sprite.height - Settings.TILE_SIZE - 1);
	}

	public void move(int dx, int dy) {
		Vector dest = Vector.add(tilePos, dx, dy);
		if (MapManager.collisionAt(dest)) return;
		if (!currentlyMoving) {
			currentlyMoving = true;
			if (dy == -1) {
				facing = Direction.UP;
				sprite = sprites.get("up");
			} else if (dy == 1) {
				facing = Direction.DOWN;
				sprite = sprites.get("down");
			} else if (dx == -1) {
				facing = Direction.LEFT;
				sprite = sprites.get("left");
			} else if (dx == 1) {
				facing = Direction.RIGHT;
				sprite = sprites.get("right");
			}
			this.dx = dx * Settings.TILE_SIZE / TILE_MOVE_TIME;
			this.dy = dy * Settings.TILE_SIZE / TILE_MOVE_TIME;
			this.destinationTile = dest;
			this.tilePos.set(destinationTile);
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
