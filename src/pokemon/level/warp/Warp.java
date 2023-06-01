package pokemon.level.warp;

import pokemon.Game;
import pokemon.Settings;
import pokemon.entity.Character;
import pokemon.entity.Entity;
import pokemon.entity.Player;
import pokemon.level.Map;
import pokemon.level.Tile;
import pokemon.util.Vector;

import java.util.Set;

public class Warp extends Tile {
    public Warp otherWarp;

    public Warp(int tx, int ty, Map map) {
        super(tx, ty);
        map.specialTiles[tx + ty * map.width] = this;
    }
    
    public void connect(Warp other) {
        this.otherWarp = other;
        other.otherWarp = this;
    }

    public void action(Character e) {
        if (!e.hasMoved()) return;
        e.setTilePos(new Vector(otherWarp.tx, otherWarp.ty));
        e.setWorldPos(Vector.mul(e.getTilePos(), Settings.SCALED_TILE_SIZE));
        if (e instanceof Player) {
            e.getScreenPos().set(Game.WIDTH / 2 - e.getSprite().width / 2, Game.HEIGHT / 2 - e.getSprite().height / 2);
            e.setScreenPos(Vector.add(e.getScreenPos(), 0, Settings.SCALED_TILE_SIZE - e.getSprite().height / 2));
            Map.offsetX -= (otherWarp.tx - tx) * Settings.SCALED_TILE_SIZE;
            Map.offsetY -= (otherWarp.ty - ty) * Settings.SCALED_TILE_SIZE;
        } else {
            e.setScreenPos(Vector.sub(Vector.mul(e.getTilePos(), Settings.SCALED_TILE_SIZE), 0, e.getSprite().height - Settings.SCALED_TILE_SIZE));
        }
        e.setHasMoved(false);
    }
}