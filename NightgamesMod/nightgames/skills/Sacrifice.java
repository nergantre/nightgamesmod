package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class Sacrifice extends Skill {

	public Sacrifice(Character self) {
		super("Sacrifice", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Dark) >= 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && !c.getStance().sub(getSelf())
				&& getSelf().getArousal().percent() >= 70;
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public String describe(Combat c) {
		return "Damage yourself to reduce arousal";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		getSelf().weaken(c, 20 + getSelf().get(Attribute.Dark));
		getSelf().calm(c, getSelf().getArousal().max() / 3 + 20
				+ getSelf().get(Attribute.Dark));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Sacrifice(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.calming;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return "You feed your own lifeforce and pleasure to the darkness inside you. Your legs threaten to give out, but you've regained some self control.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return getSelf().name()
				+ " pinches her nipples hard while screaming in pain. You see her stagger in exhaustion, but she seems much less aroused.";
	}
}
