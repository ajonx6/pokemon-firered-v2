package pokemon.ui;

import pokemon.gfx.sprites.Sprite;

public class MyCharacter {
    // public static final int DEFAULT_CHAR_WIDTH = 6;
    // public static final int DEFAULT_CHAR_HEIGHT = 9;
    // public static final int DEFAULT_X_ADVANCE = 3;
    
    public char character;
    public int width, height;
    public int xOffset, yOffset;
    public int xAdvance;
    public Sprite sprite;
    
    // public MyCharacter(char character, Sprite sprite, int index) {
    //     this.character = character;
    //     this.width = DEFAULT_CHAR_WIDTH;
    //     this.height = DEFAULT_CHAR_HEIGHT;
    //     this.yOffset = 0;
    //     this.xAdvance = DEFAULT_X_ADVANCE;
    //     this.sprite = sprite.cutIntoNewSprite(index * DEFAULT_CHAR_WIDTH, 0, DEFAULT_CHAR_WIDTH, DEFAULT_CHAR_HEIGHT);
    // }

    public MyCharacter(char character, int width, int height, int xOffset, int yOffset, int xAdvance, Sprite sprite) {
        this.character = character;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
        this.sprite = sprite;
    }

}