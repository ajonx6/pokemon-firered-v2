package firered.ui;

import firered.gfx.Screen;
import firered.util.Vector;

public class Text {
    public String text;
    public Vector screenPosition;
    public MyFont font;

    public Text(String text, MyFont font) {
        this(text, 0, 0, font);
    }
    
    public Text(String text, int x, int y, MyFont font) {
        this.text = text;
        this.screenPosition = new Vector(x, y);
        this.font = font;
    }

    public void render(Screen screen) {
        int xOffset = 0;
        for (char c : text.toCharArray()) {
            MyCharacter ch = font.getCharacterByChar(c);
            screen.prepareRender(Vector.add(screenPosition, xOffset + ch.xOffset, font.maxHeight - ch.height + ch.yOffset), ch.sprite, Screen.UI_ELEMENTS);
            xOffset += ch.xAdvance;
        }
    }

    public int getPixelWidth() {
        int width = 0;
        for (char c : text.toCharArray()) {
            MyCharacter ch = font.getCharacterByChar(c);
            width += ch.xAdvance;
        }
        return width;
    }
    
    public void setPosition(int x, int y) {
        this.screenPosition = new Vector(x, y);
    }
}