package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class Stunned extends Skill {

	public Stunned(Character self) {
		super("Stunned", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().stunned();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			if (Global.random(3) >= 2) {
				c.write(getSelf(), getSelf().stunLiner(c));
			} else {
				c.write(getSelf(), receive(c, 0, Result.normal, target));
			}
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Skill copy(Character user) {
		return new Stunned(user);
	}

	@Override
	public int speed() {
		return 0;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return "You're unable to move.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return getSelf().name()
				+ " is on the floor, trying to catch her breath.";
	}

	@Override
	public String describe(Combat c) {
		return "You're stunned";
	}
}
