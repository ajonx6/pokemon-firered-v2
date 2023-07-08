package firered.pokemon.moves;

import firered.pokemon.StatusEffect;

public class SelfStatusModifier extends MoveModifier {
	public StatusEffect type;

	public SelfStatusModifier(StatusEffect type) {
		this.type = type;
	}
}