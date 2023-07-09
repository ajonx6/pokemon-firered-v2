package firered.pokemon.moves;

public class NormalDamage extends MoveModifier {
	public int damage;

	public NormalDamage(int damage) {
		super("NormalDamage");
		this.damage = damage;
	}
}