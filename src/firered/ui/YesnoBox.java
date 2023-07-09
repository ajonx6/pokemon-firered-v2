package firered.ui;

import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;

public class YesnoBox extends UIBox {
	public static final Sprite YESNO_BOX = new Sprite("ui/yesno");
	public static final Sprite ARROW = MyFont.DARK_FONT.cursorSprite;

	public static YesnoBox instance;
	public static boolean yesOptionSelected = true;
	
	public YesnoBox() {
		super(161, 65, YESNO_BOX);
	}
	
	public static YesnoBox getInstance() {
		if (instance == null) instance = new YesnoBox();
		return instance;
	}

	public void render(Screen screen) {
		super.render(screen);
		screen.prepareRender(169, yesOptionSelected ? 76 : 90, ARROW, Screen.UI_ELEMENTS);
	}
}