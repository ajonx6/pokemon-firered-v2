package firered.battle;

import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.pokemon.Pokemon;
import firered.ui.MessageBox;
import firered.ui.MyFont;
import firered.ui.battleui.HealthUI;
import firered.ui.battleui.MovePickerUI;
import firered.util.Util;

public class Battle {
	public static final Sprite BACKGROUND = new Sprite("ui/battle/background");
	public static final Sprite BATTLE_OPTIONS_UI = new Sprite("ui/battle/battle_options_ui");
	public static final Sprite OPTIONS_SELECT_ARROW = new Sprite("ui/battle/options_select_arrow");

	public Pokemon playerPokemon, opPokemon;
	public HealthUI playerHealthUI, opHealthUI;

	public boolean optionLeft = true;
	public boolean optionUp = true;
	public BattleState battleState;

	public MessageBox messageBox = new MessageBox(MessageBox.BATTLE_UI, MyFont.LIGHT_FONT);
	public MovePickerUI movePickerUI;

	public Battle(Pokemon playerPokemon, Pokemon opPokemon) {
		this.playerPokemon = playerPokemon;
		this.opPokemon = opPokemon;

		init();
	}

	public void init() {
		this.playerHealthUI = new HealthUI(true, playerPokemon);
		this.opHealthUI = new HealthUI(false, opPokemon);
		messageBox.reset();
		messageBox.addText(Util.scriptToMessages("What will%n" + playerPokemon.nickname + " do?"));
		optionLeft = optionUp = true;
		movePickerUI = new MovePickerUI(playerPokemon);
		battleState = BattleState.WAITING_FOR_INPUT;
	}

	public void select() {
		if (battleState == BattleState.WAITING_FOR_INPUT) {
			if (optionLeft && optionUp) {
				battleState = BattleState.SELECTING_MOVE;
			} else if (!optionLeft && optionUp) {

			} else if (optionLeft && !optionUp) {

			} else if (!optionLeft && !optionUp) {

			}
		} else if (battleState == BattleState.WAITING_FOR_INPUT) {
			
		}
	}

	public void back() {
		if (battleState == BattleState.SELECTING_MOVE) battleState = BattleState.WAITING_FOR_INPUT;
	}

	public void tick(double delta) {
		messageBox.tick(delta);
		movePickerUI.tick(delta);
	}

	public void render(Screen screen) {
		screen.prepareRender(0, 0, BACKGROUND, Screen.UI_ELEMENTS);
		screen.prepareRender(64 - playerPokemon.base.backSprite.width / 2, 112 - playerPokemon.base.backSprite.height, playerPokemon.base.backSprite, Screen.UI_ELEMENTS);
		screen.prepareRender(176 - opPokemon.base.frontSprite.width / 2, 63 - 0.75 * opPokemon.base.frontSprite.height, opPokemon.base.frontSprite, Screen.UI_ELEMENTS);
		playerHealthUI.render(screen);
		opHealthUI.render(screen);

		if (battleState == BattleState.WAITING_FOR_INPUT) {
			messageBox.render(screen);
			screen.prepareRender(120, 112, BATTLE_OPTIONS_UI, Screen.UI_ELEMENTS);
			int selectArrowX = optionLeft ? 129 : 185;
			int selectArrowY = optionUp ? 124 : 140;
			screen.prepareRender(selectArrowX, selectArrowY, OPTIONS_SELECT_ARROW, Screen.UI_ELEMENTS);
		} else if (battleState == BattleState.SELECTING_MOVE) {
			movePickerUI.render(screen);
		}
	}
}