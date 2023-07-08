package firered.gfx.sprites;

import firered.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    public static final Sprite DEFAULT_SPRITE = new Sprite(Settings.TILE_SIZE, Settings.TILE_SIZE, 0xffff00ff);

    public BufferedImage image;
    public int width, height;
    public int[] pixels;
    public String path;

    public Sprite(String path) {
        try {
            this.path = path;
            this.image = ImageIO.read(new File("res/" + path + ".png"));
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.pixels = new int[width * height];
            this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
        } catch (IOException ioe) {
            System.err.println("Cannot find image: " + path + ".png");
            ioe.printStackTrace();
            System.exit(1);
        }

        // resizeInline(Settings.SCALE);
    }

    public Sprite(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[width * height];
        this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
    }

    public Sprite(Sprite sprite) {
        this(sprite.width, sprite.height, sprite.pixels);
    }

    public Sprite(int width, int height, int[] pix) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        System.arraycopy(pix, 0, this.pixels, 0, pix.length);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
    }

    public Sprite(int width, int height, int col) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = col;
        }
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);

        // resizeInline(Settings.SCALE);
    }

    // public Sprite cutIntoNewSprite(int x1, int y1, int x2, int y2) {
    // 	int[] newPix = new int[(x2 - x1 + 1) * (y2 - y1 + 1)];
    //
    // 	for (int x = x1; x < x2; x++) {
    // 		for (int y = y1; y < y2; y++) {
    // 			System.out.println((x - x1) + (y - y1) * (x2 - x1 + 1) + " " + newPix.length + " / " + (x + y * width) + " " + pixels.length);
    // 			newPix[(x - x1) + (y - y1) * (x2 - x1 + 1)] = pixels[x + y * width];
    // 		}
    // 	}
    //
    // 	return new Sprite(x2 - x1 + 1, y2 - y1 + 1, newPix);
    // }

    public Sprite cutIntoNewSprite(int xp, int yp, int w, int h) {
        int[] newPix = new int[w * h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                newPix[x + y * w] = pixels[(xp + x) + (yp + y) * width];
            }
        }

        return new Sprite(w, h, newPix);
    }

//	public Sprite resize(double scale) {
//		int newWidth = (int) (scale * image.getWidth());
//		int newHeight = (int) (scale * image.getHeight());
//		BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = resized.createGraphics();
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//		g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
//		g.dispose();
//
//		Sprite res = new Sprite(resized);
//		return res;
//	}

    public void resizeInline(double scale) {
        int newWidth = (int) (scale * image.getWidth());
        int newHeight = (int) (scale * image.getHeight());
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        this.image = resized;
        this.width = resized.getWidth();
        this.height = resized.getHeight();
        this.pixels = new int[width * height];
        this.pixels = resized.getRGB(0, 0, width, height, null, 0, width);
    }

    public void write(String path) throws IOException {
        File f = new File("res/" + path + ".png");
        if (!f.exists()) {
            f.createNewFile();
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        ImageIO.write(image, "png", f);
    }
}