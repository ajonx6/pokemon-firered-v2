package pokemon;

import pokemon.entity.Entity;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = (int) (240 * Settings.SCALE);
	public static final int HEIGHT = (int) (160 * Settings.SCALE);
	public static final String TITLE = "2D Game";
	public static final double FPS = 300.0;

	private static Game instance;

	public BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public JFrame frame;
	public boolean running;

	public Screen screen;
	public Entity player;
	public Sprite grass1;
	public Sprite grass2;
	public Sprite grass3;
	public Sprite grass4;

	private Game() {
		screen = new Screen(WIDTH, HEIGHT);
		player = new Entity(Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE, new Sprite("entities/player_sprite"));
		grass1 = new Sprite("tiles/grass1");
		grass2 = new Sprite("tiles/grass2");
		grass3 = new Sprite("tiles/grass3");
		grass4 = new Sprite("tiles/water");
		
		addKeyListener(new KeyInput());
	}

	// Returns the instance of this class so other classes can access these variables
	public static Game getInstance() {
		if (instance == null) instance = new Game();
		return instance;
	}

	// Stops the current thread and therefore ends the program
	public void stop() {
		if (!running) return;
		running = false;
	}

	// Runs the program
	public void run() {
		running = true;
		requestFocus();

		int frames = 0, ticks = 0;
		long frameCounter = 0;
		double frameTime = 1.0 / FPS;
		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (running) {
			boolean render = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;
				Time.setDelta(frameTime);
				tick();
				ticks++;
				if (frameCounter >= Time.SECOND) {
					frame.setTitle(TITLE + " | FPS: " + frames + ", UPS: " + ticks);
					frames = 0;
					ticks = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	public void tick() {
		double delta = Time.getFrameTimeInSeconds();
		
		if (KeyInput.wasPressed(KeyEvent.VK_ESCAPE)) System.exit(0);
		if (KeyInput.wasPressed(KeyEvent.VK_UP)) player.move(0, -Settings.SCALED_TILE_SIZE);
		if (KeyInput.wasPressed(KeyEvent.VK_DOWN)) player.move(0, Settings.SCALED_TILE_SIZE);
		if (KeyInput.wasPressed(KeyEvent.VK_LEFT)) player.move(-Settings.SCALED_TILE_SIZE, 0);
		if (KeyInput.wasPressed(KeyEvent.VK_RIGHT)) player.move(Settings.SCALED_TILE_SIZE, 0);
		
		KeyInput.tick(delta);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		screen.clear(0xffaaaaaa);

		screen.render(0, 0, grass1);
		screen.render(Settings.SCALED_TILE_SIZE, 0, grass2);
		screen.render(0, Settings.SCALED_TILE_SIZE, grass3);
		screen.render(Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE, grass4);
		player.render(screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
	}
}