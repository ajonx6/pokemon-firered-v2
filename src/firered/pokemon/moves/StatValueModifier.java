package firered.pokemon.moves;

import firered.pokemon.StatType;

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