package firered.pokemon.moves.modifiers;

public class RestoreHealthModifier extends MoveModifier {
	public double proportion;
	public boolean basedOnDamage;

	public RestoreHealthModifier(double proportion, boolean basedOnDamage) {
		super("RestoreHealth");
		this.proportion = proportion;
		this.basedOnDamage = basedOnDamage;
	}
}