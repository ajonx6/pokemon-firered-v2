package firered.scripts;

import firered.Game;
import firered.State;
import firered.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Script {
	public HashMap<String, String> variables = new HashMap<>();
	public HashMap<String, Function> functions = new HashMap<>();

	public Function currentFunction;
	public Stack<Function> callStack = new Stack<>();

	public Script(String path) {
		List<String> lines = Util.load(path);
		String currentName = "";
		for (String line : lines) {
			if (line.trim().equals("")) continue;
			if (line.startsWith("@")) {
				currentName = line.substring(1);
				functions.put(currentName, new Function(currentName));
			} else {
				functions.get(currentName).addLine(line);
			}
		}

		currentFunction = functions.get("main");
	}

	public void nextLine() {
		String line = currentFunction.getLine();
		String[] tokens = line.split(" ");
		if (tokens[0].equals("print")) {
			PrintEvent.print(this, line.substring(6));
		} else if (tokens[0].equals("loadvar")) {
			variables.put(tokens[2], GameVariablesForScripts.vars.get(tokens[1]));
			nextLine();
		} else if (tokens[0].equals("jump")) {
			Function toCall = functions.get(tokens[1]);
			toCall.startFunction();
			callStack.push(currentFunction);
			currentFunction = toCall;
			nextLine();
		} else if (tokens[0].equals("end")) {
			if (callStack.isEmpty()) {
				Game.gameState = State.NORMAL;
			} else {
				currentFunction = callStack.pop();
				nextLine();
			}
		} else {
			System.out.println(line);
			nextLine();
		}
	}

	public void startScript() {
		nextLine();
	}
}