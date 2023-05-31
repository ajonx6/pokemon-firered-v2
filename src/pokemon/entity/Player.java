package pokemon.entity;

import pokemon.Game;
import pokemon.Settings;
import pokemon.gfx.Screen;
import pokemon.gfx.sprites.Sprite;
import pokemon.level.Map;
import pokemon.util.Vector;

public class Player extends Entity {
    public Player(double wx, double wy, Sprite sprite) {
        super(wx, wy, sprite);
        Map.offsetX = Game.WIDTH / 2 - (worldPos.getX() + sprite.width / 2);
        Map.offsetY = Game.HEIGHT / 2 - (worldPos.getY() + sprite.height / 2 + 8 - Settings.SCALED_TILE_SIZE);
        this.screenPos.set(Game.WIDTH / 2 - sprite.width / 2, Game.HEIGHT / 2 - sprite.height / 2);
        this.screenPos = Vector.add(screenPos, 0, Settings.SCALED_TILE_SIZE - sprite.height / 2);// new Vector(Game.WIDTH / 2 - sprite.width / 2, Game.HEIGHT / 2 - Settings.SCALED_TILE_SIZE - );
    }
    
    public void processMovement(double delta) {
        if (currentlyMoving) {
            moveTime += delta;
            worldPos = Vector.add(worldPos, dx * delta, dy * delta);
            Map.offsetX -= dx * delta;
            Map.offsetY -= dy * delta;
        }
        if (moveTime > TILE_MOVE_TIME) {
            moveTime = 0;
            currentlyMoving = false;
            this.dx = 0;
            this.dy = 0;
            Map.offsetX = Math.round(Map.offsetX);
            Map.offsetY = Math.round(Map.offsetY);
            this.worldPos = Vector.mul(tilePos, Settings.SCALED_TILE_SIZE);
        }
    }

    public void render(Screen screen) {
        screen.render(screenPos.getX(), screenPos.getY(), sprite);
    }
}