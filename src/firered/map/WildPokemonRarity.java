package firered.map;

public enum WildPokemonRarity {
	NONE("n", 0), VERY_RARE("vr", 1.25), RARE("r", 3.33), SEMI_RARE("sr", 6.75), COMMON("c", 8.5), VERY_COMMON("vc", 10);

	private String symbol;
	private double scale;

	WildPokemonRarity(String symbol, double scale) {
		this.symbol = symbol;
		this.scale = scale;
	}

	public double getScale() {
		return scale;
	}
	
	public static WildPokemonRarity getRarityBySymbol(String s) {
		for (WildPokemonRarity wpr : values()) {
			if (wpr.symbol.equals(s)) return wpr;
		}
		return null;
	}
}