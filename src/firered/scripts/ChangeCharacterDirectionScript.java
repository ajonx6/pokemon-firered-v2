package firered.scripts;

import firered.Game;
import firered.entity.NPC;
import firered.entity.Direction;
import firered.map.MapManager;
import firered.util.Util;
import firered.util.Vector;

public class ChangeCharacterDirectionScript {
	public static void facePlayer(int charID) {
		NPC c = MapManager.currentMap.getCharacterByID(charID);
		Vector diff = Vector.sub(c.getTilePos(), Game.player.getTilePos());
		if (Vector.length(diff) - 1 >= Util.EPSILON) {
			System.err.println("[Error - character not adjacent to player]");
		} else {
			if (diff.intX() == 1.0) {
				c.setFacing(Direction.LEFT);
			}
			if (diff.intX() == -1.0) {
				c.setFacing(Direction.RIGHT);
			}
			if (diff.intY() == 1.0) {
				c.setFacing(Direction.UP);
			}
			if (diff.intY() == -1.0) {
				c.setFacing(Direction.DOWN);
			}
		}
	}

	public static void face(int characterID, String dir) {
		NPC c = characterID > 0 ? MapManager.currentMap.getCharacterByID(characterID) : Game.player;
		c.setFacing(Direction.getByInitial(dir));
	}
}