package firered.gfx;

import firered.gfx.sprites.Sprite;

public class Transition {
	public static final double TRANSITION_TIME = 1.4;
	public static final Sprite TRIANGLE_SPRITE = new Sprite("ui/transition/triangles");
	public static final Sprite CIRCLE_SPRITE = new Sprite("ui/transition/circle");
	public static final Sprite SPIRAL_SPRITE = new Sprite("ui/transition/spiral");
	public static final Sprite GRADIENT_SPRITE = new Sprite("ui/transition/gradient");

	public static Sprite sprite;
	public static double time = 0;
	public static boolean active = false;
	public static boolean reverse = false;
	public static boolean isReverse = false;

	public static void startTransition(Sprite sprite, boolean r) {
		Transition.sprite = sprite;
		time = 0;
		active = true;
		reverse = r;
	}

	public static void tick(double delta) {
		if (!active) return;
		
		if (!isReverse) time += delta;
		else time -= delta;
		
		if (time > TRANSITION_TIME) {
			if (reverse) isReverse = true;
			else active = false;
		}
		
		if (time < 0) {
			isReverse = false;
			active = false;
		}
	}

	public static void render(Screen screen) {
		if (!active) return;
		double percentage = percentage();
		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				int cutoff = (int) (percentage * 255.0);
				int transColour = sprite.pixels[x + y * sprite.width];
				int colour = (transColour & 0xff);
				if (colour < cutoff) {
					screen.pixels[x + y * screen.getWidth()] = 0xff000000;
				}
			}
		}
	}

	public static double percentage() {
		return time / TRANSITION_TIME;
	}
}