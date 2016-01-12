package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;

public class PullOut extends Skill {

	public PullOut(Character self) {
		super("Pull Out", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.hasStatus(Stsflag.knotted) && getSelf().canAct()
				&& (c.getStance().en == Stance.facesitting
						|| c.getStance().inserted()
								&& c.getStance().dom(getSelf()));
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result result = Result.normal;
		if (c.getStance().inserted(getSelf())) {
			if (c.getStance().en == Stance.anal) {
				result = Result.anal;
			}
		} else if (c.getStance().inserted(target)) {
			result = Result.reverse;
		} else {
			result = Result.special;
		}
		if (c.getStance().en == Stance.anal) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, result, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, result, target));
			}
			c.setStance(c.getStance().insertRandom());
		} else if (result == Result.special) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, result, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, result, target));
			}
			c.setStance(new StandingOver(getSelf(), target));
		} else {
			if (getSelf().hasStatus(Stsflag.leglocked)
					|| getSelf().hasStatus(Stsflag.armlocked)
					|| target.has(Trait.tight)
							&& c.getStance().inserted(getSelf())) {
				boolean escaped = getSelf().check(Attribute.Power,
						10 - getSelf().escape(c) + target.get(Attribute.Power));
				if (escaped) {
					if (getSelf().human()) {
						c.write(getSelf(), deal(c, 0, result, target));
					} else if (target.human()) {
						c.write(getSelf(), receive(c, 0, result, target));
					}
				} else {
					if (getSelf().hasStatus(Stsflag.leglocked)) {
						BodyPart part = c.getStance().anallyPenetrated(
								getSelf()) ? target.body.getRandom("ass")
										: target.body.getRandomPussy();
						String partString = part.describe(target);
						if (getSelf().human()) {
							c.write(getSelf(), "You try to pull out of "
									+ target.name() + "'s " + partString
									+ ", but her legs immediately tighten against your waist, holding you inside her. The mere friction from her action sends a shiver down your spine.");
						} else {
							c.write(getSelf(), "She tries to pull out of "
									+ target.nameOrPossessivePronoun() + " "
									+ partString
									+ ", but your legs immediately pull her back in, holding you inside her.");
						}
					} else if (getSelf().hasStatus(Stsflag.armlocked)) {
						if (getSelf().human()) {
							c.write(getSelf(),
									"You try to pull yourself off of "
											+ target.name()
											+ ", but she merely pulls you back on top of her, surrounding you in her embrace.");
						} else {
							c.write(getSelf(),
									"She tries to pull herself off of "
											+ target.name()
											+ ", but with a gentle pull of your hands, she collapses back on top of you.");
						}
					} else if (target.has(Trait.tight)
							&& c.getStance().inserted(getSelf())) {
						BodyPart part = c.getStance().anallyPenetrated(target)
								? target.body.getRandom("ass")
								: target.body.getRandomPussy();
						String partString = part.describe(target);
						if (getSelf().human()) {
							c.write(getSelf(),
									"You try to pull yourself out of "
											+ target.name() + "'s " + partString
											+ ", but she clamps down hard on your cock while smiling at you. You almost cum from the sensation, and quickly abandon ideas about your escape.");
						} else {
							c.write(getSelf(),
									"She tries to pull herself out of "
											+ target.nameOrPossessivePronoun()
											+ " " + partString
											+ ", but you clamp down hard on her cock, and prevent her from pulling out.");
						}
					}
					int m = 8;
					if (c.getStance().inserted(getSelf())) {
						BodyPart part = c.getStance().anallyPenetrated(target)
								? target.body.getRandom("ass")
								: target.body.getRandomPussy();
						getSelf().body.pleasure(target, part,
								getSelf().body.getRandomInsertable(), m, c);
					}
					getSelf().struggle();
					return false;
				}
			} else if (getSelf().hasStatus(Stsflag.cockbound)) {
				CockBound s = (CockBound) getSelf()
						.getStatus(Stsflag.cockbound);
				c.write(getSelf(), "You try to pull out of " + target.name()
						+ "'s " + target.body.getRandomPussy() + ", but "
						+ s.binding
						+ " instantly reacts and pulls your dick back in.");
				int m = 8;
				getSelf().body.pleasure(target, target.body.getRandom("pussy"),
						getSelf().body.getRandom("cock"), m, c);
				return false;
			} else if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, result, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, result, target));
			}
			c.setStance(c.getStance().insertRandom());
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new PullOut(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.reverse) {
			return "You rise up and let " + target.nameOrPossessivePronoun()
					+ " girl-cock slip out of your "
					+ (c.getStance().en == Stance.anal ? "ass." : "pussy");
		} else if (modifier == Result.anal) {
			return "You pull your dick completely out of " + target.name()
					+ "'s ass.";
		} else if (modifier == Result.normal) {
			return "You pull completely out of " + target.name()
					+ "'s pussy, causing her to let out a disappointed little whimper.";
		} else {
			return "You pull yourself off " + target.name()
					+ "'s face, causing her to gasp lungfuls of the new fresh air offer to her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.anal) {
			return "You feel the pressure in your anus recede as "
					+ getSelf().name() + " pulls out.";
		} else if (modifier == Result.reverse) {
			return getSelf().name()
					+ " lifts her hips more than normal, letting your dick slip completely out of her.";
		} else if (modifier == Result.normal) {
			return getSelf().name()
					+ " pulls her dick completely out of your pussy, leaving you feeling empty.";
		} else {
			return getSelf().name()
					+ " lifts herself off your face, giving you a brief respite.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Aborts penetration";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
