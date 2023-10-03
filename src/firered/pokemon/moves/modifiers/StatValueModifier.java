package firered.pokemon.moves.modifiers;

import firered.pokemon.StatType;
import firered.pokemon.moves.modifiers.MoveModifier;

public class StatValueModifier extends MoveModifier {
	public StatType type;
	public int dx;
	public double prob;

	public StatValueModifier(StatType type, int dx) {
		this(type, dx, 1.0);
	}

	public StatValueModifier(StatType type, int dx, double prob) {
		super("StatValueModifier");
		this.type = type;
		this.dx = dx;
		this.prob = prob;
	}
}