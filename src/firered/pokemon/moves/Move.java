package firered.pokemon.moves;

import firered.battle.*;
import firered.pokemon.Pokemon;
import firered.pokemon.StatType;
import firered.pokemon.StatusEffect;
import firered.pokemon.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Move {
	public static final Random RANDOM = new Random();

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
			.addModifier(new RestoreHealth(0.5, true));
	public static final Move SHADOW_BALL = new Move("Shadow Ball", Type.GHOST, 15)
			.addModifier(new NormalDamage(80))
			.addModifier(new AccuracyModifier())
			.addModifier(new StatValueModifier(StatType.Defence, -1, 0.2));

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
			.addModifier(new SelfStatValueModifier(StatType.Attack, 1));

	public static final Move THUNDER_SHOCK = new Move("Thundershock", Type.ELECTRIC, 30)
			.addModifier(new NormalDamage(40))
			.addModifier(new AccuracyModifier())
			.addModifier(new StatusModifier(StatusEffect.PARALYSIS, 0.1));
	public static final Move GROWL = new Move("Growl", Type.NORMAL, 40)
			.addModifier(new AccuracyModifier())
			.addModifier(new StatValueModifier(StatType.Defence, -1));
	public static final Move QUICK_ATTACK = new Move("Quick Attack", Type.NORMAL, 30)
			.addModifier(new NormalDamage(40))
			.addModifier(new AccuracyModifier())
			.addModifier(new PriorityMove(1));
	public static final Move THUNDER_WAVE = new Move("Thunder Wave", Type.ELECTRIC, 20)
			.addModifier(new AccuracyModifier())
			.addModifier(new StatusModifier(StatusEffect.PARALYSIS));

	public String name;
	public Type type;
	public int maxPP;
	public List<MoveModifier> modifiers = new ArrayList<>();

	public int pp;

	public Move(String name, Type type, int maxPP) {
		this.name = name;
		this.type = type;
		this.maxPP = maxPP;
		this.pp = maxPP;
	}

	public Move addModifier(MoveModifier mod) {
		modifiers.add(mod);
		return this;
	}

	public MoveModifier getModifier(String name) {
		for (MoveModifier mod : modifiers) {
			if (mod.name.equals(name)) return mod;
		}
		return null;
	}

	public boolean containsModifier(String name) {
		for (MoveModifier mod : modifiers) {
			if (mod.name.equals(name)) return true;
		}
		return false;
	}

	public List<PostMoveChange> useMove(Pokemon user, Pokemon defender) {
		List<PostMoveChange> changes = new ArrayList<>();

		changes.add(new WaitForAnimation(1.5));

		if (this.containsModifier("NormalDamage")) {
			double damage = ((NormalDamage) this.getModifier("NormalDamage")).damage;
			double damageCalculation = ((((2.0 * user.level / 5.0 + 2.0) * user.attack * damage / defender.defence) / 50.0) + 2.0) * getSTAB(user); // * resweak * ran/100
			damageCalculation /= 3.0;
			changes.add(new ChangePokemonHealth(defender, Math.max(0, defender.hp - (int) Math.ceil(damageCalculation))));
		}

		if (this.containsModifier("StatValueModifier")) {
			StatValueModifier svm = (StatValueModifier) this.getModifier("StatValueModifier");
			boolean active = RANDOM.nextDouble() < svm.prob;
			if (active) changes.add(new ChangeStat(defender, svm.type, svm.dx));
		}


		if (this.containsModifier("SelfStatValueModifier")) {
			SelfStatValueModifier ssvm = (SelfStatValueModifier) this.getModifier("SelfStatValueModifier");
			boolean active = RANDOM.nextDouble() < ssvm.prob;
			if (active) changes.add(new ChangeSelfStat(user, ssvm.type, ssvm.dx));
		}

		if (this.containsModifier("StatusModifier")) {
			StatusModifier ssvm = (StatusModifier) this.getModifier("StatusModifier");
			boolean active = RANDOM.nextDouble() < ssvm.prob;
			if (active) changes.add(new ChangeStatusCondition(defender, ssvm.type));

		}

		changes.add(new MoveComplete());

		return changes;
	}

	public double getSTAB(Pokemon p) {
		return type.equals(p.base.type1) || type.equals(p.base.type2) ? 1.5 : 1.0;
	}
}