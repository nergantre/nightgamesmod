package skills;

import stance.Behind;
import stance.Stance;
import status.Braced;
import status.Stsflag;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Turnover extends Skill {

	public Turnover(Character self) {
		super("Turn Over", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Power)>=6;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().enumerate()==Stance.standingover&&c.getStance().dom(self);
	}

	@Override
	public String describe() {
		return "Turn your opponent over and get behind her";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		if(!target.is(Stsflag.braced)){
			target.add(new Braced(target));
		}
		c.setStance(new Behind(self,target));
		target.emote(Emotion.dominant, 20);
	}

	@Override
	public Skill copy(Character user) {
		return new Turnover(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You turn "+target.name()+" onto her hands and knees. You moved behind her while she slowly gets up.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" rolls you onto your stomach. You push yourself back up, but she takes the opportunity to get behind you.";
	}

}
