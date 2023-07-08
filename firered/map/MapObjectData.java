package firered.map;

import firered.Settings;
import firered.gfx.sprites.Sprite;

import java.util.HashMap;

public class MapObjectData {
    public static final HashMap<String, MapObjectData> MAP_OBJECTS = new HashMap<>();
    
    private String name;
    private int tileWidth, tileHeight;
    private Sprite fullSprite;
    private Sprite[] subSprites;
    private int[] tileData;

    public MapObjectData(String name, int tileWidth, int tileHeight, Sprite sprite, int[] tileData) {
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.fullSprite = sprite;
        this.subSprites = new Sprite[tileWidth * tileHeight];
        this.tileData = new int[tileWidth * tileHeight];
        System.arraycopy(tileData, 0, this.tileData, 0, tileData.length);

        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                subSprites[x + y * tileWidth] = fullSprite.cutIntoNewSprite(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, Settings.TILE_SIZE, Settings.TILE_SIZE);
            }
        }
        
        MAP_OBJECTS.put(name, this);
    }
    
    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Sprite getFullSprite() {
        return fullSprite;
    }

    public int getTileData(int x, int y) {
        return tileData[x + y * tileWidth];
    }
    
    public Sprite getSubSprite(int x, int y) {
        return subSprites[x + y * tileWidth];
    }
}