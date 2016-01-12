package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class Flick extends Skill {

	public Flick(Character self) {
		super("Flick", self, 3);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.crotchAvailable() && c.getStance().reachBottom(getSelf())
				&& getSelf().canAct() && !getSelf().has(Trait.shy);
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 5;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.roll(this, c, accuracy(c))) {
			if (target.has(Trait.brassballs)) {
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.weak, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.weak, target));
				}
			} else {
				int mojoLost = 25;
				int m = Global.random(6) + 5;
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, m, Result.normal, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, m, Result.normal, target));
				}
				if (target.has(Trait.achilles)) {
					m += 2 + Global
							.random(target.get(Attribute.Perception) / 2);
					mojoLost = 40;
				}
				target.pain(c, m);
				target.loseMojo(c, mojoLost);
				getSelf().emote(Emotion.dominant, 10);
				target.emote(Emotion.angry, 15);
				target.emote(Emotion.nervous, 15);
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
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction) >= 17
				&& !user.has(Trait.softheart);
	}

	@Override
	public Skill copy(Character user) {
		return new Flick(user);
	}

	@Override
	public int speed() {
		return 6;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.miss) {
			return "You flick your finger between " + target.name()
					+ "'s legs, but don't hit anything sensitive.";
		} else if (modifier == Result.weak) {
			return "You flick " + target.name() + "'s balls, but "
					+ target.pronoun() + " seems utterly unfazed.";
		} else {
			if (target.hasBalls()) {
				return "You use two fingers to simultaneously flick both of "
						+ target.name()
						+ " dangling balls. She tries to stifle a yelp and jerks her hips away reflexively. "
						+ "You feel a twinge of empathy, but she's done far worse.";
			} else {
				return "You flick your finger sharply across " + target.name()
						+ "'s sensitive clit, causing her to yelp in surprise and pain. She quickly covers her girl parts "
						+ "and glares at you in indignation.";
			}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.miss) {
			return getSelf().name()
					+ " flicks at your balls, but hits only air.";
		} else if (modifier == Result.weak) {
			return getSelf().name() + " flicks your balls, but "
					+ " you barely feel a thing.";
		} else {
			return getSelf().name()
					+ " gives you a mischievous grin and flicks each of your balls with her finger. It startles you more than anything, but it does hurt and "
					+ "her seemingly carefree abuse of your jewels destroys your confidence.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Flick opponent's genitals, which is painful and embarrassing";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
