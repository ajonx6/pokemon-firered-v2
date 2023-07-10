package firered.map;

import firered.entity.Entity;
import firered.entity.Player;
import firered.gfx.Screen;
import firered.scripts.Script;
import firered.util.Vector;

public class MapManager {
	public static Map currentMap;
	public static double offsetX = 0, offsetY = 0;

	public static void loadMap(Map map) {
		currentMap = map;
	}

	public static Tile tileUnder(Entity e) {
		return currentMap.tileUnder(e);
	}

	public static Script scriptUnder(Entity e) {
		return currentMap.scriptUnder(e.getTilePos().intX(), e.getTilePos().intY());
	}

	public static Script scriptUnder(int x, int y) {
		return currentMap.scriptUnder(x, y);
	}

	public static boolean collisionAt(Vector tilePos) {
		return currentMap.collisionAt(tilePos);
	}

	public static void render(Screen screen) {
		currentMap.render(screen);
	}
}