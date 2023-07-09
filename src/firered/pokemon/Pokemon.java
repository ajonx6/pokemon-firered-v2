package firered.pokemon;

import firered.pokemon.moves.Move;

public class Pokemon {
    public static final int USE_BASE_HP = -1;
    
    public BasePokemon base;
    public String nickname;
    public int level, hp, attack, defence, speed;
    public Move[] moves = new Move[4];
    public int numMoves;
    public StatusEffect statusEffect = StatusEffect.NONE;

    public Pokemon(BasePokemon base, int level, int hp, int attack, int defence, int speed) {
        this(base, base.name, level, hp, attack, defence, speed);
    }

    public Pokemon(BasePokemon base, String nickname, int level, int hp, int attack, int defence, int speed) {
        this.base = base;
        this.nickname = nickname;
        this.level = level;
        this.hp = hp == USE_BASE_HP ? base.maxHP : hp;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
    }
    
    public double getHealthPercentage() {
        return (double) hp / (double) base.maxHP;
    }

    public void addMoves(Move... moves) {
        for (int i = 0; i < moves.length; i++) {
            this.moves[i] = moves[i];
        }
        this.numMoves = moves.length;
    }
}