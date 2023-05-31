package pokemon.level;

import pokemon.Game;
import pokemon.Settings;
import pokemon.entity.Entity;
import pokemon.gfx.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    public static final Random RANDOM = new Random();
    
    public static double offsetX, offsetY;
    
    public int width, height;
    public int[] tiles;
    
    public List<Entity> entities = new ArrayList<>();

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x + y * width] = RANDOM.nextInt(TileData.TILES.size());
            }
        }
    }
    
    public void render(Screen screen) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                screen.render(x * Settings.SCALED_TILE_SIZE + offsetX, y * Settings.SCALED_TILE_SIZE + offsetY, TileData.TILES.get(tiles[x + y * width]));
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
}