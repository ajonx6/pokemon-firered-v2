package firered.helper;

import firered.gfx.sprites.Sprite;
import firered.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreatePokemonSubfiles {
	public static final String PATH_TO_FOLDER = "pokemon/";
	public static final String NAME_LIST = "gen1pokemon.txt";
	public static final String ALL_POKEMON_NAME = "all_pokemon_gen1";
	public static final int SQUARE_SIZE = 64;
	public static final int SMALL_OFFSET = 65;
	public static final int BIG_OFFSET_X = 130;
	public static final int BIG_OFFSET_Y = 164;
	public static final int POKEMON_X = 15;
	public static final int START_X = 11;
	public static final int START_Y = 45;
	
	public static void main(String[] args) throws IOException {
		List<String> names = Util.load(PATH_TO_FOLDER + NAME_LIST);
		Sprite allPokemon = new Sprite(PATH_TO_FOLDER + ALL_POKEMON_NAME);

		// for (int i = 0; i < 1; i++) { 
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i).toLowerCase();
			String filteredName = name.replace("♀", "-f").replace("♂", "-m").replace("\'", "").replace(". ", "-");

			String path = "res/" + PATH_TO_FOLDER + "/" + filteredName;
			System.out.println(name + " (" + (i + 1) + " / " + names.size() + ")");

			writePokemonData(i, name, filteredName, path);

			int pokemonX = i % POKEMON_X;
			int pokemonY = i / POKEMON_X;
			int x = START_X + BIG_OFFSET_X * pokemonX;
			int y = START_Y + BIG_OFFSET_Y * pokemonY;
			Sprite frontNormal = allPokemon.cutIntoNewSprite(x, y, SQUARE_SIZE, SQUARE_SIZE);
			Sprite frontShiny = allPokemon.cutIntoNewSprite(x + SMALL_OFFSET, y, SQUARE_SIZE, SQUARE_SIZE);
			Sprite backNormal = allPokemon.cutIntoNewSprite(x, y + SMALL_OFFSET, SQUARE_SIZE, SQUARE_SIZE);
			Sprite backShiny = allPokemon.cutIntoNewSprite(x + SMALL_OFFSET, y + SMALL_OFFSET, SQUARE_SIZE, SQUARE_SIZE);

			saveImage(trim(frontNormal), path + "/" + "front_normal");
			saveImage(trim(frontShiny), path + "/" + "front_shiny");
			saveImage(trim(backNormal), path + "/" + "back_normal");
			saveImage(trim(backShiny), path + "/" + "back_shiny");
		}
	}
	
	public static void writePokemonData(int loopNumber, String name, String filteredName, String path) throws IOException {
		URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + filteredName + "/");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		StringBuilder inline = new StringBuilder();
		Scanner scanner = new Scanner(url.openStream());
		while (scanner.hasNext()) {
			inline.append(scanner.nextLine());
		}

		JSONObject all = (JSONObject) JSONValue.parse(inline.toString());
		String type1 = getType(1, all);
		String type2 = getType(2, all);
		String ability = (String) ((JSONObject) ((JSONObject) ((JSONArray) all.get("abilities")).get(0)).get("ability")).get("name");

		List<String> levelMoves = new ArrayList<>();
		List<String> machineMoves = new ArrayList<>();
		JSONArray moves = (JSONArray) all.get("moves");
		for (int moveIndex = 0; moveIndex < moves.size(); moveIndex++) {
			JSONObject move = (JSONObject) moves.get(moveIndex);
			String moveName = (String) ((JSONObject) move.get("move")).get("name");
			JSONArray versionDetailsArr = (JSONArray) move.get("version_group_details");
			for (int vi = 0; vi < versionDetailsArr.size(); vi++) {
				JSONObject versionDetails = (JSONObject) versionDetailsArr.get(vi);
				JSONObject versionGroup = (JSONObject) versionDetails.get("version_group");
				String version = (String) versionGroup.get("name");
				if (version.equals("firered-leafgreen")) {
					JSONObject learnMethod = (JSONObject) versionDetails.get("move_learn_method");
					String method = (String) learnMethod.get("name");
					if (method.equals("machine")) machineMoves.add(moveName);
					else if (method.equals("level-up")) {
						long level = (Long) versionDetails.get("level_learned_at");
						levelMoves.add(Long.toString(level));
						levelMoves.add(moveName);
					}
				}
			}
		}
		
		int basehp = (int) getBaseStat(0, all);
		int baseatt = (int) getBaseStat(1, all);
		int basedef = (int) getBaseStat(2, all);
		int basespatt = (int) getBaseStat(3, all);
		int basespdef = (int) getBaseStat(4, all);
		int basespeed = (int) getBaseStat(5, all);
		int baseexp = (int) getBaseExp(all);

		int evhp = (int) getEVStat(0, all);
		int evatt = (int) getEVStat(1, all);
		int evdef = (int) getEVStat(2, all);
		int evspatt = (int) getEVStat(3, all);
		int evspdef = (int) getEVStat(4, all);
		int evspeed = (int) getEVStat(5, all);

		int height = (int) getHeight(all);
		int weight = (int) getWeight(all);

		new File(path).mkdir();
		File data = new File(path + "/pokedata.pd");
		data.createNewFile();
		
		try (FileWriter writer = new FileWriter(path + "/pokedata.pd")) {
			writer.write(name + "\n");
			writer.write((loopNumber + 1) + "\n");
			writer.write(type1 + "\n");
			writer.write(type2 + "\n");
			writer.write(ability + "\n");
			for (String s : levelMoves) {
				writer.write(s + " ");
			}
			writer.write("\n");
			for (String s : machineMoves) {
				writer.write(s + " ");
			}
			writer.write("\n");
			writer.write(baseexp + "\n");
			writer.write(basehp + " " + baseatt + " " + basedef + " " + basespatt + " " + basespdef + " " + basespeed + " " + "\n");
			writer.write(evhp + " " + evatt + " " + evdef + " " + evspatt + " " + evspdef + " " + evspeed + " " + "\n");
			writer.write(height + "\n");
			writer.write(Integer.toString(weight));
		}
	}

	public static String getType(int num, JSONObject all) {
		JSONArray types = (JSONArray) all.get("types");
		if (num == 1) return (String) ((JSONObject) ((JSONObject) types.get(0)).get("type")).get("name");
		else if (num == 2)
			return types.size() > 1 ? (String) ((JSONObject) ((JSONObject) types.get(1)).get("type")).get("name") : "null";
		else return "null";
	}

	public static long getBaseExp(JSONObject all) {
		return (Long) all.get("base_experience");
	}

	public static long getHeight(JSONObject all) {
		return (Long) all.get("height");
	}

	public static long getWeight(JSONObject all) {
		return (Long) all.get("weight");
	}

	public static long getBaseStat(int index, JSONObject all) {
		JSONArray stats = (JSONArray) all.get("stats");
		return (Long) ((JSONObject) stats.get(index)).get("base_stat");
	}

	public static long getEVStat(int index, JSONObject all) {
		JSONArray stats = (JSONArray) all.get("stats");
		return (Long) ((JSONObject) stats.get(index)).get("effort");
	}

	public static Sprite trim(Sprite sprite) {
		int colourToRemove = sprite.pixels[0];
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
		for (int y = 0; y < sprite.height; y++) {
			for (int x = 0; x < sprite.width; x++) {
				if (sprite.pixels[x + y * sprite.width] != colourToRemove) {
					if (x < minX) minX = x;
					if (y < minY) minY = y;
					if (x > maxX) maxX = x;
					if (y > maxY) maxY = y;
				}
			}
		}

		int newW = maxX - minX + 1;
		int newH = maxY - minY + 1;
		int[] newPixels = new int[newW * newH];
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int c = sprite.pixels[x + y * sprite.width];
				if (c != colourToRemove) newPixels[(x - minX) + (y - minY) * newW] = c;
			}
		}

		return new Sprite(newW, newH, newPixels);
	}

	public static void saveImage(Sprite sprite, String path) throws IOException {
		BufferedImage img = new BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, sprite.width, sprite.height, sprite.pixels, 0, sprite.width);
		ImageIO.write(img, "PNG", new File(path + ".png"));
	}
}