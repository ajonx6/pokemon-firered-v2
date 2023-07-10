package firered.ui;

import com.sun.security.jgss.GSSUtil;
import firered.Game;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.scripts.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageBox extends UIBox {
	public static final Sprite UI1 = new Sprite("ui/ui1_full");
	public static final Sprite UI2 = new Sprite("ui/ui2_full");
	public static final Sprite UI3 = new Sprite("ui/ui3_full");
	public static final Sprite UI4 = new Sprite("ui/ui4_full");
	public static final Sprite UI5 = new Sprite("ui/ui5_full");
	public static final Sprite UI6 = new Sprite("ui/ui6_full");
	public static final Sprite UI7 = new Sprite("ui/ui7_full");
	public static final Sprite BATTLE_UI = new Sprite("ui/battle/battle_ui");

	public static final int MESSAGE_BOX_HEIGHT = 48;
	public static final int DEFAULT_PADDING = 0;
	public static final double TIME_PER_CHARACTER = 0.04;

	public MyFont font;
	public String line1 = "", line2 = "";
	public int line1Index = 1, line2Index = 0;
	public Text text1, text2;
	public List<String> textToRender = new ArrayList<>();

	public double timer = 0;
	public int numCharsToRender = 0;
	public boolean renderNextArrow = false;
	public boolean isRenderArrowActive = false;

	public boolean active = false;
	public boolean debugMode = false;
	public Script script;
	public boolean useYesNoBox = false;

	public MessageBox(Sprite ui, MyFont font) {
		this(DEFAULT_PADDING, ui, font);
	}

	public MessageBox(int padding, Sprite ui, MyFont font) {
		super(padding, Game.HEIGHT - MESSAGE_BOX_HEIGHT + padding, ui);
		this.font = font;
		this.text1 = new Text("", 12, 122, font);
		this.text2 = new Text("", 12, 138, font);
	}

	public void attachScript(Script script) {
		this.script = script;
	}

	public void useYesnoBox() {
		this.useYesNoBox = true;
	}

	public void addText(List<String> toAdd) {
		addText(toAdd, true);
	}

	public void addText(List<String> toAdd, boolean useArrow) {
		active = true;
		textToRender.addAll(toAdd);
		isRenderArrowActive = useArrow;
		continueText();
	}

	public void enterDebug() {
		debugMode = true;
		text1.text = "/";
		text2.text = "";
	}

	public void executeDebug() {
		String[] tokens = text1.text.split(" ");
		if (tokens[0].equals("/UI")) {
			int i = Character.getNumericValue(tokens[1].charAt(0));
			if (i == 2) currentUI = UI2;
			else if (i == 3) currentUI = UI3;
			else if (i == 4) currentUI = UI4;
			else if (i == 5) currentUI = UI5;
			else if (i == 6) currentUI = UI6;
			else if (i == 7) currentUI = UI7;
			else {
				currentUI = UI1;
				if (i != 1) {
					text2.text = "Invalid UI number, Only 1-6 allowed";
					return;
				}
			}
			text1.text = "/";
		}
	}

	public void exitDebug() {
		debugMode = false;
		text1.text = "";
		text2.text = "";
	}

	public void continueText() {
		if (textToRender.isEmpty()) {
			active = false;
			if (script != null) {
				script.nextLine();
			}
			return;
		}
		if (text1.text.length() + text2.text.length() != numCharsToRender) return;
		line1 = textToRender.remove(0);
		line2 = textToRender.size() > 0 ? textToRender.remove(0) : "";
		if (line1.equals("") && line2.equals("")) {
			active = false;
			return;
		}
		renderNextArrow = false;
		line1Index = 1;
		line2Index = 0;
		text1.text = line1.equals("") ? "" : line1.substring(0, line1Index);
		text2.text = "";
		numCharsToRender = line1.length() + line2.length();

		timer = 0;
	}

	public void reset() {
		line1 = line2 = "";
		line1Index = 1;
		line2Index = 0;
		text1.text = text2.text = "";
		textToRender.clear();
		timer = numCharsToRender = 0;
		renderNextArrow = false;
		active = false;
		debugMode = false;
	}

	public void tick(double delta) {
		if (!active) return;
		if (!line1.equals("") && line1.length() > text1.text.length()) {
			timer += delta;
			if (timer >= TIME_PER_CHARACTER) {
				timer -= TIME_PER_CHARACTER;
				text1.text = line1.substring(0, ++line1Index);
			}
		} else if (!line2.equals("") && line2.length() > text2.text.length()) {
			timer += delta;
			if (timer >= TIME_PER_CHARACTER) {
				timer -= TIME_PER_CHARACTER;
				text2.text = line2.substring(0, ++line2Index);
			}
		} else {
			if (useYesNoBox && textToRender.isEmpty()) {
				Game.yesnoBox.active = true;
			} else {
				renderNextArrow = true;
			}
		}
	}

	public boolean isDone() {
		return textToRender.isEmpty() && numCharsToRender == text1.text.length() + text2.text.length();
	}

	public boolean stillRendering() {
		return numCharsToRender != text1.text.length() + text2.text.length();
	}

	public void render(Screen screen) {
		if (!active && !debugMode) return;
		super.render(screen);
		text1.render(screen);
		text2.render(screen);
		if (isRenderArrowActive && renderNextArrow)
			screen.prepareRender(225, 141, font.cursorSprite, Screen.UI_ELEMENTS);
	}
}
