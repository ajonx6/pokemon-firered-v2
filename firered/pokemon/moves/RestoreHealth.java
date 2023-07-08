package firered.pokemon.moves;

public class RestoreHealth extends MoveModifier {
	public double proportion;
	public boolean basedOnDamage;

	public RestoreHealth(double proportion, boolean basedOnDamage) {
		this.proportion = proportion;
		this.basedOnDamage = basedOnDamage;
	}
}