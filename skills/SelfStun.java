package skills;

import status.Winded;
import characters.Character;

import combat.Combat;
import combat.Result;

public class SelfStun extends Skill {

	public SelfStun(Character self) {
		super("Stun Self", self);
	}

	@Override
	public boolean requirements() {
		return self.human();
	}

	@Override
	public boolean requirements(Character user) {
		return user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !self.stunned() && self.canAct();
	}

	@Override
	public String describe() {
		return "Stun yourself. For Debugging!";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.add(new Winded(self));
		if (self.human()) {
			c.write(self, deal(c, 0, Result.normal, target));
		} else if(self.human()) {
			c.write(self, receive(c, 0, Result.normal, target));
		}
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
