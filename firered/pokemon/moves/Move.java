package firered.pokemon.moves;

import firered.pokemon.StatType;
import firered.pokemon.StatusEffect;
import firered.pokemon.Type;

import java.util.ArrayList;
import java.util.List;

public class Move {
	public static final Move HYPNOSIS = new Move("Hypnosis", Type.PSYCHIC, 20)
			.addModifier(new AccuracyModifier(0.6))
			.addModifier(new StatusModifier(StatusEffect.SLEEP));
	public static final Move LICK = new Move("Lick", Type.ELECTRIC, 30)
			.addModifier(new NormalDamage(20))
			.addModifier(new AccuracyModifier())
			.addModifier(new StatusModifier(StatusEffect.PARALYSIS, 0.3));
	public static final Move DREAM_EATER = new Move("Dream Eater", Type.PSYCHIC, 15)
			.addModifier(new DamageOnlyWithStatus(100, StatusEffect.SLEEP))
			.addModifier(new AccuracyModifier())
			.addModifier(new RestoreHealth(0.5 , true));
	public static final Move SHADOW_BALL = new Move("Shadow Ball", Type.GHOST, 15)
			.addModifier(new NormalDamage(80))
			.addModifier(new AccuracyModifier())
			.addModifier(new StatValueModifier(StatType.DEFENCE, -1, 0.2));
	
	public static final Move TACKLE = new Move("Tackle", Type.NORMAL, 35)
			.addModifier(new NormalDamage(35))
			.addModifier(new AccuracyModifier(0.95));
	public static final Move VINE_WHIP = new Move("Wine Whip", Type.GRASS, 10)
			.addModifier(new NormalDamage(35))
			.addModifier(new AccuracyModifier());
	public static final Move RAZOR_LEAF = new Move("Razor Leaf", Type.GRASS, 25)
			.addModifier(new NormalDamage(55))
			.addModifier(new AccuracyModifier(0.95));
	public static final Move GROWTH = new Move("Growth", Type.NORMAL, 40)
			.addModifier(new SelfStatValueModifier(StatType.ATTACK, 1));
	
	public static final Move THUNDER_SHOCK = new Move("Thundershock", Type.ELECTRIC, 30)
			.addModifier(new NormalDamage(40))
			.addModifier(new AccuracyModifier())
			.addModifier(new StatusModifier(StatusEffect.PARALYSIS, 0.1));
	public static final Move GROWL = new Move("Growl", Type.NORMAL, 40)
			.addModifier(new AccuracyModifier())
			.addModifier(new StatValueModifier(StatType.DEFENCE, -1));
	public static final Move QUICK_ATTACK = new Move("Quick Attack", Type.NORMAL, 30)
			.addModifier(new NormalDamage(40))
			.addModifier(new AccuracyModifier())
			.addModifier(new PriorityMove(1));
	public static final Move THUNDER_WAVE = new Move("Thunder Wave", Type.ELECTRIC, 20)
			.addModifier(new AccuracyModifier())
			.addModifier(new StatusModifier(StatusEffect.PARALYSIS));
	
	public String name;
	public Type type;
	public int pp;
	public List<MoveModifier> modifiers = new ArrayList<>();

	public Move(String name, Type type, int pp) {
		this.name = name;
		this.type = type;
		this.pp = pp;
	}
	
	public Move addModifier(MoveModifier mod) {
		modifiers.add(mod);
		return this;
	}
}