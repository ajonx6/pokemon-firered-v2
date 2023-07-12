package firered.scripts;

import firered.Game;
import firered.State;
import firered.util.Timer;
import firered.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Script {
	public HashMap<String, String> variables = new HashMap<>();
	public HashMap<String, Function> functions = new HashMap<>();

	public Function currentFunction;
	public Stack<Function> callStack = new Stack<>();
	public Timer timer;

	public Script(String path) {
		List<String> lines = Util.load(path + ".scr");
		String currentName = "";
		for (String line : lines) {
			if (line.trim().equals("")) continue;
			if (line.startsWith("@")) {
				currentName = line.substring(1).trim();
				functions.put(currentName, new Function(currentName));
			} else {
				functions.get(currentName).addLine(line);
			}
		}
	}

	public void tick(double delta) {
		if (timer != null && timer.tick(delta)) {
			timer = null;
			nextLine();
		}
	}

	/////////////////////////////////
	// IF CID NEGATIVE, MEANS SELF //
	/////////////////////////////////
	public void nextLine() {
		String line = currentFunction.getLine();
		System.out.println(currentFunction.name + " (" + (currentFunction.lineNumber - 1) + ") line=" + line);
		String[] tokens = line.split(" ");
		if (tokens[0].equals("print")) {
			PrintEvent.print(this, line.substring(6));
		} else if (tokens[0].equals("loadvar")) {
			variables.put(tokens[2], GameVariablesForScripts.vars.get(tokens[1]));
			nextLine();
		} else if (tokens[0].equals("call")) {
			callFunction(tokens[1]);
		} else if (tokens[0].equals("yesno")) {
			YesnoEvent.print(this, tokens[1], tokens[2], line.split(tokens[2])[1].substring(1));
		} else if (tokens[0].equals("sflag")) {
			System.out.println("[Setting flag=" + tokens[1] + "]");
			GameVariablesForScripts.setFlag(tokens[1]);
			nextLine();
		} else if (tokens[0].equals("cflag")) {
			boolean flagSet = GameVariablesForScripts.flags.contains(tokens[1]);
			System.out.println("[Flag " + tokens[1] + "=" + (flagSet ? "TRUE]" : "FALSE]"));
			if (flagSet) {
				if (tokens[2].equals("-")) nextLine();
				else callFunction(tokens[2]);
			} else {
				if (tokens[3].equals("-")) nextLine();
				else callFunction(tokens[3]);
			}
		} else if (tokens[0].equals("move")) {
			MoveScript.Move(this, Integer.parseInt(tokens[1]), line.split(tokens[1])[1].substring(1));
		} else if (tokens[0].equals("faceplayer")) {
			ChangeCharacterDirectionScript.facePlayer(Integer.parseInt(tokens[1]));
			nextLine();
		} else if (tokens[0].equals("face")) {
			ChangeCharacterDirectionScript.face(Integer.parseInt(tokens[1]), tokens[2]);
			nextLine();
		} else if (tokens[0].equals("wait")) {
			timer = new Timer(Double.parseDouble(tokens[1]));
		} else if (tokens[0].equals("end")) {
			if (callStack.isEmpty()) {
				Game.gameState = State.NORMAL;
				System.out.println("--------- Script ended ---------\n");
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
		currentFunction = null;
		callFunction("main");
	}

	public void callFunction(String funcName) {
		Function toCall = functions.get(funcName);
		toCall.startFunction();
		if (currentFunction != null) callStack.push(currentFunction);
		currentFunction = toCall;
		nextLine();
	}
}