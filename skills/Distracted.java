package skills;

import status.Stsflag;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Distracted extends Skill {

	public Distracted(Character self) {
		super("Distracted", self);
	}

	@Override
	public boolean requirements() {
		return false;
	}

	@Override
	public boolean requirements(Character user) {
		return false;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.distracted()&&!self.is(Stsflag.enthralled);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, self));
		}
	}

	@Override
	public Skill copy(Character user) {
		return new Distracted(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You miss your opportunity to act.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		return attacker.name()+" looks a little unfocused and makes no attempt to defend herself.";
	}

	@Override
	public String describe() {
		return "Caught off guard";
	}

}
