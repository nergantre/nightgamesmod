package skills;

import stance.Mount;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Straddle extends Skill {

	public Straddle(Character self) {
		super("Mount", self);
		
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		
		return c.getStance().mobile(self)&&c.getStance().mobile(target)&&c.getStance().prone(target)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		c.setStance(new Mount(self,target));
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Straddle(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You straddle "+target.name()+" using your body weight to hold her down.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" plops herself down on top of your stomach.";
	}

	@Override
	public String describe() {
		return "Straddles opponent";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
