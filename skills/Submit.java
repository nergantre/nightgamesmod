package skills;

import stance.Mount;
import stance.StandingOver;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Submit extends Skill {

	public Submit(Character self) {
		super("Submit", self);
		
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(self)&&self.canAct()&&!c.getStance().prone(self)&&!c.getStance().sub(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		c.setStance(new StandingOver(target, self));
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Submit(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You nervously lie down on the floor.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" with a nervous glance, lies down on the floor.";
	}

	@Override
	public String describe() {
		return "Submits to your opponent by lying down.";
	}
}
