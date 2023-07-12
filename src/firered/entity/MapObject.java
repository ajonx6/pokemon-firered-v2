package firered.entity;

import firered.Settings;
import firered.gfx.Screen;
import firered.map.MapManager;
import firered.map.MapObjectData;

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
				int tileX = tilePos.intX() + x;
				int tileY = tilePos.intY() + y;
				if (tileX < 0 || tileY < 0 || tileX >= MapManager.currentMap.getWidth() || tileY >= MapManager.currentMap.getHeight())
					continue;
				screen.prepareRender(screenPos.getX() + MapManager.offsetX + Settings.TILE_SIZE * x, screenPos.getY() + MapManager.offsetY + Settings.TILE_SIZE * y, objectData.getSubSprite(x, y), objectData.getTileData(x, y));
			}
		}
	}

	public MapObjectData getObjectData() {
		return objectData;
	}
}
