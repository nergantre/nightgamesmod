package skills;

import status.CounterStatus;
import characters.Character;
import combat.Combat;
import combat.Result;

public abstract class CounterBase extends Skill {
	private String description;
	public CounterBase(String name, Character self, int cooldown, String description) {
		super(name, self, cooldown);
		this.description = description;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.setup, target));
		} else {
			c.write(getSelf(), receive(c, 0, Result.setup, target));
		}
		getSelf().add(c, new CounterStatus(getSelf(), this, description));
		return true;
	}

	public int getMojoCost(Combat c) {
		return 0;
	}

	public abstract void resolveCounter(Combat c, Character target);

	public int speed(){
		return 10;
	}
}
