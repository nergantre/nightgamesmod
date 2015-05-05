package skills;

import stance.Stance;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.AssPart;
import combat.Combat;
import combat.Result;

public class Anilingus extends Skill {
	public Anilingus(Character self) {
		super("Lick Ass", self);
	}

	@Override
	public boolean requirements(Character user) {
		return (self.has(Trait.shameless) || self.has(Trait.Unflappable) || self.get(Attribute.Seduction) > 30);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.pantsless()&&target.body.has("ass")&&c.getStance().oral(self)&&self.canAct()&&!c.getStance().penetration(self);
	}

	@Override
	public float priorityMod(Combat c) {
		return (self.has(Trait.silvertongue) ? 1 : 0);
	}

	@Override
	public void resolve(Combat c, Character target) {
		AssPart targetAss = (AssPart) target.body.getRandom("ass");
		Result result = Result.normal;
		int m = 0; int n = 0;
		if (c.getStance().enumerate() == Stance.facesitting) {
			result = Result.reverse;
			m = 4 + Global.random(6);
			n = 10;
		} else if(target.roll(this, c, accuracy())){
			m = 4 + Global.random(6);
			if(self.has(Trait.silvertongue)){
				m += 4;
				result = Result.special;
			}
		} else {
			result = Result.miss;
		}
		if (self.human()) {
			c.write(self,deal(c, m, result, target));
		} else {
			c.write(self,receive(c, m, result, target));
		}
		if (m > 0) {
			target.body.pleasure(self, self.body.getRandom("mouth"), targetAss, m, c);
		}
		if (n > 0) {
			target.buildMojo(c, n);
		}
	}

	@Override
	public Skill copy(Character user) {
		return new Anilingus(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss) {
			return "You try to lick "+target.name()+"'s rosebud, but she pushes your head away.";
		} else if(modifier==Result.special) {
			return "You gently rim "
					+ target.name()
					+ "'s asshole with your tongue, sending shivers through her body.";
		} else if (modifier==Result.reverse) {
			return "With " + target.nameOrPossessivePronoun() + " ass pressing into your face, you helplessly give in and take an experimental lick at her pucker.";
		}
		return "You thrust your tongue into "
				+ target.name()
				+ "'s ass and lick it, making her yelp in suprise.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss) {
			return self.name() + " closes in on your behind, but you manage to push her head away.";
		} else if(modifier==Result.special) {
			return self.name() + " gently rims your asshole with her tongue, sending shivers through your body.";
		} else if (modifier==Result.reverse) {
			return "With your ass pressing into " + self.nameOrPossessivePronoun() + " face, she helplessly gives in and starts licking your ass.";
		}
		return self.name() + " licks your tight asshole, both surprising and arousing you.";
	}

	@Override
	public String describe() {
		return "Perfom anilingus on opponent";
	}
}
