package firered.pokemon.moves;

import firered.pokemon.StatType;

public class SelfStatValueModifier extends MoveModifier {
	public StatType type;
	public int dx;
	public double prob;

	public SelfStatValueModifier(StatType type, int dx) {this(type, dx, 1.0);}

	public SelfStatValueModifier(StatType type, int dx, double prob) {
		super("SelfStatValueModifier");
		this.type = type;
		this.dx = dx;
		this.prob = prob;
	}
}