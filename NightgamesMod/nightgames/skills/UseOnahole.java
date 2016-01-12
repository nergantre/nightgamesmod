package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class UseOnahole extends Skill {

	public UseOnahole(Character self) {
		super(Item.Onahole.getName(), self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (getSelf().has(Item.Onahole) || getSelf().has(Item.Onahole2)) && getSelf().canAct() && target.hasDick()
				&& c.getStance().reachBottom(getSelf()) && target.crotchAvailable() && !c.getStance().inserted(target);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 5 + Global.random(5);

		if (target.roll(this, c, accuracy(c))) {
			if (getSelf().has(Item.Onahole2)) {
				m += 5;
				if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.upgrade, target));
				} else {
					c.write(getSelf(), deal(c, 0, Result.upgrade, target));
				}
				target.body.pleasure(getSelf(), null, target.body.getRandomCock(), m, c);
			} else {
				if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.normal, target));
				} else {
					c.write(getSelf(), deal(c, 0, Result.upgrade, target));
				}
				target.body.pleasure(getSelf(), null, target.body.getRandomCock(), m, c);
			}
		} else {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.miss, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new UseOnahole(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.miss) {
			return "You try to stick your onahole onto " + target.name() + "'s dick, but she manages to avoid it.";
		} else if (modifier == Result.upgrade) {
			return "You slide your onahole over " + target.name()
					+ "'s dick. The well-lubricated toy covers her member with minimal resistance. As you pump it, she moans in "
					+ "pleasure and her hips buck involuntarily.";
		} else {
			return "You stick your cocksleeve onto " + target.name()
					+ "'s erection and rapidly pump it. She squirms a bit at the sensation and can't quite stifle a moan.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.miss) {
			return getSelf().name() + " tries to stick a cocksleeve on your dick, but you manage to avoid it.";
		} else if (modifier == Result.upgrade) {
			return getSelf().name()
					+ " slides her cocksleeve over your dick and starts pumping it. The sensation is the same as if she was riding you, but you're the only "
					+ "one who's feeling anything.";
		} else {
			return getSelf().name()
					+ " forces a cocksleeve over your erection and begins to pump it. At first the feeling is strange and a little bit uncomfortable, but the "
					+ "discomfort gradually becomes pleasure.";
		}

	}

	@Override
	public String describe(Combat c) {
		return "Pleasure opponent with an Onahole";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
