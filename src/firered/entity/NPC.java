package firered.entity;

import firered.Settings;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.map.MapManager;
import firered.map.Tile;
import firered.scripts.Script;
import firered.util.Util;
import firered.util.Vector;

import java.util.*;

public class NPC extends Entity {
	public static final HashMap<String, NPC> CHARACTERS = new HashMap<>();
	public static final Player PLAYER = new Player("player", Util.charsToSprites("player", "down", "up", "left", "right"));
	public static final NPC NPC = new NPC("npc", Util.charsToSprites("npc", "down", "up", "left", "right"));

	public static final double TILE_MOVE_TIME = 0.35;

	public boolean currentlyMoving = false;
	public Direction facing = Direction.DOWN;
	public Map<String, Sprite> sprites = new HashMap<>();
	public int id;
	public LinkedList<Direction> movementQueue = new LinkedList<>();
	public Script scriptOnInteract;

	public Script scriptToContinueAfterMove = null;

	protected double dx, dy;
	protected double moveTime = 0;
	protected boolean hasMoved = false;
	protected Vector destinationTile = null;

	public NPC(String name, Map<String, Sprite> sprites) {
		super(-1, -1, sprites.get("down"));
		CHARACTERS.put(name, this);
		this.sprites.putAll(sprites);
	}

	public NPC(int id, double tx, double ty, Map<String, Sprite> sprites) {
		super(tx, ty, sprites.get("down"));
		this.id = id;
		this.sprites.putAll(sprites);

		screenPos = Vector.sub(screenPos, 0, sprite.height - Settings.TILE_SIZE - 1);
	}

	public NPC instantiate(int id, double tx, double ty) {
		NPC c = new NPC(id, tx, ty, sprites);
		return c;
	}

	public void move(int dx, int dy) {
		Vector dest = Vector.add(tilePos, dx, dy);
		if (!currentlyMoving) {
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
			if (MapManager.collisionAt(dest)) return;
			currentlyMoving = true;
			
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

	public void attachScriptOnInteract(Script script) {
		this.scriptOnInteract = script;
	}

	public void attachScriptForAfterMove(Script s) {
		this.scriptToContinueAfterMove = s;
	}

	public void tick(double delta) {
		if (!currentlyMoving && !movementQueue.isEmpty()) {
			Direction d = movementQueue.removeFirst();
			if (d != null) {
				move(d.dx, d.dy);
			}
		} else if (!currentlyMoving && scriptToContinueAfterMove != null && movementQueue.isEmpty()) {
			scriptToContinueAfterMove.nextLine();
			scriptToContinueAfterMove = null;
		}

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

	public void setFacing(Direction d) {
		this.facing = d;
		if (d == Direction.UP) {
			facing = Direction.UP;
			sprite = sprites.get("up");
		}
		if (d == Direction.DOWN) {
			sprite = sprites.get("down");
		}
		if (d == Direction.LEFT) {
			sprite = sprites.get("left");
		}
		if (d == Direction.RIGHT) {
			sprite = sprites.get("right");
		}
	}

	public void render(Screen screen) {
		screen.prepareRender(screenPos.getX() + MapManager.offsetX, screenPos.getY() + MapManager.offsetY, sprite, Screen.NPCS);
	}
}
