package firered.ui.battleui;

import firered.Game;
import firered.battle.Battle;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.pokemon.Pokemon;
import firered.pokemon.moves.Move;
import firered.ui.MyFont;
import firered.ui.Text;
import firered.util.Util;

public class MovePickerUI {
	public static final Sprite MOVE_SELECT_SCREEN = new Sprite("ui/battle/move_select_screen");

	public Pokemon pokemon;
	public Text move1Text, move2Text, move3Text, move4Text;
	public Text ppText;
	public Text moveTypeText;

	public boolean optionLeft = true;
	public boolean optionUp = true;

	public MovePickerUI(Pokemon pokemon) {
		this.pokemon = pokemon;
		this.move1Text = new Text(pokemon.moves[0] == null ? "-" : pokemon.moves[0].name, MyFont.BATTLE_UI_FONT_LIGHT);
		move1Text.setPosition(16, 122);
		this.move2Text = new Text(pokemon.moves[1] == null ? "-" : pokemon.moves[1].name, MyFont.BATTLE_UI_FONT_LIGHT);
		move2Text.setPosition(88, 122);
		this.move3Text = new Text(pokemon.moves[2] == null ? "-" : pokemon.moves[2].name, MyFont.BATTLE_UI_FONT_LIGHT);
		move3Text.setPosition(16, 138);
		this.move4Text = new Text(pokemon.moves[3] == null ? "-" : pokemon.moves[3].name, MyFont.BATTLE_UI_FONT_LIGHT);
		move4Text.setPosition(88, 138);
		this.ppText = new Text(pokemon.moves[0].pp + "/" + pokemon.moves[0].pp, MyFont.BATTLE_UI_FONT_LIGHT);
		ppText.setPosition(231 - ppText.getPixelWidth(), 123);
		this.moveTypeText = new Text(Util.capitaliseFirst(pokemon.moves[0].type.name()), MyFont.BATTLE_UI_FONT_LIGHT);
		moveTypeText.setPosition(192, 139);
	}

	public void tick(double delta) {
		Move selectedMove = null;
		if (optionUp && optionLeft) selectedMove = pokemon.moves[0];
		if (optionUp && !optionLeft) selectedMove = pokemon.moves[1];
		if (!optionUp && optionLeft) selectedMove = pokemon.moves[2];
		if (!optionUp && !optionLeft) selectedMove = pokemon.moves[3];
		
		ppText = new Text(selectedMove.pp + "/" + selectedMove.pp, MyFont.BATTLE_UI_FONT_LIGHT);
		ppText.setPosition(231 - ppText.getPixelWidth(), 123);
		moveTypeText = new Text(Util.capitaliseFirst(selectedMove.type.name()), MyFont.BATTLE_UI_FONT_LIGHT);
		moveTypeText.setPosition(192, 139);
	}
	
	//231, 124
	public void render(Screen screen) {
		screen.prepareRender(0, Game.HEIGHT - MOVE_SELECT_SCREEN.height, MOVE_SELECT_SCREEN, Screen.UI_ELEMENTS);
		move1Text.render(screen);
		move2Text.render(screen);
		move3Text.render(screen);
		move4Text.render(screen);
		ppText.render(screen);
		moveTypeText.render(screen);
		
		int selectArrowX = optionLeft ? 9 : 81;
		int selectArrowY = optionUp ? 124 : 140;
		screen.prepareRender(selectArrowX, selectArrowY, Battle.OPTIONS_SELECT_ARROW, Screen.UI_ELEMENTS);
	}
}