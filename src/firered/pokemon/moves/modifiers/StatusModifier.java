package firered.pokemon.moves.modifiers;

import firered.pokemon.StatusEffect;
import firered.pokemon.moves.modifiers.MoveModifier;

public class StatusModifier extends MoveModifier {
	public StatusEffect type;
	public double prob;

	public StatusModifier(StatusEffect type) {
		this(type, 1.0);
	}

	public StatusModifier(StatusEffect type, double prob) {
		super("StatusModifier");
		this.type = type;
		this.prob = prob;
	}
}