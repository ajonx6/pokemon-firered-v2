package firered.gfx;

import firered.gfx.sprites.Sprite;

import java.util.HashMap;

public class SpriteList {
    public static final HashMap<String, Sprite> TILES = new HashMap<>();
    public static final HashMap<String, Sprite> OBJECTS = new HashMap<>();
    public static final Sprite WARP_SPRITE = new Sprite("tiles/warp");
    
    public static void init() {
        TILES.put("inv", new Sprite("tiles/inv"));
        TILES.put("grass1", new Sprite("tiles/grass1"));
        TILES.put("grass2", new Sprite("tiles/grass2"));
        TILES.put("grass3", new Sprite("tiles/grass3"));
        TILES.put("water", new Sprite("tiles/water"));

        OBJECTS.put("tree", new Sprite("objects/tree"));
    }
}