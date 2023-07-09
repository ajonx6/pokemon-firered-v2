package firered.ui;

import firered.gfx.sprites.Sprite;
import firered.util.Util;

import java.util.HashMap;
import java.util.List;

public class MyFont {
    // public static MyFont UPPERCASE_DARK_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'", "uppercase_dark", 3, true);
    public static MyFont LIGHT_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'()&:><", "light", true);
    public static MyFont DARK_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'()&:><", "dark", true);
    public static MyFont BEIGE_DARK_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'()&:><", "beige_dark", true);
    public static MyFont BATTLE_UI_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-\"\'", "battle_font", false);
    public static MyFont BATTLE_UI_FONT_LIGHT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-\"\'", "battle_font_light", false);

    public Sprite originalSprite;
    public Sprite cursorSprite;
    public String chars;
    public HashMap<Character, MyCharacter> charData = new HashMap<>();
    public int maxHeight;

    private MyFont(String chars, String name, boolean hasCursor) {
        this.originalSprite = new Sprite("ui/fonts/" + name);
        if (hasCursor) this.cursorSprite = new Sprite("ui/fonts/" + name + "_cursor");
        this.chars = chars;

        List<String> charData = Util.load("ui/fonts/" + name + ".fd");
        this.maxHeight = Integer.parseInt(charData.get(0));
        int xSpriteOffset = 0;
        for (int i = 1; i < charData.size(); i++) {
            String[] tokens = charData.get(i).split("\\\\");
            char c = tokens[0].charAt(0);

            int width = Integer.parseInt(tokens[1]);
            int height = Integer.parseInt(tokens[2]);
            Sprite sprite = originalSprite.cutIntoNewSprite(xSpriteOffset, 0, width, height);
            this.charData.put(c, new MyCharacter(c, width, height, Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), sprite));
            xSpriteOffset += width;
        }
    }

    public MyCharacter getCharacterByChar(char c) {
        return charData.get(c);
    }
}