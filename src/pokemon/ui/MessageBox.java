package pokemon.ui;

import pokemon.Game;
import pokemon.gfx.Screen;

import java.util.ArrayList;
import java.util.List;

public class MessageBox extends UIBox {
    public static final int MESSAGE_BOX_HEIGHT = 48;
    public static final int DEFAULT_PADDING = 2;
    public static final double TIME_PER_CHARACTER = 0.04;
    
    public MyFont font;
    public String line1 = "", line2 = "";
    public int line1Index = 1, line2Index = 0;
    public Text text1, text2;
    public List<String> textToRender = new ArrayList<>();
    
    public double timer = 0;
    public int numCharsToRender = 0;

    public MessageBox(UISprites ui, MyFont font) {
        this(DEFAULT_PADDING, ui, font);
    }

    public MessageBox(int padding, UISprites ui, MyFont font) {
        super(padding, Game.HEIGHT - MESSAGE_BOX_HEIGHT + padding, Game.WIDTH - padding * 2, MESSAGE_BOX_HEIGHT - padding * 2, ui);
        this.font = font;
        this.text1 = new Text("", 12, 122, font);
        this.text2 = new Text("", 12, 138, font);
    }
    
    public void addText(List<String> toAdd) {
        textToRender.addAll(toAdd);
        line1 = textToRender.remove(0);
        line2 = textToRender.remove(0);
        text1.text = line1.equals("") ? "" : line1.substring(0, line1Index);
        text2.text = "";
        
        numCharsToRender = line1.length() + line2.length();
    }
    
    public void tick(double delta) {
        if (!line1.equals("") && line1.length() > text1.text.length()) {
            timer += delta;
            if (timer >= TIME_PER_CHARACTER) {
                timer -= TIME_PER_CHARACTER;
                text1.text = line1.substring(0, ++line1Index);
            }
        } else if (!line2.equals("") && line2.length() > text2.text.length()) {
            timer += delta;
            if (timer >= TIME_PER_CHARACTER) {
                timer -= TIME_PER_CHARACTER;
                text2.text = line2.substring(0, ++line2Index);
            }
        }
    }
    
    public void render(Screen screen) {
        super.render(screen);
        text1.render(screen);
        text2.render(screen);
    }
}
