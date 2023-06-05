package pokemon.map.warp;

import pokemon.map.Map;

public class WarpManager {
    // public static HashMap<Warp, Warp> warps = new HashMap<>();
    
    public static void init(Map map) {
        Warp w1 = new Warp(6, 7, map);
        Warp w2 = new Warp(15, 7, map);
        w1.connect(w2);
    }
}
