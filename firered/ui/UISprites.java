package firered.ui;

import firered.Settings;
import firered.gfx.sprites.Sprite;

public class UISprites {
    public static final int DEFAULT_DIMENSIONS = 6;

    public Sprite original;
    public Sprite topLeft, topRight, bottomLeft, bottomRight;
    public Sprite top, bottom, left, right;
    public Sprite middle;

    public int dimension;

    public UISprites(String name) {
        this(name, DEFAULT_DIMENSIONS);
    }

    public UISprites(String name, int dimension) {
        this.original = new Sprite("ui/" + name);
        this.dimension = dimension;

        this.topLeft = original.cutIntoNewSprite(0, 0, original.width / 2, original.height / 2);
        this.topRight = original.cutIntoNewSprite(original.width / 2, 0, original.width / 2, original.height / 2);
        this.bottomLeft = original.cutIntoNewSprite(0, original.height / 2, original.width / 2, original.height / 2);
        this.bottomRight = original.cutIntoNewSprite(original.width / 2, original.height / 2, original.width / 2, original.height / 2);

        this.top = original.cutIntoNewSprite(original.width / 2, 0, Settings.SCALE, original.height / 2);
        this.bottom = original.cutIntoNewSprite(original.width / 2, original.height / 2, Settings.SCALE, original.height / 2);
        this.left = original.cutIntoNewSprite(0, original.height / 2, original.width / 2, Settings.SCALE);
        this.right = original.cutIntoNewSprite(original.width / 2, original.height / 2, original.width / 2, Settings.SCALE);

        this.middle = original.cutIntoNewSprite(original.width / 2, original.height / 2, Settings.SCALE, Settings.SCALE);
    }
}