package pokemon.map;

public class MapManager {
    public static Map currentMap;
    public static double offsetX = 0, offsetY = 0;
    
    public static void swapMap(Map map) {
        currentMap = map;
    }
}