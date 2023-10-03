package firered.pokemon.moves;

import firered.battle.*;
import firered.pokemon.Pokemon;
import firered.pokemon.Type;
import firered.pokemon.moves.modifiers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Move {
	public static final Random RANDOM = new Random();
	public static final HashMap<String, Move> MOVES = new HashMap<>();
	
	public String name;
	public Type type;
	public MoveType moveType;
	public int maxPP;
	public List<MoveModifier> modifiers = new ArrayList<>();

	public Move(String id, String name, Type type, MoveType moveType, int maxPP) {
		this.name = name;
		this.type = type;
		this.moveType = moveType;
		this.maxPP = maxPP;
		MOVES.put(id, this);
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
			double damage = ((NormalDamageModifier) this.getModifier("NormalDamage")).damage;
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