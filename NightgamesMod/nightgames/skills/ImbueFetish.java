package nightgames.skills;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;

public class ImbueFetish extends Skill {

	private static final List<String> POSSIBLE_FETISHES = Collections
			.unmodifiableList(Arrays.asList("pussy", "breasts", "feet", "ass", "cock"));

	private String chosenFetish;

	public ImbueFetish(Character self) {
		super("Imbue Fetish", self, 3);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Fetish) >= 10;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
				&& !c.getStance().inserted();
	}

	@Override
	public int getMojoCost(Combat c) {
		return 25;
	}

	@Override
	public String describe(Combat c) {
		return "Bestow a random fetish on your opponent: 15 Mojo";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		chosenFetish = Global
				.pickRandom(POSSIBLE_FETISHES.stream().filter(part -> getSelf().body.has(part)).toArray(String[]::new));
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		target.add(new BodyFetish(target, getSelf(), chosenFetish,
				Global.randomdouble() * .2 + getSelf().get(Attribute.Fetish) * .01));
		chosenFetish = null;
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ImbueFetish(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You brandish one of your fetish needles - this one imbued with a " + chosenFetish
				+ " fetish - and stick it in " + target.name()
				+ "'s arm. The needle is far too thin to cause any harm, but the "
				+ "mind-altering substance immediately takes effect.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "You feel a tiny prick in your arm, and when you look you see a small"
				+ " needle sticking out. You remove the needle, but when you look back at " + getSelf().name()
				+ " - who has a maniacal look on " + getSelf().possessivePronoun()
				+ " face - you feel an unnaturally strong attraction towards " + getSelf().possessivePronoun() + " "
				+ chosenFetish + ".";
	}

}
