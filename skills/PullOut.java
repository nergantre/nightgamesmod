package skills;

import stance.Neutral;
import stance.Stance;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class PullOut extends Skill {

	public PullOut(Character self) {
		super("Pull Out", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().penetration(self)&&c.getStance().dom(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(c.getStance().en ==Stance.anal){
			if(self.human()){
				c.write(self,deal(c,0,Result.anal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.anal, target));
			}
		}
		else{
			if (self.hasStatus(Stsflag.cockbound)) {
				c.write(self,"You try to pull out of "+target.name()+"'s " + target.body.getRandomPussy() + ", but her pussy-tongue tongue instantly react and pulls your dick back in.");
				int m = 8;
				self.body.pleasure(target, target.body.getRandom("pussy"), self.body.getRandom("cock"), m, c);					
				return;
			} else if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			} else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
		}
		c.setStance(c.getStance().insert(self, self));
	}

	@Override
	public Skill copy(Character user) {
		return new PullOut(user);
	}

	@Override
	public Tactics type(Combat c) {
		// TODO Auto-generated method stub
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.anal){
			return "You pull your dick completely out of "+target.name()+"'s ass.";
		}
		return "You pull completely out of "+target.name()+"'s pussy, causing her to let out a disappointed little whimper.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.anal){
			return "You feel the pressure in your anus recede as "+self.name()+" pulls out.";
		}
		else{
			return self.name()+" lifts her hips more than normal, letting your dick slip completely out of her.";
		}
	}

	@Override
	public String describe() {
		return "Aborts penetration";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
