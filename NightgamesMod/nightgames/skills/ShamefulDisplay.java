package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Horny;
import nightgames.status.Shamed;

public class ShamefulDisplay extends Skill {

	public ShamefulDisplay(Character self) {
		super("Shameful Display", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getPure(Attribute.Submissive) >= 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf()) && getSelf().crotchAvailable()
				&& (getSelf().hasDick() || getSelf().hasPussy());
	}

	@Override
	public String describe(Combat c) {
		return "Degrade yourself to entice your opponent";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(receive(c, 0, Result.normal, target));
		}
		getSelf().add(new Shamed(getSelf()));
		target.add(new Horny(target, getSelf().get(Attribute.Submissive) / 4, 2, " (Dominant Thrill)"));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ShamefulDisplay(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (getSelf().hasDick()) {
			return String.format(
					"You spread your legs, exposing your naked %s and balls, and thrust your hips"
							+ " out in a show of submission. %s practically drools at the sight, "
							+ "while you struggle to bear the shame.",
					getSelf().body.getRandomCock().describe(getSelf()), Global.capitalizeFirstLetter(target.pronoun()));
		} else {
			return String.format(
					"You spread your legs and dip a hand between them. You stare lustfully"
							+ " at %s while you finger yourself, breathing heavily. You feel a thrill"
							+ " at opening yourself up like this, and %s seems quite taken with the sight.",
					target.name(), target.pronoun());
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (getSelf().hasDick()) {
			return String.format(
					"%s fondles %s %s while looking at you with an almost daring look."
							+ " %s seems to find the situation arousing, and so do you.",
					getSelf().name(), getSelf().possessivePronoun(), getSelf().body.getRandomCock().describe(getSelf()),
					Global.capitalizeFirstLetter(getSelf().pronoun()));
		} else {
			return String.format(
					"%s lifts %s hips and spreads %s pussy lips open. %s's "
							+ "bright red with shame, but the sight is lewd enough to drive you wild.",
					getSelf().name(), getSelf().possessivePronoun(), getSelf().possessivePronoun(),
					Global.capitalizeFirstLetter(getSelf().pronoun()));
		}
	}

}
