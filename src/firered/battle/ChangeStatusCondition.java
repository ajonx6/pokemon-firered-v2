package firered.battle;

import firered.KeyInput;
import firered.pokemon.Pokemon;
import firered.pokemon.StatType;
import firered.pokemon.StatusEffect;
import firered.util.Util;

import java.awt.event.KeyEvent;

public class ChangeStatusCondition extends PostMoveChange {
	public Pokemon pokemon;
	public StatusEffect status;

	public ChangeStatusCondition(Pokemon pokemon, StatusEffect status) {
		this.pokemon = pokemon;
		this.status = status;
	}

	public void init() {
		Battle.messageBox.addText(Util.scriptToMessages(pokemon.nickname + "%n" + status.text + "."));
		pokemon.statusEffect = status;
	}

	public boolean tick(double delta) {
		Battle.messageBox.tick(delta);
		return Battle.messageBox.isDone() && KeyInput.wasPressed(KeyEvent.VK_X);
	}

	public void onExit() {
		Battle.messageBox.continueText();
	}
}