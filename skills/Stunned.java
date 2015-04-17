package skills;
import global.Global;
import characters.Character;

import combat.Combat;
import combat.Result;



public class Stunned extends Skill {

	public Stunned(Character self) {
		super("Stunned", self);
	}

	@Override
	public boolean requirements() {
		return false;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.stunned();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			if(Global.random(3)>=2){
				c.write(self,self.stunLiner());
			}
			else{
				c.write(self,receive(c,0,Result.normal, target));
			}
		}
	}

	@Override
	public boolean requirements(Character user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Skill copy(Character user) {
		return new Stunned(user);
	}
	public int speed(){
		return 0;
	}
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You're unable to move.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" is on the floor, trying to catch her breath.";
	}

	@Override
	public String describe() {
		return "You're stunned";
	}
}
