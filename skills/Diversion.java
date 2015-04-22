package skills;

import characters.Character;
import characters.Trait;
import stance.Behind;
import status.Flatfooted;

import combat.Combat;
import combat.Result;

public class Diversion extends Skill {

	public Diversion(Character self) {
		super("Diversion", self);
	}

	@Override
	public boolean requirements() {
		return self.has(Trait.misdirection);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.misdirection);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && self.canAct()&&c.getStance().mobile(self)&&!self.nude()&&!c.getStance().prone(self)&&!c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.topless()){
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else{
				c.write(self,receive(c,0,Result.special, target));
			}
			self.strip(1, c);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else{
				c.write(self,receive(c,0,Result.normal, target));
			}
			self.strip(0, c);
		}
		self.buildMojo(c, 75);	
		c.setStance(new Behind(self,target));
		target.add(new Flatfooted(target,1));
	}

	@Override
	public Skill copy(Character user) {
		return new Diversion(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return "You quickly strip off your "+self.top.peek().name()+" and throw it to the right, while you jump to the left. "+target.name()+" catches your discarded clothing, " +
					"losing sight of you in the process.";
		}
		else{
			return "You quickly strip off your "+self.bottom.peek().name()+" and throw it to the right, while you jump to the left. "+target.name()+" catches your discarded clothing, " +
					"losing sight of you in the process.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if(modifier==Result.normal){
			return "You lose sight of "+self.name()+" for just a moment, but then see her moving behind you in your peripheral vision. You quickly spin around and grab her, " +
					"but you find yourself holding just her "+self.top.peek().getName()+". Wait... what the fuck?";
		}
		else{
			return "You lose sight of "+self.name()+" for just a moment, but then see her moving behind you in your peripheral vision. You quickly spin around and grab her, " +
					"but you find yourself holding just her "+self.bottom.peek().getName()+". Wait... what the fuck?";
		}
	}

	@Override
	public String describe() {
		return "Throws your clothes as a distraction";
	}

}
