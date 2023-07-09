package firered.map;

import firered.entity.Character;

public abstract class Tile {
    public int tx, ty;

    public Tile(int tx, int ty) {
        this.tx = tx;
        this.ty = ty;
    }
    
    public abstract void action(Character e);
}