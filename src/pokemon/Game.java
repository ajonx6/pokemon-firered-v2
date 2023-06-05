package pokemon;

import pokemon.entity.Character;
import pokemon.entity.Player;
import pokemon.gfx.Screen;
import pokemon.gfx.SpriteList;
import pokemon.gfx.sprites.Sprite;
import pokemon.map.Map;
import pokemon.map.MapManager;
import pokemon.map.warp.WarpManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;

public class Game extends Canvas implements Runnable {
    @Serial private static final long serialVersionUID = 1L;

    public static final int WIDTH = (int) (240 * Settings.SCALE);
    public static final int HEIGHT = (int) (160 * Settings.SCALE);
    public static final String TITLE = "2D Game";
    public static final double FPS = 300.0;

    public static boolean debug = false;
    private static Game instance;
    
    public BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    public JFrame frame;
    public boolean running;
    
    public Screen screen;
    public Character npc;
    public Player player;
    public Map map;

    private Game() {
        SpriteList.init();
        Loader.loadObjects();
        Loader.loadMaps();
        
        screen = new Screen(WIDTH, HEIGHT);
        player = new Player(4, 4, new Sprite("entities/player_sprite"));
        npc = new Character(2, 2, new Sprite("entities/npc_sprite"));
        
        MapManager.swapMap(Map.MAPS_MAP.get("pallet_town"));
        
        // map = new Map(0, 16, 16);
        // map = Loader.loadMap("pallet_town");
        map = Map.MAPS_MAP.get("pallet_town");
        map.addEntity(player);
        map.addEntity(npc);

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
        if (KeyInput.wasPressed(KeyEvent.VK_SPACE)) debug = !debug;
        
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
        
        screen.renderAll();

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.getPixel(i);
        }
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
    }
}