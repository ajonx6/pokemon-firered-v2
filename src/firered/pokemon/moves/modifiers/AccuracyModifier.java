package firered.pokemon.moves.modifiers;

public class AccuracyModifier extends MoveModifier {
	public double accuracy;
	
	public AccuracyModifier() {
		this(1.0);
	}

	public AccuracyModifier(double accuracy) {
		super("AccuracyModifier");
		this.accuracy = accuracy;
	}
}