package firered.pokemon.moves;

public class AccuracyModifier extends MoveModifier {
	public double accuracy;
	
	public AccuracyModifier() {
		this(1.0);
	}

	public AccuracyModifier(double accuracy) {
		this.accuracy = accuracy;
	}
}