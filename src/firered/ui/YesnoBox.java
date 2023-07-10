package firered.ui;

import firered.Game;
import firered.KeyInput;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.scripts.Script;

import java.awt.event.KeyEvent;

public class YesnoBox extends UIBox {
	public static final Sprite YESNO_BOX = new Sprite("ui/yesno");
	public static final Sprite ARROW = MyFont.DARK_FONT.cursorSprite;

	public boolean yesOptionSelected = true;
	public boolean active = false;
	public Script script;
	public String yesFuncName, noFuncName;

	public YesnoBox() {
		super(161, 65, YESNO_BOX);
	}

	public void tick(double delta) {
		if (!active) return;
		if (KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_DOWN))
			yesOptionSelected = !yesOptionSelected;
		if (KeyInput.wasPressed(KeyEvent.VK_X)) {
			active = false;
			Game.messageBox.active = false;
			Game.messageBox.useYesNoBox = false;
			if (yesOptionSelected) script.callFunction(yesFuncName);
			else script.callFunction(noFuncName);
			yesOptionSelected = true;
		}
	}

	public void render(Screen screen) {
		if (!active) return;
		super.render(screen);
		screen.prepareRender(169, yesOptionSelected ? 76 : 90, ARROW, Screen.UI_ELEMENTS);
	}

	public void setFunctions(Script script, String yesFuncName, String noFuncName) {
		this.script = script;
		this.yesFuncName = yesFuncName;
		this.noFuncName = noFuncName;
	}
}