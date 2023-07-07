package pokemon.ui;

import pokemon.gfx.sprites.Sprite;
import pokemon.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyFont {
    // public static MyFont UPPERCASE_DARK_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'", "uppercase_dark", 3, true);
    public static MyFont LIGHT_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'", "light");
    public static MyFont DARK_FONT = new MyFont("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,!?/-…\"\'", "dark");

    public Sprite originalSprite;
    public String chars;
    public HashMap<Character, MyCharacter> charData = new HashMap<>();
    public int maxHeight;

    private MyFont(String chars, String name) {
        this.originalSprite = new Sprite("ui/fonts/" + name);
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