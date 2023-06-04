package pokemon.entity;

import pokemon.Settings;
import pokemon.gfx.Screen;
import pokemon.level.Map;
import pokemon.level.MapObjectData;

public class MapObject extends Entity {
    private MapObjectData objectData;

    public MapObject(double tx, double ty, MapObjectData objectData) {
        super(tx, ty, objectData.getFullSprite());
        this.objectData = objectData;
    }

    public void tick(double delta) {
    }

    public void render(Screen screen) {
        for (int y = 0; y < objectData.getTileHeight(); y++) {
            for (int x = 0; x < objectData.getTileWidth(); x++) {
                screen.prepareRender(screenPos.getX() + Map.offsetX + Settings.SCALED_TILE_SIZE * x, screenPos.getY() + Map.offsetY + Settings.SCALED_TILE_SIZE * y, objectData.getSubSprite(x, y), objectData.getTileData(x, y));
            }
        }
    }
}
