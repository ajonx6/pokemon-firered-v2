package firered;

import firered.battle.Battle;
import firered.entity.Character;
import firered.entity.Player;
import firered.gfx.Screen;
import firered.gfx.SpriteList;
import firered.gfx.sprites.Sprite;
import firered.map.Map;
import firered.map.MapManager;
import firered.map.warp.WarpManager;
import firered.pokemon.BasePokemon;
import firered.pokemon.Pokemon;
import firered.pokemon.StatusEffect;
import firered.pokemon.moves.Move;
import firered.scripts.Script;
import firered.scripts.GameVariablesForScripts;
import firered.scripts.YesnoEvent;
import firered.ui.*;

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
	public static Character npc;
	public static Player player;
	public static MessageBox messageBox;
	public static YesnoBox yesnoBox;
	public static State gameState = State.NORMAL;

	public static Pokemon p1;
	public static Pokemon p2;
	public static Pokemon p3;
	public static Battle battle;

	public Script script;

	private Game() {
		SpriteList.init();
		Loader.loadObjects();
		Loader.loadMaps();
		GameVariablesForScripts.init();

		screen = new Screen(WIDTH, HEIGHT);
		player = new Player(4, 4, new Sprite("entities/player_sprite"));
		npc = new Character(2, 2, new Sprite("entities/npc_sprite"));

		MapManager.loadMap(Map.MAPS_MAP.get("pallet_town"));
		MapManager.currentMap.addEntity(player);
		MapManager.currentMap.addEntity(npc);

		messageBox = new MessageBox(MessageBox.UI1, MyFont.DARK_FONT);
		yesnoBox = new YesnoBox();

		p1 = new Pokemon(BasePokemon.BASE_PIKACHU, 100, Pokemon.USE_BASE_HP, 10, 2, 10);
		p1.addMoves(Move.THUNDER_SHOCK, Move.GROWL, Move.QUICK_ATTACK, Move.THUNDER_WAVE);
		p2 = new Pokemon(BasePokemon.BASE_GASTLY, 100, Pokemon.USE_BASE_HP, 4, 3, 12);
		p2.addMoves(Move.HYPNOSIS, Move.LICK, Move.DREAM_EATER, Move.SHADOW_BALL);
		p3 = new Pokemon(BasePokemon.BASE_BULBASAUR, 100, Pokemon.USE_BASE_HP, 5, 5, 10);
		p3.addMoves(Move.TACKLE, Move.VINE_WHIP, Move.RAZOR_LEAF, Move.GROWTH);
		battle = new Battle(p3, p1);

		WarpManager.init(MapManager.currentMap);
		addKeyListener(new KeyInput());

		script = new Script("script5.scr");
		MapManager.currentMap.addScript(script, 3, 3);
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

		switch (gameState) {
			case NORMAL -> {
				if (KeyInput.wasPressed(KeyEvent.VK_SLASH)) {
					messageBox.enterDebug();
					gameState = State.COMMAND;
					break;
				}

				if (KeyInput.wasPressed(KeyEvent.VK_1)) script.startScript();

				if (KeyInput.wasPressed(KeyEvent.VK_B)) {
					battle.init();
					gameState = State.BATTLE;
				}

				if (KeyInput.isDown(KeyEvent.VK_UP)) player.move(0, -1);
				if (KeyInput.isDown(KeyEvent.VK_DOWN)) player.move(0, 1);
				if (KeyInput.isDown(KeyEvent.VK_LEFT)) player.move(-1, 0);
				if (KeyInput.isDown(KeyEvent.VK_RIGHT)) player.move(1, 0);

				if (KeyInput.isDown(KeyEvent.VK_W)) npc.move(0, -1);
				if (KeyInput.isDown(KeyEvent.VK_S)) npc.move(0, 1);
				if (KeyInput.isDown(KeyEvent.VK_A)) npc.move(-1, 0);
				if (KeyInput.isDown(KeyEvent.VK_D)) npc.move(1, 0);
				
				player.tick(delta);
				npc.tick(delta);
			}
			case MESSAGE_BOX -> {
				if (messageBox.active && !messageBox.stillRendering() && KeyInput.wasPressed(KeyEvent.VK_X) && !yesnoBox.active) {
					messageBox.continueText();
				}
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

		messageBox.tick(delta);
		yesnoBox.tick(delta);
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
			npc.render(screen);
			messageBox.render(screen);
			yesnoBox.render(screen);
		} else if (gameState == State.BATTLE) {
			battle.render(screen);
		}

		screen.renderAll();
		screen.scaleAndSetPixels(pixels);

		g.drawImage(image, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
		g.dispose();
		bs.show();
	}
}