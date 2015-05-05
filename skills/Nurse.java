package skills;

import global.Global;
import stance.Mount;
import stance.NursingHold;
import stance.Stance;
import status.Stsflag;
import status.Suckling;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;
import characters.body.BreastsPart;

import combat.Combat;
import combat.Result;

public class Nurse extends Skill {

	public Nurse(Character self) {
		super("Nurse", self);
		
	}

	@Override
	public boolean requirements(Character user) {
		return self.get(Attribute.Seduction) > 10;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.topless()&&c.getStance().reachTop(self)&&!c.getStance().behind(self)
				&& self.body.getLargestBreasts().size >= BreastsPart.c.size
				&&c.getStance().mobile(self)
				&&(!c.getStance().mobile(target)||c.getStance().prone(target))
				&&self.canAct();
	}
	
	@Override
	public float priorityMod(Combat c){
		return self.has(Trait.lactating) ? 3 : 0;
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		if (self.has(Trait.lactating)&&!target.is(Stsflag.suckling)&&!target.is(Stsflag.wary)) {
			c.write(target, Global.format("{other:SUBJECT-ACTION:are|is} a little confused at the sudden turn of events, but after milk starts flowing into {other:possessive} mouth, {other:pronoun} can't help but continue to suck on {self:possessive} teats.", self, target));
			target.add(new Suckling(target, self, 4));
		}
		if (c.getStance().en != Stance.nursing &&!c.getStance().penetration(self) &&!c.getStance().penetration(target)) {
			c.setStance(new NursingHold(self,target));
			self.emote(Emotion.dominant, 20);
		} else {
			(new Suckle(target)).resolve(c, self);
			self.emote(Emotion.dominant, 10);
		}
	}

	@Override
	public Skill copy(Character user) {
		return new Nurse(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		if (c.getStance().enumerate() != Stance.nursing) {
			return Tactics.positioning;
		} else {
			return Tactics.pleasure;
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.special) {
		return "You cradle "+target.name()+"'s head in your lap and press your " + self.body.getRandomBreasts().fullDescribe(self)
				+ " over her face. " + target.name() + " vocalizes a confused little yelp, and you take advantage of it to force your nipples between her lips.";
		} else {
			return "You gently stroke " + target.nameOrPossessivePronoun() + " hair as you feed your nipples to " + target.directObject() + ". " +
					"Even though she is reluctant at first, you soon have " + target.name() + " sucking your teats like a newborn.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.special) {
			return self.name()+" plops her " + self.body.getRandomBreasts().fullDescribe(self) +" in front of your face. You vision suddenly consists of only swaying titflesh." +
					" Giggling a bit, " + self.name() + " pokes your sides and slides her nipples in your mouth when you let out a yelp.";
		} else {
			return self.name() + " gently strokes your hair as she presents her nipples to your mouth. " +
					"Present with the opportunity, you happily suck on her breasts.";
		}
	}

	@Override
	public String describe() {
		return "Feed your nipples to your opponent";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
