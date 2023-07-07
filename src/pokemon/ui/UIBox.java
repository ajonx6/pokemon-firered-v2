package pokemon.ui;

import pokemon.Settings;
import pokemon.gfx.Screen;

public class UIBox {
    public static final UISprites UI1 = new UISprites("ui1");
    public static final UISprites UI2 = new UISprites("ui2");
    public static final UISprites UI3 = new UISprites("ui3");
    public static final UISprites UI4 = new UISprites("ui4");
    public static final UISprites UI5 = new UISprites("ui5");
    public static final UISprites UI6 = new UISprites("ui6");
    
    public int x, y;
    public int width, height;
    public UISprites currentUI;

    public UIBox(int x, int y, int width, int height, UISprites ui) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentUI = ui;
    }

    public void render(Screen screen) {
        screen.prepareRender(x, y, currentUI.topLeft, Screen.UI_ELEMENTS);
        screen.prepareRender(x + width - currentUI.dimension, y, currentUI.topRight, Screen.UI_ELEMENTS);
        screen.prepareRender(x, y + height - currentUI.dimension, currentUI.bottomLeft, Screen.UI_ELEMENTS);
        screen.prepareRender(x + width - currentUI.dimension, y + height - currentUI.dimension, currentUI.bottomRight, Screen.UI_ELEMENTS);

        for (int xx = currentUI.dimension; xx <= width - currentUI.dimension; xx++) {
            int xp = x + xx;
            screen.prepareRender(xp, y, currentUI.top, Screen.UI_ELEMENTS);
            screen.prepareRender(xp, y + height - currentUI.dimension, currentUI.bottom, Screen.UI_ELEMENTS);
        }

        for (int yy = currentUI.dimension; yy <= height - currentUI.dimension; yy++) {
            int yp = y + yy;
            screen.prepareRender(x, yp, currentUI.left, Screen.UI_ELEMENTS);
            screen.prepareRender(x + width - currentUI.dimension, yp, currentUI.right, Screen.UI_ELEMENTS);
        }

        for (int yy = currentUI.dimension; yy <= height - currentUI.dimension; yy++) {
            int yp = y + yy;
            for (int xx = currentUI.dimension; xx <= width - currentUI.dimension; xx++) {
                int xp = x + xx;
                screen.prepareRender(xp, yp, currentUI.middle, Screen.UI_ELEMENTS);
            }
        }        
    }
}
