package firered.map;

import firered.Game;
import firered.Settings;
import firered.entity.NPC;
import firered.entity.Entity;
import firered.entity.MapObject;
import firered.gfx.Screen;
import firered.gfx.SpriteList;
import firered.map.warp.Warp;
import firered.scripts.Script;
import firered.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Map {
	public static final Random RANDOM = new Random();
	public static final HashMap<String, Map> MAPS_MAP = new HashMap<>();

	private String name;
	private int id;
	private int width, height;
	private String[] tiles;
	private Tile[] specialTiles;
	private int[] collisionData;
	private Script[] scripts;

	private List<Entity> entities = new ArrayList<>();
	private HashMap<Integer, NPC> characters = new HashMap<>();
	private List<MapObject> objects = new ArrayList<>();

	public Map(String name, int id, int width, int height) {
		this.name = name;
		this.id = id;
		this.width = width;
		this.height = height;
		this.tiles = new String[width * height];
		this.specialTiles = new Tile[width * height];
		this.collisionData = new int[width * height];
		this.scripts = new Script[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + y * width] = "grass1";
				// RANDOM.nextInt(TileData.TILES_BY_ID.size());
			}
		}

		MAPS_MAP.put(name, this);
	}

	public Map(String name, int id, int width, int height, String[] tiles) {
		this.name = name;
		this.id = id;
		this.width = width;
		this.height = height;
		this.tiles = new String[width * height];
		this.specialTiles = new Tile[width * height];
		this.collisionData = new int[width * height];
		this.scripts = new Script[width * height];
		System.arraycopy(tiles, 0, this.tiles, 0, tiles.length);

		MAPS_MAP.put(name, this);
	}

	public Tile tileUnder(Entity e) {
		if (e.getTilePos().intX() >= width || e.getTilePos().intX() < 0 || e.getTilePos().intY() >= height || e.getTilePos().intY() < 0)
			return null;
		return specialTiles[e.getTilePos().intX() + e.getTilePos().intY() * width];
	}

	public Script scriptUnder(int x, int y) {
		if (x >= width || x < 0 || y >= height || y < 0)
			return null;
		return scripts[x + y * width];
	}

	public Script entityHasScript(int x, int y) {
		if (x >= width || x < 0 || y >= height || y < 0)
			return null;
		for (NPC c : characters.values()) {
			if (c.getTilePos().intX() == x && c.getTilePos().intY() == y) return c.scriptOnInteract;
		}
		return null;
	}

	public boolean collisionAt(Vector tilePos) {
		if (tilePos.intX() < 0 || tilePos.intY() < 0 || tilePos.intX() >= width || tilePos.intY() >= height)
			return true;
		for (Entity e : entities) {
			if (Vector.equals(e.getTilePos(), tilePos)) return true;
		}
		return collisionData[tilePos.intX() + tilePos.intY() * width] == 1;
	}

	public void tick(double delta) {
		for (Entity e : entities) {
			e.tick(delta);
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Script s = scripts[x + y * width];
				if (s != null) s.tick(delta);
			}
		}
	}

	public void render(Screen screen) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				screen.prepareRender(x * Settings.TILE_SIZE + MapManager.offsetX, y * Settings.TILE_SIZE + MapManager.offsetY, SpriteList.TILES.get(tiles[x + y * width]), Screen.TILE_LAYER);
			}
		}

		for (MapObject obj : objects) {
			obj.render(screen);
			// screen.render(obj.getTilePos().intX() * Settings.TILE_SIZE + MapManager.offsetX,  * Settings.TILE_SIZE + MapManager.offsetY, SpriteList.TILES.get(tiles[x + y * width]));
		}

		if (Game.debug) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Tile t = specialTiles[x + y * width];
					if (t == null) continue;
					if (t instanceof Warp)
						screen.prepareRender(x * Settings.TILE_SIZE + MapManager.offsetX, y * Settings.TILE_SIZE + MapManager.offsetY, SpriteList.WARP_SPRITE, Screen.UI_ELEMENTS);
				}
			}
		}

		for (Entity e : entities) {
			if (e == Game.player) continue;
			e.render(screen);
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
		if (e instanceof NPC) characters.put(((NPC) e).id, (NPC) e);
	}

	public void addObject(MapObject o) {
		objects.add(o);
		for (int y = 0; y < o.getObjectData().getTileHeight(); y++) {
			for (int x = 0; x < o.getObjectData().getTileWidth(); x++) {
				if (o.getTilePos().intX() + x < 0 || o.getTilePos().intY() + y < 0 || o.getTilePos().intX() + x >= width || o.getTilePos().intY() + y >= height)
					continue;
				if (o.getObjectData().getTileData(x, y) == 1)
					collisionData[o.getTilePos().intX() + x + (o.getTilePos().intY() + y) * width] = 1;
			}
		}
	}

	public void addScript(Script s, int tx, int ty) {
		this.scripts[tx + ty * width] = s;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String[] getTiles() {
		return tiles;
	}

	public Tile[] getSpecialTiles() {
		return specialTiles;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public NPC getCharacterByID(int id) {
		return characters.get(id);
	}

	public List<MapObject> getObjects() {
		return objects;
	}

	public NPC getNPCByID(int id) {
		for (NPC npc : characters.values()) {
			if (npc.id == id) return npc;
		}
		return null;
	}
}