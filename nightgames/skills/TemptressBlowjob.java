package nightgames.skills;

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
		int m = 15 + Global.random(15);

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
				return String.format("");
			case weak:
				return String.format("");
			case special:
				return String.format("");
			default: // should be Result.normal
				// already hard
				switch (damage) {
					case 0:
						return String.format("");
					case 1:
						return String.format("");
					default:
						return String.format("");
				}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return deal(c, damage, modifier, target);
	}

}
