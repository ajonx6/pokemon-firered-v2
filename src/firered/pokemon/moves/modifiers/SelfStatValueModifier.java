package firered.pokemon.moves.modifiers;

import firered.pokemon.StatType;
import firered.pokemon.moves.modifiers.MoveModifier;

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