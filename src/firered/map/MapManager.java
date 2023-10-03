package firered.map;

import firered.entity.Direction;
import firered.entity.Entity;
import firered.gfx.Screen;
import firered.pokemon.Pokemon;
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

	public static Script entityHasScript(int x, int y) {
		return currentMap.entityHasScript(x, y);
	}

	public static boolean collisionAt(Vector tilePos, Direction d) {
		return currentMap.collisionAt(tilePos, d);
	}

	public static void render(Screen screen) {
		currentMap.render(screen, 0, 0);
	}

	public static void tick(double delta) {
		currentMap.tick(delta);
	}

	public static Pokemon checkForWildBattle(Vector tilePos) {
		return currentMap.generateBattle(tilePos);
	}
}