package skills;

import stance.Mount;
import stance.ReverseMount;
import characters.Character;

import combat.Combat;
import combat.Result;

public class ReverseStraddle extends Skill {

	public ReverseStraddle(Character self) {
		super("Mount(Reverse)", self);
		
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
		c.setStance(new ReverseMount(self,target));
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ReverseStraddle(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You straddle "+target.name()+", facing her feet.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" sits on your chest, facing your crotch.";
	}

	@Override
	public String describe() {
		return "Straddle facing groin";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
