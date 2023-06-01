package pokemon.entity;

import pokemon.Settings;
import pokemon.gfx.sprites.Sprite;

public class MapObject extends Entity {
    public int tileWidth, tileHeight;

    public MapObject(double tx, double ty, Sprite sprite) {
        super(tx, ty, sprite);
        this.tileWidth = sprite.width / Settings.TILE_SIZE;
        this.tileHeight = sprite.height / Settings.TILE_SIZE;
    }
}
