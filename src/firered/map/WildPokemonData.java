package firered.map;

import firered.pokemon.BasePokemon;

public class WildPokemonData {
	public BasePokemon pokemon;
	public int minLevel, maxLevel;
	public double probability;

	public WildPokemonData(BasePokemon pokemon, int minLevel, int maxLevel, double probability) {
		this.pokemon = pokemon;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.probability = probability;
	}
}