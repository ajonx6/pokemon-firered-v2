package pokemon.map;

import pokemon.entity.Entity;
import pokemon.gfx.Screen;
import pokemon.util.Vector;

public class MapManager {
    public static Map currentMap;
    public static double offsetX = 0, offsetY = 0;
    
    public static void loadMap(Map map) {
        currentMap = map;
    }

    public static Tile tileUnder(Entity e) {
        return currentMap.tileUnder(e);
    }

    public static boolean collisionAt(Vector tilePos) {
        return currentMap.collisionAt(tilePos);
    }
    
    public static void render(Screen screen) {
        currentMap.render(screen);
    }
}