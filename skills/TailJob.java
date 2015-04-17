package skills;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;



public class TailJob extends Skill {

	public TailJob(Character self) {
		super("Tailjob", self);
	}

	@Override
	public boolean requirements() {
		boolean enough = self.getPure(Attribute.Seduction)>=20 || self.getPure(Attribute.Animism)>=0; 
		return enough && self.body.get("tail").size() > 0;
	}

	@Override
	public boolean requirements(Character user) {
		boolean enough = self.getPure(Attribute.Seduction)>=20 || self.getPure(Attribute.Animism)>=0;
		return enough && user.body.get("tail").size() > 0;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&target.pantsless()&&c.getStance().mobile(self)&&!c.getStance().mobile(target)&&!c.getStance().penetration(target);
	}

	@Override
	public String describe() {
		return "Use your tail to tease your opponent";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		int m = (5 + Global.random(10)) * (100 + self.getArousal().percent())/100;
		String receiver;
		if (target.hasDick()) {
			receiver = "cock";
		} else {
			receiver = "pussy";
		}
		target.body.pleasure(self, self.body.getRandom("tail"), target.body.getRandom(receiver), m, c);
	}

	@Override
	public Skill copy(Character user) {
		return new TailJob(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You skillfully use your flexible " +self.body.getRandom("tail").describe(self) + " to stroke and tease "+target.name()+"'s sensitive girl parts.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" teases your sensitive dick and balls with her " + self.body.getRandom("tail").describe(self) + ". It wraps completely around your shaft and strokes firmly.";
	}

}
