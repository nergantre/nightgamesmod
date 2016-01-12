package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.TribadismStance;

public class Tribadism extends Skill {

	public Tribadism(String name, Character self, int cooldown) {
		super(name, self, cooldown);
	}

	public Tribadism(Character self) {
		super("Tribadism", self);
	}

	public BodyPart getSelfOrgan() {
		BodyPart res = getSelf().body.getRandomPussy();
		return res;
	}

	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomPussy();
	}

	public boolean fuckable(Combat c, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		boolean possible = selfO != null && targetO != null;
		boolean stancePossible = false;
		if (possible) {
			stancePossible = true;
			if (selfO.isType("pussy")) {
				stancePossible &= !c.getStance().vaginallyPenetrated(getSelf());
			}
			if (targetO.isType("pussy")) {
				stancePossible &= !c.getStance().vaginallyPenetrated(target);
			}
		}
		stancePossible &= !c.getStance().havingSex();
		return possible && stancePossible && getSelf().clothingFuckable(selfO) && target.crotchAvailable();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target) && c.getStance().mobile(getSelf()) && !c.getStance().mobile(target)
				&& getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		c.setStance(new TribadismStance(getSelf(), target));
		int otherm = 10;
		int m = 10;
		target.body.pleasure(getSelf(), selfO, targetO, m, c);
		getSelf().body.pleasure(target, targetO, selfO, otherm, c);
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Tribadism(user);
	}

	@Override
	public int speed() {
		return 2;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.normal) {
			return Global.format(
					"You grab {other:name-possessive} legs and push them apart. You then push your hot snatch across her pussy lips and grind down on it.",
					getSelf(), target);
		}
		return "Bad stuff happened";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		if (modifier == Result.normal) {
			String message = getSelf().name()
					+ " grabs your leg and slides her crotch against yours. She then grinds her "
					+ selfO.describe(getSelf()) + " against your wet " + targetO.describe(target) + ".";
			return message;
		}
		return "Bad stuff happened";
	}

	@Override
	public String describe(Combat c) {
		return "Grinds your pussy against your opponent's.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
