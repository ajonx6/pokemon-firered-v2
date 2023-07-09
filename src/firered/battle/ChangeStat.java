package firered.battle;

import firered.KeyInput;
import firered.pokemon.Pokemon;
import firered.pokemon.StatType;
import firered.util.Util;

import java.awt.event.KeyEvent;

public class ChangeStat extends PostMoveChange {
	public Pokemon pokemon;
	public StatType stat;
	public int d;

	public ChangeStat(Pokemon pokemon, StatType stat, int d) {
		this.pokemon = pokemon;
		this.stat = stat;
		this.d = d;
	}

	public void init() {
		String extra = d == 1 ? " rose." : d == 2 ? " rose sharply." : d == -1 ? " fell." : d == -2 ? " fell sharply." : " error";
		Battle.messageBox.addText(Util.scriptToMessages(pokemon.nickname + "'s%n" + stat.name() + extra));
	}

	public boolean tick(double delta) {
		Battle.messageBox.tick(delta);
		return Battle.messageBox.isDone() && KeyInput.wasPressed(KeyEvent.VK_X);
	}
	
	public void onExit() {
		Battle.messageBox.continueText();
	}
}