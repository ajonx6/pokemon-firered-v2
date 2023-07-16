package firered;

import firered.entity.NPC;
import firered.entity.MapObject;
import firered.entity.Player;
import firered.gfx.sprites.Sprite;
import firered.map.Map;
import firered.map.MapObjectData;
import firered.map.WildPokemonData;
import firered.map.WildPokemonRarity;
import firered.map.warp.Warp;
import firered.pokemon.BasePokemon;
import firered.pokemon.StatType;
import firered.pokemon.StatusEffect;
import firered.pokemon.Type;
import firered.pokemon.moves.*;
import firered.scripts.Script;
import firered.util.Util;

import java.io.File;
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
			String[] positions = tokens[2].split("/");
			for (String pos : positions) {
				if (pos.startsWith("l")) {
					String[] tks = pos.substring(1).split(",");
					int x = Integer.parseInt(tks[0]), y = Integer.parseInt(tks[1]);
					int dx = Integer.parseInt(tks[2]), dy = Integer.parseInt(tks[3]);
					int nx = Integer.parseInt(tks[4]), ny = Integer.parseInt(tks[5]);
					for (int xx = 0; xx < nx; xx++) {
						for (int yy = 0; yy < ny; yy++) {
							map.addObject(new MapObject(x + xx * dx, y + yy * dy, MapObjectData.MAP_OBJECTS.get(tokens[1])));
						}
					}
				} else {
					String[] ps = pos.split(",");
					map.addObject(new MapObject(Integer.parseInt(ps[0]), Integer.parseInt(ps[1]), MapObjectData.MAP_OBJECTS.get(tokens[1])));
				}
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

		while (data.get(currentDataIndex).startsWith("warp ")) {
			String line = data.get(currentDataIndex++);
			String[] tokens = line.split(" ");
			if (tokens[1].equals("#")) continue;
			String[] posIn = tokens[1].split(",");
			String dest = tokens[2];
			String[] posOut = tokens[3].split(",");
			Warp w1 = new Warp(Integer.parseInt(posIn[0]), Integer.parseInt(posIn[1]), map);
			Warp w2 = new Warp(Integer.parseInt(posOut[0]), Integer.parseInt(posOut[1]), Map.MAPS_MAP.get(dest));
			w1.connect(w2);
		}

		if (data.get(currentDataIndex).startsWith("wild ")) {
			String line = data.get(currentDataIndex++);
			String[] tokens = line.split(" ");
			WildPokemonRarity wpr = WildPokemonRarity.getRarityBySymbol(tokens[1]);
			String[] pokes = tokens[2].split("/");
			List<WildPokemonData> wpd = new ArrayList<>();
			for (String dat : pokes) {
				String[] ds = dat.split(",");
				wpd.add(new WildPokemonData(BasePokemon.BASE_POKEMON.get(ds[0]), Integer.parseInt(ds[1]), Integer.parseInt(ds[2]), Double.parseDouble(ds[3])));
			}
			map.setWildPokemon(wpr, wpd);
		}
	}

	public static void loadMoves() {
		File parent = new File("res/moves/");
		File[] files = parent.listFiles();
		for (File f : files) {
			String fileName = f.getName();
			List<String> data = Util.load("moves/" + fileName);

			String id = data.get(0);
			String name = data.get(1);
			Type type = Type.getTypeByName(data.get(2));
			int pp = Integer.parseInt(data.get(3));
			Move move = new Move(id, name, type, pp);

			for (int i = 4; i < data.size(); i++) {
				String[] tokens = data.get(i).split(" ");
				if (tokens[0].equals("damage")) {
					if (tokens.length == 3) {
						move.addModifier(new DamageOnlyWithStatus(Integer.parseInt(tokens[1]), StatusEffect.getTypeByName(tokens[2])));
					} else {
						move.addModifier(new NormalDamage(Integer.parseInt(tokens[1])));
					}
				} else if (tokens[0].equals("accuracy")) {
					if (tokens.length == 2) {
						move.addModifier(new AccuracyModifier(Double.parseDouble(tokens[1])));
					} else {
						move.addModifier(new AccuracyModifier());
					}
				} else if (tokens[0].equals("restore")) {
					move.addModifier(new RestoreHealth(Double.parseDouble(tokens[1]), Boolean.parseBoolean(tokens[2])));
				} else if (tokens[0].equals("stat")) {
					if (tokens.length == 4) {
						move.addModifier(new StatValueModifier(StatType.getTypeByName(tokens[1]), Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3])));
					} else {
						move.addModifier(new StatValueModifier(StatType.getTypeByName(tokens[1]), Integer.parseInt(tokens[2])));
					}
				} else if (tokens[0].equals("selfstat")) {
					move.addModifier(new SelfStatValueModifier(StatType.getTypeByName(tokens[1]), Integer.parseInt(tokens[2])));
				} else if (tokens[0].equals("status")) {
					if (tokens.length == 3) {
						move.addModifier(new StatusModifier(StatusEffect.getTypeByName(tokens[1]), Double.parseDouble(tokens[2])));
					} else {
						move.addModifier(new StatusModifier(StatusEffect.getTypeByName(tokens[1])));
					}
				} else if (tokens[0].equals("selfstatus")) {
					move.addModifier(new SelfStatusModifier(StatusEffect.getTypeByName(tokens[1])));
				} else if (tokens[0].equals("priority")) {
					move.addModifier(new PriorityMove(Integer.parseInt(tokens[1])));
				}
			}
		}
	}

	public static void loadBasePokemon() {
		File parent = new File("res/pokemon/");
		File[] folders = parent.listFiles();
		for (File f : folders) {
			if (f.isDirectory()) {
				String fileName = f.getName();
				List<String> data = Util.load("pokemon/" + fileName + "/pokedata.pd");

				String name = data.get(0);
				int pokedexNumber = Integer.parseInt(data.get(1));
				Type type1 = Type.getTypeByName(data.get(2));
				Type type2 = Type.getTypeByName(data.get(3));
				String ability = data.get(4);
				int height = Integer.parseInt(data.get(10));
				int weight = Integer.parseInt(data.get(11));
				BasePokemon basePokemon = BasePokemon.addPokemon(name, pokedexNumber, type1, type2, ability, height, weight);

				basePokemon.setSprites(new Sprite("pokemon/" + fileName + "/front_normal"), new Sprite("pokemon/" + fileName + "/back_normal"), new Sprite("pokemon/" + fileName + "/front_shiny"), new Sprite("pokemon/" + fileName + "/back_shiny"));

				String[] levelMoves = data.get(5).split(" ");
				for (int i = 0; i < levelMoves.length; i += 2) {
					Move m = Move.MOVES.get(levelMoves[i + 1]);
					if (m == null) continue;
					basePokemon.addLevelMove(m, Integer.parseInt(levelMoves[i]));
				}
				String[] machineMoves = data.get(6).split(" ");
				for (String move : machineMoves) {
					Move m = Move.MOVES.get(move);
					if (m == null) continue;
					basePokemon.addMachineMove(m);
				}

				int baseExp = Integer.parseInt(data.get(7));
				String[] bases = data.get(8).split(" ");
				basePokemon.setBaseStats(Integer.parseInt(bases[0]), Integer.parseInt(bases[1]), Integer.parseInt(bases[2]), Integer.parseInt(bases[3]), Integer.parseInt(bases[4]), Integer.parseInt(bases[5]), baseExp);

				String[] evs = data.get(9).split(" ");
				basePokemon.setEVStats(Integer.parseInt(evs[0]), Integer.parseInt(evs[1]), Integer.parseInt(evs[2]), Integer.parseInt(evs[3]), Integer.parseInt(evs[4]), Integer.parseInt(evs[5]));
			}
		}
	}
}