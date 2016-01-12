package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Winded;

public class SelfStun extends Skill {

	public SelfStun(Character self) {
		super("Stun Self", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !getSelf().stunned() && getSelf().canAct();
	}

	@Override
	public String describe(Combat c) {
		return "Stun yourself. For Debugging!";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().add(c, new Winded(getSelf()));
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (getSelf().human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new SelfStun(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You stun yourself. Yup.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "She stuns herself. Yup.";
	}

}
