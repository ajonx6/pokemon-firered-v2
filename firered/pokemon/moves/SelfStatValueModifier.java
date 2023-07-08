package firered.pokemon.moves;

import firered.pokemon.StatType;

public class SelfStatValueModifier extends MoveModifier {
	public StatType type;
	public int dx;

	public SelfStatValueModifier(StatType type, int dx) {
		this.type = type;
		this.dx = dx;
	}
}