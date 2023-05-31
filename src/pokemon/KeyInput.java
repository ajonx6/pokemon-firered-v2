package pokemon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
	public static final int NUM_KEYS = 256;
	public static final boolean[] keys = new boolean[NUM_KEYS];
	public static final boolean[] lastKeys = new boolean[NUM_KEYS];

	public static final double FACING_THRESHOLD = 5.0;

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public static void tick(double delta) {
		for (int i = 0; i < NUM_KEYS; i++) {
			lastKeys[i] = keys[i];
		}
	}

	public static boolean isDown(int key) {
		return keys[key];
	}

	public static boolean wasPressed(int key) {
		return isDown(key) && !lastKeys[key];
	}

	public static boolean wasReleased(int key) {
		return !isDown(key) && lastKeys[key];
	}
}