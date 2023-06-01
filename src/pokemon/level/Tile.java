package pokemon.level;

import pokemon.entity.Character;
import pokemon.entity.Entity;

public abstract class Tile {
    public int tx, ty;

    public Tile(int tx, int ty) {
        this.tx = tx;
        this.ty = ty;
    }
    
    public abstract void action(Character e);
}