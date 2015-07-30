package skills;

import characters.Attribute;
import characters.Character;
import characters.Trait;
import combat.Combat;
import combat.Result;
import global.Global;
import stance.Engulfed;
import stance.Stance;
import status.Bound;

public class Engulf extends Skill {
	public Engulf(Character self) {
		super("Engulf", self, 5);
	}

	@Override
	public float priorityMod(Combat c) {
		return 2;
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.slime);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().en != Stance.engulfed && target.nude();
	}

	public int getMojoCost(Combat c) {
		return 30;
	}

	@Override
	public String describe() {
		return "Engulfs the opponent in your slime";
	}

	@Override
	public Skill copy(Character user) {
		return new Engulf(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.miss) {
			return Global.format("You will your slime to rush at {other:name} and pull her down, but she dodges away at the last second.\n", getSelf(), target);			
		} else {
			return Global.format("You will your slime to rush at {other:name} and manage to entrap her inside of yourself.\n", getSelf(), target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.miss) {
			return Global.format("{self:NAME}'s fluid body squirms violently and suddenly rushes at you. You manage to dodge out of the way and avoid being trapped.\n", getSelf(), target);			
		} else {
			return Global.format("{self:NAME}'s fluid body squirms violently and suddenly rushes at you. You don't react in time, and before you know it, {self:name-possessive} slime firmly locks you in inside {self:direct-object}", getSelf(), target);
		}
	}

	public String getTargetOrganType(Combat c, Character target) {
		return "none";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "none";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int difficulty = target.getLevel() - (target.getStamina().get() * 10 / target.getStamina().max()) + target.get(Attribute.Cunning) / 2;
		int strength = getSelf().getLevel() + getSelf().get(Attribute.Cunning) + getSelf().get(Attribute.Bio);

		boolean success = Global.random(Math.min(Math.max(difficulty - strength, 1), 10)) == 0;
		Result result = Result.normal;
		if (!success) {
			result = Result.miss;
		}
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, result, target));
		} else {
			c.write(getSelf(), receive(c, 0, result, target));
		}
		if (success) {
			c.setStance(new Engulfed(getSelf(), target));
			target.add(c, new Bound(target, 40, getSelf().name() + "'s slime"));
		}
		return success;
	}
}
