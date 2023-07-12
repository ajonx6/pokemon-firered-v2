package firered.scripts;

import firered.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameVariablesForScripts {
	public static List<String> flags = new ArrayList<>();
	public static HashMap<String, String> vars = new HashMap<>();
	
	public static void init() {
		vars.put("player_name", Player.NAME);
		vars.put("rival_name", Player.RIVAL_NAME);
	}

	public static void setFlag(String s) {
		flags.add(s);
	}
}