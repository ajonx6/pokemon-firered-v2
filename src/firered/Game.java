package firered;

import firered.battle.Battle;
import firered.entity.Player;
import firered.gfx.Screen;
import firered.gfx.SpriteList;
import firered.gfx.Transition;
import firered.map.Map;
import firered.map.MapManager;
import firered.pokemon.BasePokemon;
import firered.pokemon.Pokemon;
import firered.pokemon.StatusEffect;
import firered.pokemon.moves.Move;
import firered.scripts.Script;
import firered.scripts.GameVariablesForScripts;
import firered.ui.*;
import firered.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;

public class Game extends Canvas implements Runnable {
	@Serial
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int WINDOW_WIDTH = WIDTH * Settings.SCALE;
	public static final int WINDOW_HEIGHT = HEIGHT * Settings.SCALE;
	public static final String TITLE = "2D Game";
	public static final double FPS = 300.0;

	public static boolean debug = false;
	private static Game instance;

	public BufferedImage image = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public JFrame frame;
	public boolean running;

	public static Screen screen;
	public static Player player;
	public static MessageBox messageBox;
	public static YesnoBox yesnoBox;
	public static State gameState = State.NORMAL;

	public static Pokemon p1;
	public static Pokemon p2;
	public static Pokemon p3;
	public static Battle battle = new Battle();
	
	private Game() {
		SpriteList.init();
		Loader.loadMoves();
		Loader.loadBasePokemon();
		Loader.loadObjects();
		Loader.loadMaps();
		GameVariablesForScripts.init();

		screen = new Screen(WIDTH, HEIGHT);

		// MapManager.loadMap(Map.MAPS_MAP.get("route1"));
		MapManager.loadMap(Map.MAPS_MAP.get("pallet_town"));
		player = new Player(0, 4, 4, Util.charsToSprites("player", "down", "up", "left", "right"));

		messageBox = new MessageBox(MessageBox.UI1, MyFont.DARK_FONT);
		yesnoBox = new YesnoBox();

		p1 = new Pokemon(BasePokemon.BASE_POKEMON.get("Pikachu"), 100, Pokemon.USE_BASE_HP, 10, 2, 10);
		p1.addMoves(Move.MOVES.get("thunder-shock"), Move.MOVES.get("growl"), Move.MOVES.get("quick-attack"), Move.MOVES.get("thunder-wave"));
		p2 = new Pokemon(BasePokemon.BASE_POKEMON.get("Gastly"), 100, Pokemon.USE_BASE_HP, 4, 3, 12);
		p2.addMoves(Move.MOVES.get("hypnosis"), Move.MOVES.get("lick"), Move.MOVES.get("dream-eater"), Move.MOVES.get("shadow-ball"));
		p3 = new Pokemon(BasePokemon.BASE_POKEMON.get("Bulbasaur"), 100, Pokemon.USE_BASE_HP, 5, 5, 10);
		p3.addMoves(Move.MOVES.get("tackle"), Move.MOVES.get("vine-whip"), Move.MOVES.get("razor-leaf"), Move.MOVES.get("growth"));

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
					// System.out.println("FPS: " + frames + ", UPS: " + ticks);
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
		
		switch (gameState) {
			case NORMAL -> {
				if (KeyInput.wasPressed(KeyEvent.VK_SLASH)) {
					messageBox.enterDebug();
					gameState = State.COMMAND;
					break;
				}
				
				if (KeyInput.wasPressed(KeyEvent.VK_B)) {
					battle.startBattle(p3, p1);
					gameState = State.BATTLE;
				}

				if (KeyInput.isDown(KeyEvent.VK_UP)) player.move(0, -1);
				if (KeyInput.isDown(KeyEvent.VK_DOWN)) player.move(0, 1);
				if (KeyInput.isDown(KeyEvent.VK_LEFT)) player.move(-1, 0);
				if (KeyInput.isDown(KeyEvent.VK_RIGHT)) player.move(1, 0);
				
				if (KeyInput.wasPressed(KeyEvent.VK_X) && !player.currentlyMoving) {
					Script s = MapManager.scriptUnder(player.getTilePos().intX() + player.facing.dx, player.getTilePos().intY() + player.facing.dy);
					Script e = MapManager.entityHasScript(player.getTilePos().intX() + player.facing.dx, player.getTilePos().intY() + player.facing.dy);
					if (e != null) e.startScript();
					else if (s != null) s.startScript();
				}
				player.tick(delta);
			}
			case MESSAGE_BOX -> {
				if (messageBox.active && !messageBox.stillRendering() && KeyInput.wasPressed(KeyEvent.VK_X) && !yesnoBox.active) {
					messageBox.continueText();
				}
				player.tick(delta);
			}
			case COMMAND -> {
				for (int i = 'A'; i <= 'Z'; i++) {
					if (KeyInput.wasPressed(i)) {
						messageBox.text1.text += (char) i;
						messageBox.text2.text = "";
					}
				}
				if (KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
					messageBox.text1.text += " ";
					messageBox.text2.text = "";
				}
				for (int i = '0'; i <= '9'; i++) {
					if (KeyInput.wasPressed(i)) {
						messageBox.text1.text += (char) i;
						messageBox.text2.text = "";
					}
				}
				if (KeyInput.wasPressed(KeyEvent.VK_BACK_SPACE)) {
					messageBox.text1.text = messageBox.text1.text.length() == 1 ? messageBox.text1.text : messageBox.text1.text.substring(0, messageBox.text1.text.length() - 1);
					messageBox.text2.text = "";
				}
				if (KeyInput.wasPressed(KeyEvent.VK_ENTER)) messageBox.executeDebug();
				if (KeyInput.wasPressed(KeyEvent.VK_C)) messageBox.continueText();
				if (KeyInput.wasPressed(KeyEvent.VK_P)) {
					messageBox.exitDebug();
					gameState = State.NORMAL;
				}
			}
			case BATTLE -> {
				if (KeyInput.wasPressed(KeyEvent.VK_B)) gameState = State.NORMAL;
				if (KeyInput.wasPressed(KeyEvent.VK_X)) battle.select();
				if (KeyInput.wasPressed(KeyEvent.VK_Z)) battle.back();
				battle.tick(delta);
			}
		}

		Transition.tick(delta);
		messageBox.tick(delta);
		yesnoBox.tick(delta);
		MapManager.tick(delta);
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

		if (gameState == State.NORMAL || gameState == State.COMMAND || gameState == State.MESSAGE_BOX) {
			MapManager.render(screen);
			player.render(screen);
			messageBox.render(screen);
			yesnoBox.render(screen);
		} else if (gameState == State.BATTLE) {
			battle.render(screen);
		}

		screen.renderAll();
		Transition.render(screen);
		screen.scaleAndSetPixels(pixels);

		g.drawImage(image, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
		g.dispose();
		bs.show();
	}
}