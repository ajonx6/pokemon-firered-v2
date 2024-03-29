package firered.scripts;

import firered.Game;
import firered.entity.NPC;
import firered.entity.Direction;
import firered.map.MapManager;

import java.util.ArrayList;
import java.util.List;

public class MoveScript {
	public static void Move(Script s, int characterID, String movements) {
		NPC c = characterID > 0 ? MapManager.currentMap.getCharacterByID(characterID) : Game.player;
		List<Direction> dirs = new ArrayList<>();
		String[] tokens = movements.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			Direction d = Direction.getByInitial(tokens[i]);
			if (d != null) dirs.add(d);
		}
		c.attachScriptForAfterMove(s);
		c.movementQueue.addAll(dirs);
	}
}