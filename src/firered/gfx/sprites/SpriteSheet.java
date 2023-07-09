package firered.gfx.sprites;

public class SpriteSheet {
	public Sprite sheet;
	public int spriteWidth, spriteHeight;
	public Sprite[] sprites;

	public SpriteSheet(String path, double width, double height) {
		this(path, (int) width, (int) height);
	}

	public SpriteSheet(String path, int width, int height) {
		this.sheet = new Sprite(path);
		this.spriteWidth = width;
		this.spriteHeight = height;
		getSprites();
	}

	public SpriteSheet(String path, int size) {
		this.sheet = new Sprite(path);
		this.spriteWidth = size;
		this.spriteHeight = size;
		getSprites();
	}

	public void getSprites() {
		int index = 0;
		sprites = new Sprite[sheet.width / spriteWidth * sheet.height / spriteHeight];
		for (int h = 0; h < sheet.height / spriteHeight; h++) {
			for (int w = 0; w < sheet.width / spriteWidth; w++) {
				int[] pix = new int[spriteWidth * spriteHeight];
				for (int y = 0; y < spriteHeight; y++) {
					int yp = h * spriteHeight + y;
					for (int x = 0; x < spriteWidth; x++) {
						int xp = w * spriteWidth + x;
						pix[x + y * spriteWidth] = sheet.pixels[xp + yp * sheet.width];
					}
				}
				sprites[index++] = new Sprite(spriteWidth, spriteHeight, pix);
			}
		}
	}

	public Sprite getSprite(int i) {
		return sprites[i];
	}

	public Sprite getSprite(int x, int y) {
		return sprites[x + y * (sheet.width / spriteWidth)];
	}
}