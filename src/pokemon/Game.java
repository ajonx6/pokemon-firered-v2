package pokemon;

import pokemon.entity.Character;
import pokemon.entity.Entity;
import pokemon.entity.MapObject;
import pokemon.entity.Player;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;
import pokemon.level.Map;
import pokemon.level.TileData;
import pokemon.level.warp.WarpManager;

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
    public Character npc;
    public Player player;
    public Map map;
    public MapObject tree, tree2, tree3, tree4;

    private Game() {
        TileData.init();
        
        screen = new Screen(WIDTH, HEIGHT);
        player = new Player(4, 4, new Sprite("entities/player_sprite"));
        npc = new Character(2, 2, new Sprite("entities/npc_sprite"));
        tree = new MapObject(0, 0, new Sprite("objects/tree"));
        tree2 = new MapObject(0, 2, new Sprite("objects/tree"));
        tree3 = new MapObject(0, 4, new Sprite("objects/tree"));
        tree4 = new MapObject(0, 6, new Sprite("objects/tree"));
        map = new Map(16, 16);
        map.addEntity(player);
        map.addEntity(npc);
        map.addEntity(tree);
        map.addEntity(tree2);
        map.addEntity(tree3);
        map.addEntity(tree4);

        WarpManager.init(map);
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

        map.render(screen);
        player.render(screen);
        npc.render(screen);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
    }
}