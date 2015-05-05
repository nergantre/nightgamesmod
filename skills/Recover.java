package skills;

import stance.Neutral;
import status.Braced;
import status.Stsflag;
import status.Wary;
import global.Global;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Recover extends Skill {

	public Recover(Character self) {
		super("Recover", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {		
		return c.getStance().prone(self)&&c.getStance().mobile(self)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		if(!self.is(Stsflag.braced)){
			self.add(new Braced(self));
		}
		c.setStance(new Neutral(self,target));
		self.heal(c, Global.random(3));
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Recover(user);
	}
	public int speed(){
		return 0;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You pull yourself up, taking a deep breath to restore your focus.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" scrambles back to her feet.";
	}

	@Override
	public String describe() {
		return "Stand up";
	}
}
