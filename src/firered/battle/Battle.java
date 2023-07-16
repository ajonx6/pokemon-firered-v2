package firered.battle;

import firered.Game;
import firered.KeyInput;
import firered.State;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.pokemon.Pokemon;
import firered.pokemon.moves.Move;
import firered.ui.MessageBox;
import firered.ui.MyFont;
import firered.ui.battleui.HealthUI;
import firered.ui.battleui.MovePickerUI;
import firered.util.Util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {
	public static final Random RANDOM = new Random();
	public static final Sprite BACKGROUND = new Sprite("ui/battle/background");
	public static final Sprite BATTLE_OPTIONS_UI = new Sprite("ui/battle/battle_options_ui");
	public static final Sprite OPTIONS_SELECT_ARROW = new Sprite("ui/battle/options_select_arrow");

	public Pokemon playerPokemon, opPokemon;
	public HealthUI playerHealthUI, opHealthUI;

	public boolean optionLeft = true;
	public boolean optionUp = true;
	public BattleState battleState;

	public boolean hasMovedThisTurn = false;
	public Move playerMove = null;
	public Move opponentMove = null;
	public List<PostMoveChange> changes = new ArrayList<>();
	public PostMoveChange currentChange = null;

	public static MessageBox messageBox = new MessageBox(MessageBox.BATTLE_UI, MyFont.LIGHT_FONT);
	public MovePickerUI movePickerUI;

	public void startBattle(Pokemon playerPokemon, Pokemon opPokemon) {
		this.playerPokemon = playerPokemon;
		this.opPokemon = opPokemon;
		Game.gameState = State.BATTLE;
		init();
	}
	
	public void init() {
		this.playerHealthUI = new HealthUI(true, playerPokemon);
		this.opHealthUI = new HealthUI(false, opPokemon);
		messageBox.reset();
		messageBox.addText(Util.scriptToMessages("%dWhat will%n" + playerPokemon.nickname + " do?"));
		optionLeft = optionUp = true;
		movePickerUI = new MovePickerUI();
		battleState = BattleState.WAITING_FOR_INPUT;
		changes.clear();
		currentChange = null;
		playerMove = opponentMove = null;
		hasMovedThisTurn = false;

		playerPokemon.hp = playerPokemon.base.baseStats.hp;
		opPokemon.hp = opPokemon.base.baseStats.hp;
	}

	public void select() {
		if (battleState == BattleState.WAITING_FOR_INPUT) {
			if (optionLeft && optionUp) {
				battleState = BattleState.SELECTING_MOVE;
				movePickerUI.init(playerPokemon);
			} else if (!optionLeft && optionUp) {

			} else if (optionLeft && !optionUp) {

			} else if (!optionLeft && !optionUp) {
				Game.gameState = State.NORMAL;
			}
		} else if (battleState == BattleState.SELECTING_MOVE) {
			boolean playerMovesFirst = playerPokemon.speed >= opPokemon.speed;
			playerMove = getPlayerMove();
			opponentMove = getOpponentMove();
			messageBox.reset();
			hasMovedThisTurn = false;
			if (playerMovesFirst) {
				battleState = BattleState.OPPONENT_MOVE_TEXT;
				messageBox.addText(Util.scriptToMessages("Foe " + opPokemon.nickname + " used%n" + opponentMove.name + "."), false);
			} else {
				battleState = BattleState.PLAYER_MOVE_TEXT;
				messageBox.addText(Util.scriptToMessages(playerPokemon.nickname + " used%n" + playerMove.name + "."), false);
			}
		}
	}

	public void back() {
		if (battleState == BattleState.SELECTING_MOVE) battleState = BattleState.WAITING_FOR_INPUT;
	}

	public void tick(double delta) {
		playerHealthUI.tick(delta);
		opHealthUI.tick(delta);
		
		if (battleState == BattleState.WAITING_FOR_INPUT) {
			if (KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_DOWN))
				optionUp = !optionUp;
			if (KeyInput.wasPressed(KeyEvent.VK_LEFT) || KeyInput.wasPressed(KeyEvent.VK_RIGHT))
				optionLeft = !optionLeft;
			messageBox.tick(delta);
		} else if (battleState == BattleState.SELECTING_MOVE) {
			if (KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_DOWN))
				movePickerUI.optionUp = !movePickerUI.optionUp;
			if (KeyInput.wasPressed(KeyEvent.VK_LEFT) || KeyInput.wasPressed(KeyEvent.VK_RIGHT))
				movePickerUI.optionLeft = !movePickerUI.optionLeft;
			movePickerUI.tick(delta);
		} else if (battleState == BattleState.PLAYER_MOVE_TEXT) {
			if (messageBox.isDone()) {
				battleState = BattleState.PLAYER_MOVE;
			}
			messageBox.tick(delta);
		} else if (battleState == BattleState.PLAYER_MOVE) {
			carryingOutMove(playerPokemon, opPokemon, playerMove, delta);
		} else if (battleState == BattleState.PLAYER_FAINT) {
			if (messageBox.isDone() && KeyInput.wasPressed(KeyEvent.VK_X)) {
				Game.gameState = State.NORMAL;
			}
			messageBox.tick(delta);
		} else if (battleState == BattleState.OPPONENT_MOVE_TEXT) {
			if (messageBox.isDone()) {
				battleState = BattleState.OPPONENT_MOVE;
			}
			messageBox.tick(delta);
		} else if (battleState == BattleState.OPPONENT_MOVE) {
			carryingOutMove(opPokemon, playerPokemon, opponentMove, delta);
		} else if (battleState == BattleState.OPPONENT_FAINT) {
			if (messageBox.isDone() && KeyInput.wasPressed(KeyEvent.VK_X)) {
				Game.gameState = State.NORMAL;
			}
			messageBox.tick(delta);
		}
	}

	public void carryingOutMove(Pokemon user, Pokemon defender, Move move, double delta) {
		if (changes.isEmpty()) {
			changes.addAll(move.useMove(user, defender));
			currentChange = changes.remove(0);
		}
		if (currentChange.tick(delta)) {
			if (currentChange instanceof ChangePokemonHealth) {
				ChangePokemonHealth cph = (ChangePokemonHealth) currentChange;
				if (cph.faint) {
					if (battleState == BattleState.OPPONENT_MOVE) {
						battleState = BattleState.PLAYER_FAINT;
						messageBox.addText(Util.scriptToMessages(playerPokemon.nickname + "%nhas fainted..."));
					}
					if (battleState == BattleState.PLAYER_MOVE) {
						battleState = BattleState.OPPONENT_FAINT;
						messageBox.addText(Util.scriptToMessages("Foe " + opPokemon.nickname + "%nhas fainted..."));
					}
					return;
				}
			}
			currentChange.onExit();
			currentChange = changes.remove(0);
			currentChange.init();
		}

		if (currentChange instanceof MoveComplete) {
			if (!hasMovedThisTurn) {
				if (battleState == BattleState.OPPONENT_MOVE) {
					battleState = BattleState.PLAYER_MOVE_TEXT;
					messageBox.addText(Util.scriptToMessages(playerPokemon.nickname + " used%n" + playerMove.name + "."), false);
				} else if (battleState == BattleState.PLAYER_MOVE) {
					battleState = BattleState.PLAYER_MOVE_TEXT;
					messageBox.addText(Util.scriptToMessages("Foe " + opPokemon.nickname + " used%n" + opponentMove.name + "."), false);
				}
				hasMovedThisTurn = true;
			} else {
				battleState = BattleState.WAITING_FOR_INPUT;
				messageBox.reset();
				messageBox.addText(Util.scriptToMessages("%dWhat will%n" + playerPokemon.nickname + " do?"));
			}
			changes.clear();
		}
	}

	public void render(Screen screen) {
		screen.prepareRender(0, 0, BACKGROUND, Screen.UI_ELEMENTS);
		screen.prepareRender(64 - playerPokemon.base.backSprite.width / 2, 112 - playerPokemon.base.backSprite.height, playerPokemon.base.backSprite, Screen.UI_ELEMENTS);
		screen.prepareRender(176 - opPokemon.base.frontSprite.width / 2, 63 - 0.75 * opPokemon.base.frontSprite.height, opPokemon.base.frontSprite, Screen.UI_ELEMENTS);
		playerHealthUI.render(screen);
		opHealthUI.render(screen);

		messageBox.render(screen);
		
		if (battleState == BattleState.WAITING_FOR_INPUT) {
			screen.prepareRender(120, 112, BATTLE_OPTIONS_UI, Screen.UI_ELEMENTS);
			int selectArrowX = optionLeft ? 129 : 185;
			int selectArrowY = optionUp ? 124 : 140;
			screen.prepareRender(selectArrowX, selectArrowY, OPTIONS_SELECT_ARROW, Screen.UI_ELEMENTS);
		} else if (battleState == BattleState.SELECTING_MOVE) {
			movePickerUI.render(screen);
		}
	}

	public Move getPlayerMove() {
		return movePickerUI.getMove(playerPokemon);
	}

	public Move getOpponentMove() {
		Move m = opPokemon.moves[RANDOM.nextInt(opPokemon.numMoves)];
		return m;
	}
}