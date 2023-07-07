package pokemon;

import pokemon.entity.Character;
import pokemon.entity.Player;
import pokemon.gfx.Screen;
import pokemon.gfx.SpriteList;
import pokemon.gfx.sprites.Sprite;
import pokemon.map.Map;
import pokemon.map.MapManager;
import pokemon.map.warp.WarpManager;
import pokemon.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;

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

        messageBox = new MessageBox(UIBox.UI1, MyFont.DARK_FONT);
        // text2 = new Text("Hehehe… It's a proper textbox!!", 12, 122, MyFont.DARK_FONT);
        // text = new Text("Much better than before, don't you think?", 12, 138, MyFont.DARK_FONT);

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
        if (KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
            List<String> r = Arrays.asList("Omigod a fancy animation?", "Dang Joe is just so cool...");
            messageBox.addText(r);
        }

        if (KeyInput.isDown(KeyEvent.VK_UP)) player.move(0, -1);
        if (KeyInput.isDown(KeyEvent.VK_DOWN)) player.move(0, 1);
        if (KeyInput.isDown(KeyEvent.VK_LEFT)) player.move(-1, 0);
        if (KeyInput.isDown(KeyEvent.VK_RIGHT)) player.move(1, 0);

        if (KeyInput.isDown(KeyEvent.VK_W)) npc.move(0, -1);
        if (KeyInput.isDown(KeyEvent.VK_S)) npc.move(0, 1);
        if (KeyInput.isDown(KeyEvent.VK_A)) npc.move(-1, 0);
        if (KeyInput.isDown(KeyEvent.VK_D)) npc.move(1, 0);

        // for (int i = 65; i <= 90; i++) {
        //     if (KeyInput.wasPressed(i)) messageBox.setText1(messageBox.text1.text + (char) i);
        // }
        // if (KeyInput.wasPressed(KeyEvent.VK_SPACE)) messageBox.setText1(messageBox.text1.text + " ");


        messageBox.tick(delta);
        player.tick(delta);
        npc.tick(delta);

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

        MapManager.render(screen);
        player.render(screen);
        npc.render(screen);
        messageBox.render(screen);

        screen.renderAll();
        screen.scaleAndSetPixels(pixels);

        g.drawImage(image, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.dispose();
        bs.show();
    }
}