package firered.battle;

import firered.pokemon.Pokemon;

public class ChangePokemonHealth extends PostMoveChange {
	// public static final double D_HEALTH_PER_SECOND = 30.0;
	public static final double D_PERCENT_PER_SECOND = 0.2;
	// public static final double TIME_PER_HEALTH = 1.0 / D_HEALTH_PER_SECOND;

	public Pokemon pokemon;
	public int newHealth;
	public double timePerHealth;
	public boolean faint;

	public double timer = 0;

	public ChangePokemonHealth(Pokemon pokemon, int newHealth) {
		this.pokemon = pokemon;
		this.newHealth = newHealth;
		this.timePerHealth = 1.0 / (pokemon.base.maxHP * D_PERCENT_PER_SECOND);
		this.faint = newHealth == 0;
	}

	public void init() {}

	public boolean tick(double delta) {
		timer += delta;
		if (timer >= timePerHealth) {
			pokemon.hp--;
			timer -= timePerHealth;
		}
		return pokemon.hp == newHealth;
	}
	
	public void onExit() {}
}