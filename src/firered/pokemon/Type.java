package firered.pokemon;

public enum Type {
    NULL, NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DARK, DRAGON, STEEL, FAIRY;

    public static Type getTypeByName(String name) {
        for (Type t : values()) {
            if (t.name().equalsIgnoreCase(name)) return t;
        }
        return NULL;
    }
}