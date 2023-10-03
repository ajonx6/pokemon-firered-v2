package firered.pokemon.moves.modifiers;

public class FlinchModifier extends MoveModifier {
	public double flinchChance;
	
	public FlinchModifier() {
		this(1.0);
	}

	public FlinchModifier(double flinchChance) {
		super("FlinchModifier");
		this.flinchChance = flinchChance;
	}
}