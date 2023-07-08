package firered.pokemon;

import firered.gfx.sprites.Sprite;

public class BasePokemon {
    public static final BasePokemon BASE_PIKACHU = new BasePokemon("Pikachu", Type.ELECTRIC, Type.NULL, 100);
    public static final BasePokemon BASE_BULBASAUR = new BasePokemon("Bulbasaur", Type.GRASS, Type.POISON, 100);
    public static final BasePokemon BASE_GASTLY = new BasePokemon("Gastly", Type.GHOST, Type.POISON, 100);
    
    public String name;
    public Type type1, type2;
    public Sprite frontSprite, backSprite;
    public int health;

    public BasePokemon(String name, Type type1, Type type2, int health) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.frontSprite = new Sprite("pokemon/" + name.toLowerCase() + "_front");
        this.backSprite = new Sprite("pokemon/" + name.toLowerCase() + "_back");
        this.health = health;
    }
}
