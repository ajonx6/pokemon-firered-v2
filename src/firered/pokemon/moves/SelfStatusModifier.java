package firered.pokemon.moves;

import firered.pokemon.StatusEffect;

public class SelfStatusModifier extends MoveModifier {
	public StatusEffect type;

	public SelfStatusModifier(StatusEffect type) {
		super("SelfStatusModifier");
		this.type = type;
	}
}