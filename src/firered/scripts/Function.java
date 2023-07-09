package firered.scripts;

import java.util.ArrayList;
import java.util.List;

public class Function {
	public String name;
	public List<String> lines = new ArrayList<>();

	public int lineNumber = 0;

	public Function(String name) {
		this.name = name;
	}

	public void addLine(String line) {
		this.lines.add(line);
	}

	public void startFunction() {
		lineNumber = 0;
	}

	public String getLine() {
		return lines.get(lineNumber++);
	}
}