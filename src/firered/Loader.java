package firered;

import firered.entity.NPC;
import firered.entity.MapObject;
import firered.entity.Player;
import firered.gfx.sprites.Sprite;
import firered.map.Map;
import firered.map.MapObjectData;
import firered.scripts.Script;
import firered.util.Util;

import java.util.*;

public class Loader {
	public static final Random RANDOM = new Random(12345);

	public static void loadObjects() {
		String pathToMapData = "objects/objects.data";
		List<String> data = Util.load(pathToMapData);
		int currentDataIndex = 0;

		while (currentDataIndex < data.size()) {
			String[] objDetails = data.get(currentDataIndex++).split(",");
			String name = objDetails[0];
			int width = Integer.parseInt(objDetails[1]);
			int height = Integer.parseInt(objDetails[2]);
			int[] tileData = new int[width * height];
			for (int y = 0; y < height; y++) {
				String line = data.get(currentDataIndex++);
				for (int x = 0; x < width; x++) {
					int tile = Character.getNumericValue(line.charAt(x));
					tileData[x + y * width] = tile;
				}
			}
			new MapObjectData(name, width, height, new Sprite("objects/" + name), tileData);
		}
	}

	public static void loadMaps() {
		String pathToMapData = "map/map.data";
		List<String> mapNames = Util.load(pathToMapData);
		for (String name : mapNames) {
			loadMap(name);
		}
	}

	private static void loadMap(String name) {
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

		Map map = new Map(name, id, width, height, tileData);

		while (data.get(currentDataIndex).startsWith("obj ")) {
			String line = data.get(currentDataIndex++);
			String[] tokens = line.split(" ");
			if (tokens[1].equals("#")) continue;
			String[] pos = tokens[2].split(",");
			for (int i = 0; i < pos.length; i += 2) {
				map.addObject(new MapObject(Integer.parseInt(pos[i]), Integer.parseInt(pos[i + 1]), MapObjectData.MAP_OBJECTS.get(tokens[1])));
			}
		}

		while (data.get(currentDataIndex).startsWith("char ")) {
			String line = data.get(currentDataIndex++);
			String[] tokens = line.split(" ");
			if (tokens[1].equals("#")) continue;
			String charType = tokens[1];
			String[] pos = tokens[3].split(",");
			NPC c = NPC.CHARACTERS.get(charType).instantiate(Integer.parseInt(tokens[2]), Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
			if (c instanceof Player) {
				Game.player = (Player) c;
			}
			map.addEntity(c);
		}

		while (data.get(currentDataIndex).startsWith("script ")) {
			String line = data.get(currentDataIndex++);
			String[] tokens = line.split(" ");
			if (tokens[1].equals("#")) continue;
			String type = tokens[1];
			Script s = new Script("map/" + name + "/" + tokens[2]);
			if (type.equals("object") || type.equals("tile")) {
				String[] pos = tokens[3].split(",");
				map.addScript(s, Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
			} else if (type.equals("char")) {
				map.getNPCByID(Integer.parseInt(tokens[3])).attachScriptOnInteract(s);
			}
		}
	}
}