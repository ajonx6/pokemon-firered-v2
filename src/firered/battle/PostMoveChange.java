package firered.battle;

public abstract class PostMoveChange {
	public abstract void init();
	
	public abstract boolean tick(double delta);

	public abstract void onExit();
}