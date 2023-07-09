package firered.battle;

import firered.pokemon.Pokemon;

public class WaitForAnimation extends PostMoveChange {
	public double timeToWait;

	public double timer = 0;

	public WaitForAnimation(double timeToWait) {
		this.timeToWait = timeToWait;
	}

	public void init() {}

	public boolean tick(double delta) {
		timer += delta;
		return timer >= timeToWait;
	}

	public void onExit() {}
}