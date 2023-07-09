package firered.scripts;

import firered.entity.Player;

import java.util.HashMap;

public class GameVariablesForScripts {
	public static HashMap<String, String> vars = new HashMap<>();
	
	public static void init() {
		vars.put("player_name", Player.NAME);
	}
}