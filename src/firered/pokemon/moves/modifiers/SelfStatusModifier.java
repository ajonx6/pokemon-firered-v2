package firered.pokemon.moves.modifiers;

import firered.pokemon.StatusEffect;
import firered.pokemon.moves.modifiers.MoveModifier;

public class SelfStatusModifier extends MoveModifier {
	public StatusEffect type;

	public SelfStatusModifier(StatusEffect type) {
		super("SelfStatusModifier");
		this.type = type;
	}
}