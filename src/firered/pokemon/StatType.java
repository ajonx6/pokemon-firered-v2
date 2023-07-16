package firered.pokemon;

public enum StatType {
	ATTACK, DEFENCE, SPECIAL_ATTACK, SPECIAL_DEFENCE, SPEED;

	public static StatType getTypeByName(String name) {
		for (StatType t : values()) {
			if (t.name().equalsIgnoreCase(name)) return t;
		}
		return null;
	}
}