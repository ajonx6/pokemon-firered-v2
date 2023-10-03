package firered.entity;

import firered.Game;
import firered.Settings;
import firered.State;
import firered.gfx.Screen;
import firered.gfx.sprites.Sprite;
import firered.map.MapManager;
import firered.pokemon.Pokemon;
import firered.scripts.Script;
import firered.util.Vector;

import java.util.Map;

public class Player extends firered.entity.NPC {
	public static final String NAME = "Ajonx";
	public static final String RIVAL_NAME = "Ash";

	public Player(int id, double wx, double wy, Map<String, Sprite> sprites) {
		super(id, wx, wy, sprites);
		MapManager.offsetX = Game.WIDTH / 2 - (worldPos.getX() + sprite.width / 2);
		MapManager.offsetY = Game.HEIGHT / 2 - (worldPos.getY() + sprite.height / 2 + 8 - Settings.TILE_SIZE - 3);
		this.screenPos.set(Game.WIDTH / 2 - sprite.width / 2, Game.HEIGHT / 2 - sprite.height / 2);
		this.screenPos = Vector.add(screenPos, 0, Settings.TILE_SIZE - sprite.height / 2);// new Vector(Game.WIDTH / 2 - sprite.width / 2, Game.HEIGHT / 2 - Settings.TILE_SIZE - );
	}

	public void processMovement(double delta) {
		if (currentlyMoving) {
			moveTime += delta;
			worldPos = Vector.add(worldPos, dx * delta, dy * delta);
			MapManager.offsetX -= dx * delta;
			MapManager.offsetY -= dy * delta;
		}
		if (moveTime > TILE_MOVE_TIME) {
			moveTime = 0;
			currentlyMoving = false;
			this.dx = 0;
			this.dy = 0;
			this.tilePos.set(destinationTile);
			this.destinationTile = null;
			MapManager.offsetX = Math.round(MapManager.offsetX);
			MapManager.offsetY = Math.round(MapManager.offsetY);
			this.worldPos = Vector.mul(tilePos, Settings.TILE_SIZE);

			Pokemon wildPokemon = MapManager.checkForWildBattle(tilePos);
			if (wildPokemon != null) {
				Game.battle.startBattle(Game.p3, wildPokemon);
			}
		}
	}

	public void tick(double delta) {
		super.tick(delta);
		Script scriptUnder = MapManager.scriptUnder(this);
		if (Game.gameState == State.NORMAL && scriptUnder != null && !currentlyMoving && hasMoved) {
			scriptUnder.startScript();
			hasMoved = false;
		}
	}

	public void render(Screen screen) {
		screen.prepareRender(screenPos.getX(), screenPos.getY(), sprite, Screen.NPCS);
	}
}