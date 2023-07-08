package firered;

import firered.battle.Battle;
import firered.battle.BattleState;
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
import firered.pokemon.moves.Move;
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

    public Screen screen;
    public Character npc;
    public Player player;
    public MessageBox messageBox;
    public State gameState = State.NORMAL;
    
    public Pokemon playerPokemon;
    public Pokemon opponentPokemon;    
    public Battle battle;

    private Game() {
        SpriteList.init();
        Loader.loadObjects();
        Loader.loadMaps();

        screen = new Screen(WIDTH, HEIGHT);
        player = new Player(4, 4, new Sprite("entities/player_sprite"));
        npc = new Character(2, 2, new Sprite("entities/npc_sprite"));

        MapManager.loadMap(Map.MAPS_MAP.get("pallet_town"));
        MapManager.currentMap.addEntity(player);
        MapManager.currentMap.addEntity(npc);

        messageBox = new MessageBox(MessageBox.UI1, MyFont.DARK_FONT);

        playerPokemon = new Pokemon(BasePokemon.BASE_PIKACHU, 100, 43, 2, 2);
        playerPokemon.addMoves(Move.THUNDER_SHOCK, Move.GROWL, Move.QUICK_ATTACK, Move.THUNDER_WAVE);
        opponentPokemon = new Pokemon(BasePokemon.BASE_GASTLY, 54, 81, 4, 3);
        opponentPokemon.addMoves(Move.HYPNOSIS, Move.LICK, Move.DREAM_EATER, Move.SHADOW_BALL);
        battle = new Battle(playerPokemon, opponentPokemon);

        WarpManager.init(MapManager.currentMap);
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
        
        switch (gameState) {
            case NORMAL -> {
                // if (!messageBox.debugMode && KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
                //     // List<String> r = Util.scriptToMessages("Omigod a fancy animation?%nDang: Joe is just (so) cool...%pOoooh another screen...?%pDidn't expect that hahah!%p");
                //     List<String> r = Util.scriptToMessages("hi%p%p");
                //     messageBox.addText(r);
                // }

                if (KeyInput.wasPressed(KeyEvent.VK_SLASH)) {
                    messageBox.enterDebug();
                    gameState = State.COMMAND;
                    break;
                }
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

                messageBox.tick(delta);
                player.tick(delta);
                npc.tick(delta);
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
                if (battle.battleState == BattleState.WAITING_FOR_INPUT) {
                    if (KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_DOWN))
                        battle.optionUp = !battle.optionUp;
                    if (KeyInput.wasPressed(KeyEvent.VK_LEFT) || KeyInput.wasPressed(KeyEvent.VK_RIGHT))
                        battle.optionLeft = !battle.optionLeft;
                } else if (battle.battleState == BattleState.SELECTING_MOVE) {
                    if (KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_DOWN))
                        battle.movePickerUI.optionUp = !battle.movePickerUI.optionUp;
                    if (KeyInput.wasPressed(KeyEvent.VK_LEFT) || KeyInput.wasPressed(KeyEvent.VK_RIGHT))
                        battle.movePickerUI.optionLeft = !battle.movePickerUI.optionLeft;
                }
                battle.tick(delta);
            }
        }

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

        if (gameState == State.NORMAL || gameState == State.COMMAND) {
            MapManager.render(screen);
            player.render(screen);
            npc.render(screen);
            messageBox.render(screen);
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