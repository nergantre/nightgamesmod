package skills;

import stance.Stance;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Suckle extends Skill {
	public Suckle(Character self) {
		super("Suckle", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.topless()&&c.getStance().reachTop(self)&&!c.getStance().behind(self)&&(self.canAct()||(c.getStance().enumerate()==Stance.nursing&&self.canRespond()))&&c.getStance().facing();
	}

	@Override
	public void resolve(Combat c, Character target) {
		Result results = target.has(Trait.lactating) ? Result.special : Result.normal;
		int m = 5 + Global.random(6);
		if(self.human()){
			c.offerImage("LickNipples.jpg", "Art by Fujin Hitokiri");
			c.write(self,deal(c,0,results, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,results, target));
		}
		if (self.has(Trait.silvertongue)) {
			m += 4;
		}
		target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("breasts"), m, c);
		if (results == Result.special) {
			self.tempt(c, target, (3 + target.body.getRandomBreasts().size) * 2);
			target.buildMojo(c, 10);
		} else {
			self.buildMojo(c, 10);
		}
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Suckle(user);
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return "You slowly circle your tongue around each of "+target.name()+"'s nipples, and start sucking like a newborn.";
		} else {
			return "You slowly circle your tongue around each of "+target.name()+"'s nipples, and start sucking like a newborn. " +
					"Her milk slides smoothly down your throat, and you're left with a warm comfortable feeling.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return self.name()+" licks and sucks your nipples, sending a surge of excitement straight to your groin.";
		} else {
			return self.name()+" licks and sucks your nipples, drawing forth a gust of breast milk from your teats. " +
					"She drinks deeply of your milk, gurggling happily as more of the smooth liquid flows down her throat.";
		}
	}

	@Override
	public String describe() {
		return "Suck your opponent's nipples";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
