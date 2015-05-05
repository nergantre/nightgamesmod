package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Undress extends Skill {

	public Undress(Character self) {
		super("Undress", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&!c.getStance().sub(self)&&!self.nude()&&!c.getStance().prone(self)&&!self.has(Trait.strapped);
	}

	@Override
	public String describe() {
		return "Removes your own clothes";
	}

	@Override
	public void resolve(Combat c, Character target) {	
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.undress(c);
	}

	@Override
	public Skill copy(Character user) {
		return new Undress(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You quickly strip off your clothes and toss them aside.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" puts some space between you and strips naked.";
	}
}
