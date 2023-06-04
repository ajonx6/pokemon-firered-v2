package pokemon.level;

import pokemon.Game;
import pokemon.Settings;
import pokemon.entity.Entity;
import pokemon.entity.MapObject;
import pokemon.gfx.Screen;
import pokemon.gfx.SpriteList;
import pokemon.level.warp.Warp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    public static final Random RANDOM = new Random();
    
    public static double offsetX, offsetY;
    
    public int id;
    public int width, height;
    public String[] tiles;
    public Tile[] specialTiles;
    
    public List<Entity> entities = new ArrayList<>();
    public List<MapObject> objects = new ArrayList<>();

    public Map(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.tiles = new String[width * height];
        this.specialTiles = new Tile[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x + y * width] = "grass1";
                // RANDOM.nextInt(TileData.TILES_BY_ID.size());
            }
        }
    }

    public Map(int id, int width, int height, String[] tiles) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.tiles = new String[width * height];
        this.specialTiles = new Tile[width * height];

        System.arraycopy(tiles, 0, this.tiles, 0, tiles.length);
    }
    
    public Tile tileUnder(Entity e) {
        if (e.getTilePos().intX() >= width || e.getTilePos().intX() < 0 || e.getTilePos().intY() >= height || e.getTilePos().intY() < 0) return null;
        return specialTiles[e.getTilePos().intX() + e.getTilePos().intY() * width];
    }
    
    public void render(Screen screen) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                screen.render(x * Settings.SCALED_TILE_SIZE + offsetX, y * Settings.SCALED_TILE_SIZE + offsetY, SpriteList.TILES.get(tiles[x + y * width]));
            }
        }

        for (MapObject obj : objects) {
            obj.render(screen);
            // screen.render(obj.getTilePos().intX() * Settings.SCALED_TILE_SIZE + offsetX,  * Settings.SCALED_TILE_SIZE + offsetY, SpriteList.TILES.get(tiles[x + y * width]));
        }
        
        if (Game.debug) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile t = specialTiles[x + y * width];
                    if (t == null) continue;
                    if (t instanceof Warp) screen.render(x * Settings.SCALED_TILE_SIZE + offsetX, y * Settings.SCALED_TILE_SIZE + offsetY, SpriteList.WARP_SPRITE);
                }
            }
        }
        
        for (Entity e : entities) {
            if (e == Game.getInstance().player) continue;
            e.render(screen);
        }
    }
    
    public void addEntity(Entity e) {
        entities.add(e);
    }
    
    public void addObject(MapObject o) {
        objects.add(o);
    }
}