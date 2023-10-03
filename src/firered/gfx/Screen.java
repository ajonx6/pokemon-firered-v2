package firered.gfx;

import firered.Game;
import firered.Settings;
import firered.gfx.sprites.Sprite;
import firered.util.Vector;

import java.util.*;

public class Screen {
	public static final int TILE_LAYER = 0;
	public static final int COLLISION_OBJECTS = 1;
	public static final int NPCS = 2;
	public static final int OBJECTS_ABOVE_PLAYER = 3;
	public static final int UI_ELEMENTS = 4;
	
	public static double xOffset, yOffset;

	public int width, height;
	public int[] pixels;
	
	// Layer 0 = tiles 
	// Layer 1 = objects below player or collision objects
	// Layer 2 = npcs
	// Layer 3 = objects above player
	// Layer 4 = UI elements
	public List<List<RenderSprite>> layers = new ArrayList<>();
	public boolean[] sort = new boolean[] {false, true, true, true, false};
	
	public void prepareRender(Vector pos, Sprite sprite, int layer) {
		prepareRender(pos.getX(), pos.getY(), sprite, layer);
	}
	
	public void prepareRender(double x, double y, Sprite sprite, int layer) {
		int toAdd = layer - layers.size() + 1;
		while (toAdd-- > 0) {
			layers.add(new ArrayList<>());
		}
		layers.get(layer).add(new RenderSprite(x, y, sprite));
	}
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	
	public void setPixel(int i, int col) {
		double alpha = (double) ((col >> 24) & 0xff) / 255.0;
		if (alpha == 1.0) {
			pixels[i] = col;
		} else if (alpha > 0) {
			int oldColour = pixels[i];
			int newRed = ((oldColour >> 16) & 0xff) - (int) ((((oldColour >> 16) & 0xff) - ((col >> 16) & 0xff)) * alpha);
			int newGreen = ((oldColour >> 8) & 0xff) - (int) ((((oldColour >> 8) & 0xff) - ((col >> 8) & 0xff)) * alpha);
			int newBlue = (oldColour & 0xff) - (int) (((oldColour & 0xff) - (col & 0xff)) * alpha);
			pixels[i] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
		}
	}
	
	public void renderAll() {
		for (List<RenderSprite> layer : layers) {
			if (sort[layers.indexOf(layer)]) Collections.sort(layer, Comparator.comparingDouble(RenderSprite::getY));
			for (RenderSprite sprite : layer) {
				render(sprite.getX(), sprite.getY(), sprite.getSprite());
			}
			layer.clear();
		}
	}

	private void render(double x, double y, Sprite s) {
		render((int) x, (int) y, s);
	}

	private void render(int x, int y, Sprite s) {
		for (int yy = 0; yy < s.height; yy++) {
			int yp = y + yy;
			for (int xx = 0; xx < s.width; xx++) {
				int xp = x + xx;
				if (outOfBounds(xp, yp)) continue;
				else setPixel(xp + yp * width, s.pixels[xx + yy * s.width]);
			}
		}
	}

	private boolean outOfBounds(int xp, int yp) {
		return xp < 0 || yp < 0 || xp >= width || yp >= height;
	}

	public void clear() {
		clear(0);
	}

	public void clear(int colour) {
		for (int i = 0; i < pixels.length; i++) {
			setPixel(i, colour | 0xff000000);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPixel(int i) {
		return pixels[i];
	}

    public void scaleAndSetPixels(int[] pixels) {		
		for (int y = 0; y < Game.WINDOW_HEIGHT; y++) {
			for (int x = 0; x < Game.WINDOW_WIDTH; x++) {
				pixels[x + y * Game.WINDOW_WIDTH] = this.pixels[(x / Settings.SCALE) + (y / Settings.SCALE) * this.width];
			}
		}
    }
}