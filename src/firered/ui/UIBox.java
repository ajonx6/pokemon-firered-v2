package firered.ui;

import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;

public class UIBox {    
    public int x, y;
    public Sprite currentUI;

    public UIBox(int x, int y, Sprite currentUI) {
        this.x = x;
        this.y = y;
        this.currentUI = currentUI;
    }

    public void render(Screen screen) {
        // screen.prepareRender(x, y, currentUI.topLeft, Screen.UI_ELEMENTS);
        // screen.prepareRender(x + width - currentUI.dimension, y, currentUI.topRight, Screen.UI_ELEMENTS);
        // screen.prepareRender(x, y + height - currentUI.dimension, currentUI.bottomLeft, Screen.UI_ELEMENTS);
        // screen.prepareRender(x + width - currentUI.dimension, y + height - currentUI.dimension, currentUI.bottomRight, Screen.UI_ELEMENTS);
        //
        // for (int xx = currentUI.dimension; xx <= width - currentUI.dimension; xx++) {
        //     int xp = x + xx;
        //     screen.prepareRender(xp, y, currentUI.top, Screen.UI_ELEMENTS);
        //     screen.prepareRender(xp, y + height - currentUI.dimension, currentUI.bottom, Screen.UI_ELEMENTS);
        // }
        //
        // for (int yy = currentUI.dimension; yy <= height - currentUI.dimension; yy++) {
        //     int yp = y + yy;
        //     screen.prepareRender(x, yp, currentUI.left, Screen.UI_ELEMENTS);
        //     screen.prepareRender(x + width - currentUI.dimension, yp, currentUI.right, Screen.UI_ELEMENTS);
        // }
        //
        // for (int yy = currentUI.dimension; yy <= height - currentUI.dimension; yy++) {
        //     int yp = y + yy;
        //     for (int xx = currentUI.dimension; xx <= width - currentUI.dimension; xx++) {
        //         int xp = x + xx;
        //         screen.prepareRender(xp, yp, currentUI.middle, Screen.UI_ELEMENTS);
        //     }
        // }        
        
        screen.prepareRender(x, y, currentUI, Screen.UI_ELEMENTS);
    }
}
