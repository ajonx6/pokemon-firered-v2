package firered.pokemon.moves.modifiers;

import firered.pokemon.StatusEffect;

public class DamageOnlyWithStatusModifier extends MoveModifier {
	public int damage;
	public StatusEffect type;

	public DamageOnlyWithStatusModifier(int damage, StatusEffect type) {
		super("DamageOnlyWithStatus");
		this.damage = damage;
		this.type = type;
	}
}