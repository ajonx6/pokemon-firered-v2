package firered.pokemon.moves;

public enum MoveType {
	PHYSICAL, SPECIAL, STATUS;
	
	public static MoveType getMoveTypeByName(String name) {
		for (MoveType mt : values()) {
			if (mt.name().equalsIgnoreCase(name)) return mt;
		}
		return null;
	}
}