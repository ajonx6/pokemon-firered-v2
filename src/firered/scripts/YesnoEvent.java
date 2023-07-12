package firered.scripts;

import firered.Game;
import firered.State;
import firered.util.Util;

public class YesnoEvent {
	public static void print(Script script, String yesFuncName, String noFuncName, String message) {
		String res = "";
		String currentVar = "";
		boolean var = false;
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == '$') {
				if (!var) {
					var = true;
				} else {
					res += script.variables.get(currentVar);
					currentVar = "";
					var = false;
				}
			} else {
				if (var) currentVar += message.charAt(i);
				else res += message.charAt(i);
			}
		}
		Game.gameState = State.MESSAGE_BOX;
		Game.messageBox.addText(Util.scriptToMessages(res));
		// Game.messageBox.attachScript(script);
		Game.messageBox.useYesnoBox();
		Game.yesnoBox.setFunctions(script, yesFuncName, noFuncName);
	}
}
