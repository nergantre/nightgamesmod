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
	public void resolve(Combat c, Character target) {
		self.spendMojo(c, getMojoCost());
		if (self.human()) {
			c.write(self, deal(c, 0, Result.setup, target));
		} else {
			c.write(self, receive(c, 0, Result.setup, target));
		}
		self.add(new CounterStatus(self, this, description));
	}

	public int getMojoCost() {
		return 0;
	}

	public abstract void resolveCounter(Combat c, Character target);

	public int speed(){
		return 10;
	}
}
