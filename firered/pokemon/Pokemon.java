package firered.pokemon;

import firered.pokemon.moves.Move;

public class Pokemon {
    public BasePokemon base;
    public String nickname;
    public int level, hp, attack, defence;
    public Move[] moves = new Move[4];

    public Pokemon(BasePokemon base, int level, int hp, int attack, int defence) {
        this(base, base.name, level, hp, attack, defence);
    }

    public Pokemon(BasePokemon base, String nickname, int level, int hp, int attack, int defence) {
        this.base = base;
        this.nickname = nickname;
        this.level = level;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
    }
    
    public double getHealthPercentage() {
        return (double) hp / (double) base.health;
    }

    public void addMoves(Move... moves) {
        for (int i = 0; i < moves.length; i++) {
            this.moves[i] = moves[i];
        }
    }
}