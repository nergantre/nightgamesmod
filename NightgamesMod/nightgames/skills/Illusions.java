package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Alluring;
import nightgames.status.Distorted;

public class Illusions extends Skill {

	public Illusions(Character self) {
		super("Illusions", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Arcane) >= 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf())
				&& !c.getStance().prone(getSelf());
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public String describe(Combat c) {
		return "Create illusions to act as cover: 20 Mojo";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		getSelf().add(c, new Distorted(getSelf(), 6));
		getSelf().add(c, new Alluring(getSelf(), 5));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Illusions(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return "You cast an illusion spell to create several images of yourself. At the same time, you add a charm to make yourself irresistible.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return getSelf().name()
				+ " casts a brief spell and your vision is filled with naked copies of her. You can still tell which "
				+ getSelf().name()
				+ " is real, but it's still a distraction. At the same time, she suddenly looks irresistible.";
	}
}
