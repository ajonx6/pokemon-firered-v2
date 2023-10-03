package firered.pokemon.moves.modifiers;

public class PriorityMoveModifier extends MoveModifier {
	public int priority;

	public PriorityMoveModifier(int priority) {
		super("PriorityMove");
		this.priority = priority;
	}
}