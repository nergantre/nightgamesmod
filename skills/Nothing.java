package skills;

import characters.Character;
import combat.Combat;
import combat.Result;

public class Nothing extends Skill {

	public Nothing(Character self) {
		super("Nothing", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return true;
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (self.human()) {
			deal(c, 0, Result.normal, target);
		} else {
			receive(c, 0, Result.normal, target);
		}
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Nothing(user);
	}

	public int speed(){
		return 0;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
			return "You are unable to do anything.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
			return self.subject() + "is unable to do anything.";
	}

	@Override
	public String describe() {
		return "Do nothing";
	}
}
