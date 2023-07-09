package firered.map.warp;

import firered.Game;
import firered.Settings;
import firered.entity.Character;
import firered.entity.Player;
import firered.map.Map;
import firered.map.MapManager;
import firered.map.Tile;
import firered.util.Vector;

public class Warp extends Tile {
    public Warp otherWarp;

    public Warp(int tx, int ty, Map map) {
        super(tx, ty);
        map.getSpecialTiles()[tx + ty * map.getWidth()] = this;
    }
    
    public void connect(Warp other) {
        this.otherWarp = other;
        other.otherWarp = this;
    }

    public void action(Character e) {
        if (!e.hasMoved()) return;
        e.setTilePos(new Vector(otherWarp.tx, otherWarp.ty));
        e.setWorldPos(Vector.mul(e.getTilePos(), Settings.TILE_SIZE));
        if (e instanceof Player) {
            e.getScreenPos().set(Game.WIDTH / 2 - e.getSprite().width / 2, Game.HEIGHT / 2 - e.getSprite().height / 2);
            e.setScreenPos(Vector.add(e.getScreenPos(), 0, Settings.TILE_SIZE - e.getSprite().height / 2));
            MapManager.offsetX -= (otherWarp.tx - tx) * Settings.TILE_SIZE;
            MapManager.offsetY -= (otherWarp.ty - ty) * Settings.TILE_SIZE;
        } else {
            e.setScreenPos(Vector.sub(Vector.mul(e.getTilePos(), Settings.TILE_SIZE), 0, e.getSprite().height - Settings.TILE_SIZE));
        }
        e.setHasMoved(false);
    }
}