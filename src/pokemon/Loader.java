package pokemon;

import pokemon.entity.MapObject;
import pokemon.level.Map;
import pokemon.gfx.SpriteList;
import pokemon.util.Util;

import java.util.*;

public class Loader {
    public static final Random RANDOM = new Random(12345);

    public static Map loadMap(String name) {
        List<String> data = Util.load("map/" + name + "/map.data");
        int currentDataIndex = 0;

        int id = Integer.parseInt(data.get(currentDataIndex++));

        String[] mapDims = data.get(currentDataIndex++).split(",");
        int width = Integer.parseInt(mapDims[0]);
        int height = Integer.parseInt(mapDims[1]);

        HashMap<Integer, List<String>> tileIDs = new HashMap<>();
        while (data.get(currentDataIndex).startsWith("td ")) {
            String line = data.get(currentDataIndex++);
            String[] names = line.split(" ")[1].split("=")[1].split(",");
            List<String> ids = new ArrayList<>();
            Collections.addAll(ids, names);
            tileIDs.put(Integer.parseInt(line.split(" ")[1].split("=")[0]), ids);
        }

        String[] tileData = new String[width * height];
        for (int y = 0; y < height; y++) {
            String line = data.get(currentDataIndex++);
            for (int x = 0; x < width; x++) {
                int n = Character.getNumericValue(line.charAt(x));
                List<String> names = tileIDs.get(n);
                int nameIndex = RANDOM.nextInt(names.size());
                tileData[x + y * width] = names.get(nameIndex);
            }
        }

        Map map = new Map(id, width, height, tileData);

        while (data.get(currentDataIndex).startsWith("obj ")) {
            String line = data.get(currentDataIndex++);
            String objName = line.split(" ")[1];
            String[] pos = line.split(" ")[2].split(",");
            for (int i = 0; i < pos.length; i += 2) {
                map.addObject(new MapObject(Integer.parseInt(pos[i]), Integer.parseInt(pos[i + 1]), SpriteList.OBJECTS.get(objName)));
            }
        }

        return map;
    }
}