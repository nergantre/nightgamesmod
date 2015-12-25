package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;
import nightgames.status.Tied;

public class TortoiseWrap extends Skill {

	public TortoiseWrap(Character self) {
		super("Tortoise Wrap", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getPure(Attribute.Fetish) >= 21;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().reachTop(getSelf())
				&& !c.getStance().reachTop(target) && getSelf().has(Item.Rope)
				&& c.getStance().dom(getSelf()) && !target.is(Stsflag.tied)
				&& getSelf().is(Stsflag.bondage);
	}

	@Override
	public String describe(Combat c) {
		return "User your bondage skills to wrap your opponent to increase her sensitivity";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Rope, 1);
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		target.add(new Tied(target));
		target.add(new Hypersensitive(target));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new TortoiseWrap(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return String.format(
				"You skillfully tie a rope around %s's torso "
						+ "in a traditional bondage wrap. %s moans softly as the "
						+ "rope digs into %s supple skin.",
				target.name(), nightgames.global.Global.capitalizeFirstLetter(
						target.pronoun()),
				target.possessivePronoun());
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return String.format(
				"%s ties you up with a complex series of knots. "
						+ "Surprisingly, instead of completely incapacitating you, "
						+ "%s wraps you in a way that only "
						+ "slightly hinders your movement. However, the discomfort of "
						+ "the rope wrapping around you seems to make your sense of "
						+ "touch more pronounced.",
				getSelf().name(), getSelf().pronoun());
	}

}
