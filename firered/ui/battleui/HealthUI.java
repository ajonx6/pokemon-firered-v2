package firered.ui.battleui;

import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.pokemon.Pokemon;
import firered.ui.MyFont;
import firered.ui.Text;

public class HealthUI {
    public static final Sprite PLAYER_STAT_BAR = new Sprite("ui/battle/player_stat_bar");
    public static final Sprite OPPONENT_STAT_BAR = new Sprite("ui/battle/op_stat_bar");

    public static final Sprite BARS = new Sprite("ui/battle/bar_colours");
    public static final Sprite GREEN_HEALTH = BARS.cutIntoNewSprite(0, 0, 1, 3);
    public static final Sprite YELLOW_HEALTH = BARS.cutIntoNewSprite(1, 0, 1, 3);
    public static final Sprite RED_HEALTH = BARS.cutIntoNewSprite(2, 0, 1, 3);
    public static final Sprite EXP_BAR = BARS.cutIntoNewSprite(3, 0, 1, 2);
    public static final int HEALTH_BAR_LENGTH = 48;
    
    public boolean isPlayer;
    public Pokemon pokemon;
    public Text name;
    public Text level;
    public Text health;

    public HealthUI(boolean isPlayer, Pokemon pokemon) {
        this.isPlayer = isPlayer;
        this.pokemon = pokemon;
        
        this.name = new Text(pokemon.nickname, MyFont.BATTLE_UI_FONT);
        if (isPlayer) name.setPosition(143, 76);
        else name.setPosition(21, 17);
        
        this.level = new Text("Lv" + pokemon.level, MyFont.BATTLE_UI_FONT);
        int levelTextWidth = level.getPixelWidth();
        if (isPlayer) level.setPosition(221 - levelTextWidth, 76);
        else level.setPosition(99 - levelTextWidth, 18);
        
        if (isPlayer) {
            health = new Text(pokemon.hp + "/" + pokemon.base.health, MyFont.BATTLE_UI_FONT);
            health.setPosition(221 - health.getPixelWidth(), 94);
        }
    }

    public void render(Screen screen) {
        double pokemonHealthPercent = pokemon.getHealthPercentage();
        if (isPlayer) {
            screen.prepareRender(126, 74, PLAYER_STAT_BAR, Screen.UI_ELEMENTS);
            for (int x = 0; x < 48.0 * pokemonHealthPercent; x++) {
                int xp = x + 174;
                screen.prepareRender(xp, 91, pokemonHealthPercent > 0.25 ? (pokemonHealthPercent > 0.5 ? GREEN_HEALTH : YELLOW_HEALTH) : RED_HEALTH, Screen.UI_ELEMENTS);
            }
        } else {
            screen.prepareRender(13, 16, OPPONENT_STAT_BAR, Screen.UI_ELEMENTS);
            for (int x = 0; x < 48.0 * pokemonHealthPercent; x++) {
                int xp = x + 52;
                screen.prepareRender(xp, 33, pokemonHealthPercent > 0.25 ? (pokemonHealthPercent > 0.5 ? GREEN_HEALTH : YELLOW_HEALTH) : RED_HEALTH, Screen.UI_ELEMENTS);
            }
        }
        
        name.render(screen);
        level.render(screen);
        if (health != null) health.render(screen);
    }
}