package pokemon.level;

import pokemon.gfx.sprites.Sprite;

import java.util.HashMap;

public class TileData {
    public static final HashMap<Integer, Sprite> TILES = new HashMap<Integer, Sprite>();
    
    public static void init() {
        TILES.put(0, new Sprite("tiles/inv"));
        TILES.put(1, new Sprite("tiles/grass1"));
        TILES.put(2, new Sprite("tiles/grass2"));
        TILES.put(3, new Sprite("tiles/grass3"));
        TILES.put(4, new Sprite("tiles/water"));
    }
}