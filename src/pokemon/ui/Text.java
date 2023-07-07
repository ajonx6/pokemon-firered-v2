package pokemon.ui;

import pokemon.gfx.Screen;
import pokemon.util.Vector;

public class Text {
    public String text;
    public Vector screenPosition;
    public MyFont font;

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
            xOffset +=  (ch.character == ' ' ? 3 : ch.xAdvance);
        }
    }
}