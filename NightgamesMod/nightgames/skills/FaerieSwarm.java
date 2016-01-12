package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class FaerieSwarm extends Skill {

	public FaerieSwarm(Character self) {
		super("FaerieSwarm", self, 2);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getPure(Attribute.Arcane) >= 2;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf())
				&& !c.getStance().prone(getSelf())
				&& getSelf().has(Item.MinorScroll);
	}

	@Override
	public String describe(Combat c) {
		return "Briefly unleash a crowd of mischievious faeries: Minor Scroll";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.MinorScroll, 1);
		if (target.getOutfit().isNude()) {
			if (getSelf().human()) {
				c.write(deal(c, 0, Result.normal, target));
			} else if (target.human()) {
				c.write(receive(c, 0, Result.normal, target));
			}
			target.body.pleasure(getSelf(), null, null,
					25 + Global.random(getSelf().get(Attribute.Arcane)), c);
		} else {
			if (getSelf().human()) {
				c.write(deal(c, 0, Result.weak, target));
			} else if (target.human()) {
				c.write(receive(c, 0, Result.weak, target));
			}
			target.undress(c);
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new FaerieSwarm(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.weak) {
			return "You unroll the summoning scroll and unleash a cloud of cute, naked faeries."
					+ "They immediately swarm around " + target.name()
					+ ", grabbing and pulling at " + target.possessivePronoun()
					+ " clothes. By the time they disappear, "
					+ target.pronoun() + "'s left completely naked.";
		}
		return

		"You unroll the summoning scroll and unleash a cloud of cute, naked faeries. "
				+ "They eagerly take advantage of " + target.name()
				+ "'s naked body, teasing and tickling every exposed erogenous zone. "
				+ Global.capitalizeFirstLetter(target.pronoun())
				+ " tries in vain to defend " + target.directObject()
				+ "self, but there are too many of them and they're too quick. "
				+ Global.capitalizeFirstLetter(target.pronoun())
				+ "'s reduced to writhing and giggling in pleasure until "
				+ "the brief summoning spell exires.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.weak) {
			return getSelf().name()
					+ " pulls out a scroll and a swarm of butterfly-winged faeries burst forth to attack you. They mischeviously grab at your clothes, using magical assistance "
					+ "to efficiently strip you naked.";
		}
		return

		getSelf().name()
				+ " pulls out a scroll and a swarm of butterfly-winged faeries burst forth to attack you. A couple of them fly into your face to distract you with naked girl "
				+ "parts, while the rest play with your naked body. They focus especially on your "
				+ (target.hasDick() ? "dick and balls"
						: target.hasPussy() ? "pussy and clit"
								: "ass and chest")
				+ ", dozens of tiny hands playfully immobilizing you with ticklish pleasure. The spell "
				+ "doesn't actaully last very long, but from your perspective, it feels like minutes of delightful torture.";
	}

}
