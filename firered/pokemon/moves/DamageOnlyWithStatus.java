package firered.pokemon.moves;

import firered.pokemon.StatusEffect;

public class DamageOnlyWithStatus extends MoveModifier {
	public int damage;
	public StatusEffect type;

	public DamageOnlyWithStatus(int damage, StatusEffect type) {
		this.damage = damage;
		this.type = type;
	}
}