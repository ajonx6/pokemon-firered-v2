package firered.pokemon.moves.modifiers;

public class NormalDamageModifier extends MoveModifier {
	public int damage;

	public NormalDamageModifier(int damage) {
		super("NormalDamage");
		this.damage = damage;
	}
}