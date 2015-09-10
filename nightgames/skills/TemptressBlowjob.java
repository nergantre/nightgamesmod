package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.FiredUp;

public class TemptressBlowjob extends Blowjob {

	public TemptressBlowjob(Character user) {
		super(user);
	}

	@Override
	public float priorityMod(Combat c) {
		return super.priorityMod(c) + 1.5f;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.has(Trait.temptress);
	}

	@Override
	public String describe(Combat c) {
		return "Use your supreme oral skills on your opponent's dick.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 15 + Global.random(getSelf().get(Attribute.Technique));

		if (getSelf().has(Trait.silvertongue))
			m += 4;

		if (target.roll(this, c, accuracy(c))) {
			if (!target.body.getRandomCock().isReady(target)) {
				m -= 7;
				target.body.pleasure(getSelf(),
						getSelf().body.getRandom("mouth"),
						target.body.getRandomCock(), m, c);
				if (target.body.getRandomCock().isReady(target)) {
					// Was flaccid, got hard
					c.write(getSelf(), deal(c, 0, Result.special, target));
					getSelf().add(new FiredUp(getSelf(), target, "hands"));
				} else {
					// Was flaccid, still is
					c.write(getSelf(), deal(c, 0, Result.weak, target));
				}
			} else {
				FiredUp status = (FiredUp) getSelf().status.stream()
						.filter(s -> s instanceof FiredUp).findAny()
						.orElse(null);
				int stack = status == null || !status.getPart().equals("mouth")
						? 0 : status.getStack();
				c.write(getSelf(), deal(c, stack, Result.normal, target));
				target.body.pleasure(getSelf(),
						getSelf().body.getRandom("mouth"),
						target.body.getRandomCock(), m, c);
				getSelf().add(new FiredUp(getSelf(), target, "mouth"));
			}
		} else {
			c.write(getSelf(), deal(c, 0, Result.miss, target));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new TemptressBlowjob(user);
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		switch (modifier) {
			case miss:
				return String.format("%s towards %s %s, but %s %s hips back.",
						getSelf().subjectAction("move", "moves"),
						target.nameOrPossessivePronoun(),
						target.body.getRandomCock().describe(target),
						target.pronoun(),
						target.action("pull", "pulls"));
			case weak:
				return String.format("%s up %s flaccid %s, doing everything %s"
						+ " can to get it hard, but %s %s back before %s can manage it.",
						getSelf().subjectAction("gobble", "gobbles"),
						target.nameOrPossessivePronoun(),
						target.body.getRandomCock().describe(target),
						getSelf().pronoun(),
						target.pronoun(),
						target.action("pull", "pulls"),
						getSelf().pronoun());
			case special:
				return String.format("%s %s %s into %s mouth and %s on it powerfully. It hardens"
						+ " swiftly, as if %s pulled the blood right into it.",
						getSelf().subjectAction("take", "takes"),
						target.nameOrPossessivePronoun(),
						target.body.getRandomCock().describe(target),
						getSelf().possessivePronoun(),
						getSelf().action("suck", "sucks"),
						getSelf().pronoun());
			default: // should be Result.normal
				switch (damage) {
					case 0:
						return String.format("%s to town on %s %s, licking it all over."
								+ " Long, slow licks along the shaft and small, swift lick"
								+ " around the head cause %s to groan in pleasure.",
								getSelf().subjectAction("go", "goes"),
								target.nameOrPossessivePronoun(),
								target.body.getRandomCock().describe(target),
								target.directObject());
					case 1:
						return String.format("%s %s lips around the head %s hard and wet %s "
								+ "and %s on it forcefully while swirling %s tongue rapidly"
								+ " around. At the same time, %s hands are massaging and"
								+ " caressing every bit of sensitive flesh not covered by"
								+ " %s mouth.",
								getSelf().subjectAction("lock", "locks"),
								getSelf().possessivePronoun(),
								target.nameOrPossessivePronoun(),
								target.body.getRandomCock().describe(target),
								getSelf().action("suck", "sucks"),
								getSelf().possessivePronoun(),
								getSelf().possessivePronoun(),
								getSelf().possessivePronoun());
					default:
						return String.format("%s bobbing up and down now, hands still working"
								+ " on any exposed skin while %s %s, %s and even %s all over %s"
								+ " over-stimulated manhood. %s %s not even trying to hide %s"
								+ " enjoyment, and %s %s loudly every time %s teeth graze"
								+ " %s shaft.",
								getSelf().subjectAction("are", "is"),
								getSelf().pronoun(),
								getSelf().action("lick", "licks"),
								getSelf().action("suck", "sucks"),
								getSelf().action("nibble", "nibbles"),
								target.possessivePronoun(),
								target.nameDirectObject(),
								target.possessivePronoun(),
								target.pronoun(),
								target.action("grunt", "grunts"),
								getSelf().possessivePronoun(),
								target.possessivePronoun());
				}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return deal(c, damage, modifier, target);
	}

}
