package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Anal;
import nightgames.stance.Cowgirl;
import nightgames.stance.Missionary;
import nightgames.status.Shamed;

public class Offer extends Skill {

	public Offer(Character self) {
		super("Offer", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getPure(Attribute.Submissive) >= 6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (target.pantsless() || target.has(Trait.strapped))
				&& c.getStance().mobile(target)
				&& !c.getStance().mobile(getSelf())
				&& (target.hasDick() || target.has(Trait.strapped)
						|| target.hasPussy() && getSelf().hasDick())
				&& getSelf().canAct() && target.canAct()
				&& !c.getStance().inserted();
	}

	@Override
	public String describe(Combat c) {
		Character other = c.getOther(getSelf());
		return other.hasDick() || other.has(Trait.strapped)
				? "Offer your " + (getSelf().hasPussy() ? "pussy" : "ass")
						+ " to " + other.possessivePronoun() + "'s "
						+ other.body.getRandomInsertable().describe(other)
				: "Offer " + other.directObject() + " the use of your dick";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.getArousal().get() < 15) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.miss, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.miss, target));
			}
			getSelf().add(new Shamed(getSelf()));
			if (target.hasDick() || target.has(Trait.strapped)) {
				new Spank(target).resolve(c, getSelf());
			}
			return false;
		}
		if (target.hasDick() || target.has(Trait.strapped)) {
			if (getSelf().hasPussy()) {
				// offer pussy to dick/strapon
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.special, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.special, target));
				}
				c.setStance(new Missionary(target, getSelf()));
				getSelf().body.pleasure(target, target.body.getRandomCock(),
						getSelf().body.getRandomPussy(),
						Global.random(5) + getSelf().get(Attribute.Perception),
						c);
				target.body.pleasure(getSelf(), getSelf().body.getRandomPussy(),
						target.body.getRandomCock(),
						Global.random(5) + getSelf().get(Attribute.Perception),
						c);

			} else {
				// offer ass to dick/strapon
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.anal, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.anal, target));
				}
				c.setStance(new Anal(target, getSelf()));
				getSelf().body.pleasure(target,
						target.body.getRandomInsertable(),
						getSelf().body.getRandomAss(),
						Global.random(5) + getSelf().get(Attribute.Perception),
						c);
				if (!target.has(Trait.strapped)) {
					target.body.pleasure(getSelf(),
							getSelf().body.getRandomAss(),
							target.body.getRandomCock(), Global.random(5)
									+ getSelf().get(Attribute.Perception),
							c);
				}
			}
		} else {
			assert getSelf().hasDick() && target.hasPussy();
			// Offer cock to female
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.normal, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.normal, target));
			}
			c.setStance(new Cowgirl(target, getSelf()));
			getSelf().body.pleasure(target, target.body.getRandomPussy(),
					getSelf().body.getRandomCock(),
					Global.random(5) + getSelf().get(Attribute.Perception), c);
			target.body.pleasure(getSelf(), getSelf().body.getRandomCock(),
					target.body.getRandomPussy(),
					Global.random(5) + getSelf().get(Attribute.Perception), c);
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Offer(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.negative;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		switch (modifier) {
			case miss:
				if (target.hasDick() || target.has(Trait.strapped)) {
					return String.format(
							"You get on all fours and offer your %s to %s, but %s merely "
									+ "chuckles at your meekness. Before you can get back up in shame,"
									+ " %s gives you a very satisfying slap on your ass for your troubles.",
							getSelf().hasPussy() ? "pussy" : "ass",
							target.name(), target.pronoun());
				} else {
					return String
							.format("You wave your %s at %s, but %s ignores you completely.",
									getSelf().body.getRandomCock()
											.describe(getSelf()),
									target.name(), target.pronoun());
				}
			case normal:
				return String.format(
						"You lay flat on your back with your legs together and holding your %s "
								+ "straight up with your hand, all ready for %s to mount. %s weighs the situation for only"
								+ " a brief moment before sitting down on your awaiting shaft.",
						getSelf().body.getRandomCock().describe(getSelf()),
						target.name(),
						Global.capitalizeFirstLetter(target.pronoun()));
			case anal:
				return String.format(
						"You get on the ground with "
								+ "your shoulders on the ground and your ass in the air, pointing towards %s."
								+ " Reaching back, you spread your butt and softly whimper an invitation for %s"
								+ " to stick %s %s into your ass. %s takes pity on you, and plunges in.",
						target.name(), target.directObject(),
						target.possessivePronoun(),
						target.body.getRandomInsertable().describe(target),
						Global.capitalizeFirstLetter(target.pronoun()));
			default: // special
				return String.format(
						"You lay down on your back and spread your legs,"
								+ "offering your %s to %s while gazing up at %s powerful %s, hoping "
								+ "that %s will soon fill you with it.",
						getSelf().body.getRandomPussy().describe(getSelf()),
						target.name(), target.possessivePronoun(),
						target.body.getRandomInsertable().describe(target),
						target.pronoun());
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		switch (modifier) {
			case miss:
				if (target.hasDick() || target.has(Trait.strapped)) {
					return String.format(
							"%s gets down and sticks her ass in the air, offering it up to you. "
									+ "You are not interested, however, and just spank %s. %s seemed to enjoy that, but"
									+ " is still disappointed over not getting the fucking %s wanted.",
							getSelf().name(), getSelf().directObject(),
							Global.capitalizeFirstLetter(getSelf().pronoun()),
							getSelf().pronoun());
				} else {
					return String.format(
							"%s grabs %s %s and waves it at you, "
									+ "trying to entice you to mount %s. You just laugh at %s pathetic display, "
									+ "destroying %s confidence.",
							getSelf().name(), getSelf().possessivePronoun(),
							getSelf().body.getRandomCock().describe(getSelf()),
							getSelf().directObject(),
							getSelf().possessivePronoun(),
							getSelf().possessivePronoun());
				}
			case normal:
				return String.format(
						"%s gets onto %s back and holds %s %s up for your appraisal."
								+ " You admit to yourself that it does seem rather appealing, and proceed"
								+ " to mount %s, enveloping the hard shaft in your %s.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().possessivePronoun(),
						getSelf().body.getRandomCock().describe(getSelf()),
						getSelf().directObject(),
						target.body.getRandomPussy().describe(target));
			case anal:
				return String.format(
						"%s gets on the ground and spreads %s ass for your viewing pleasure,"
								+ " practically begging you to fuck %s. Well, someone has to do it. You get on your"
								+ " knees and get to it.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().directObject());
			default: // special
				return String.format(
						"%s lays flat on %s back, spreading %s legs and then using"
								+ " %s fingers to open up %s labia to you. %s beady eyes, staring longingly"
								+ " at your %s overwhelm you, and you can't help but oblige, getting "
								+ "between %s legs and sheathing your shaft.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().possessivePronoun(),
						getSelf().possessivePronoun(),
						getSelf().possessivePronoun(),
						Global.capitalizeFirstLetter(
								getSelf().possessivePronoun()),
						target.body.getRandomCock().describe(target),
						getSelf().possessivePronoun());
		}
	}
}
