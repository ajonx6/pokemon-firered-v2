package firered.pokemon;

import firered.gfx.sprites.Sprite;
import firered.pokemon.moves.Move;
import firered.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasePokemon {
	public static final HashMap<String, BasePokemon> BASE_POKEMON = new HashMap<>();

	public String name;
	public int pokedexNumber;
	public Type type1, type2;
	public String ability;
	public Sprite frontSprite, backSprite;
	public Sprite frontSpriteShiny, backSpriteShiny;

	public int baseExp;
	public StatGroup baseStats;
	public StatGroup evStats;
	public List<MoveAndLevel> levelMoves = new ArrayList<>();
	public List<Move> machineMoves = new ArrayList<>();
	public double weight, height;

	public static BasePokemon addPokemon(String name, int pokedexNumber, Type type1, Type type2, String ability, int height, int weight) {
		name = Util.capitaliseFirst(name);
		BasePokemon bp = new BasePokemon(name, pokedexNumber, type1, type2, ability, height, weight);
		BASE_POKEMON.put(name, bp);
		return bp;
	}
	
	private BasePokemon(String name, int pokedexNumber, Type type1, Type type2, String ability, int height, int weight) {
		this.name = name;
		this.pokedexNumber = pokedexNumber;
		this.type1 = type1;
		this.type2 = type2;
		this.ability = ability;
		this.height = height;
		this.weight = weight;
	}

	public void setSprites(Sprite frontSprite, Sprite backSprite, Sprite frontSpriteShiny, Sprite backSpriteShiny) {
		this.frontSprite = frontSprite;
		this.backSprite = backSprite;
		this.frontSpriteShiny = frontSpriteShiny;
		this.backSpriteShiny = backSpriteShiny;
	}

	public void setBaseStats(int hp, int att, int def, int spatt, int spdef, int speed, int baseExp) {
		this.baseExp = baseExp;
		baseStats = new StatGroup(hp, att, def, spatt, spdef, speed);
	}

	public void setEVStats(int hp, int att, int def, int spatt, int spdef, int speed) {
		evStats = new StatGroup(hp, att, def, spatt, spdef, speed);
	}

	public void addLevelMove(Move move, int level) {
		levelMoves.add(new MoveAndLevel(move, level));
	}

	public void addMachineMove(Move move) {
		machineMoves.add(move);
	}

	public static void init() {
		// new BasePokemon("Pikachu", Type.ELECTRIC, Type.NULL, 100);
		// new BasePokemon("Bulbasaur", Type.GRASS, Type.POISON, 100);
		// new BasePokemon("Gastly", Type.GHOST, Type.POISON, 100);
		// new BasePokemon("Rattata", Type.NORMAL, Type.NULL, 100);
		// new BasePokemon("Pidgey", Type.NORMAL, Type.FLYING, 100);
	}

	public class MoveAndLevel implements Comparable {
		public Move move;
		public int level;

		public MoveAndLevel(Move move, int level) {
			this.move = move;
			this.level = level;
		}

		public int compareTo(Object o) {
			MoveAndLevel other = (MoveAndLevel) o;
			int c = Integer.compare(level, other.level);
			if (c == 0) return move.name.compareTo(other.move.name);
			else return c;
		}
	}
}
