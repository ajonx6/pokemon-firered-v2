package firered.pokemon.moves;

public class PriorityMove extends MoveModifier {
	public int priority;

	public PriorityMove(int priority) {
		super("PriorityMove");
		this.priority = priority;
	}
}