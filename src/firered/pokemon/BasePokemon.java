package firered.pokemon;

import firered.gfx.sprites.Sprite;

import java.util.HashMap;

public class BasePokemon {
    public static final HashMap<String, BasePokemon> BASE_POKEMON = new HashMap<>();
    
    public String name;
    public Type type1, type2;
    public Sprite frontSprite, backSprite;
    public int maxHP;

    public BasePokemon(String name, Type type1, Type type2, int maxHP) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.frontSprite = new Sprite("pokemon/" + name.toLowerCase() + "_front");
        this.backSprite = new Sprite("pokemon/" + name.toLowerCase() + "_back");
        this.maxHP = maxHP;
        
        BASE_POKEMON.put(name, this);
    }
    
    public static void init() {
        new BasePokemon("Pikachu", Type.ELECTRIC, Type.NULL, 100);
        new BasePokemon("Bulbasaur", Type.GRASS, Type.POISON, 100);
        new BasePokemon("Gastly", Type.GHOST, Type.POISON, 100);
        new BasePokemon("Rattata", Type.NORMAL, Type.NULL, 100);
        new BasePokemon("Pidgey", Type.NORMAL, Type.FLYING, 100);
    }
}
